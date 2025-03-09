package aist.cargo.service.impl;

import aist.cargo.config.JwtService;
import aist.cargo.dto.user.*;
import aist.cargo.entity.OtpCode;
import aist.cargo.entity.User;
import aist.cargo.enums.Role;
import aist.cargo.exception.AlreadyExistException;
import aist.cargo.exception.BadCredentialException;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.OtpCodeRepository;
import aist.cargo.repository.UserRepository;
import aist.cargo.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final OtpCodeRepository otpCodeRepository;
    private final JavaMailSenderImpl mailSender;
    private final ConcurrentHashMap<String, User> pendingUsers = new ConcurrentHashMap<>();


    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistException("Пользователь с адресом электронной почты:"
                    + request.getEmail() + " уже существует");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailConfirmed(false);
        pendingUsers.put(request.getEmail(), user);
        sendOtpToEmail(request.getEmail());
        log.info("User successfully saved with the identifier: " + user.getEmail());
        String token = jwtService.generateToken(user);
        return new SignUpResponse(
                user.getId(),
                token,
                user.getEmail(),
                "Код отправлен на " + user.getEmail()
        );
    }

        @Override
        public SignInResponse signIn (SignInRequest signInRequest){
            User user = userRepository.getUserByEmail(signInRequest.email()).orElseThrow(() ->
                    new NotFoundException(String.format("Пользователь с адресом электронной почты: %s не найден!", signInRequest.email())));

            if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
                log.info("Недействительный пароль");
                throw new BadCredentialException("Недействительный пароль");
            }
            if (!user.isAccountVerified()) {
                throw new BadCredentialException("Сначала вы должны подтвердить свою электронную почту!");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.email(),
                            signInRequest.password()));

            String token = jwtService.generateToken(user);
            return SignInResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .token(token)
                    .email(user.getEmail())
                    .build();
        }

    @Override
    public SimpleResponse confirmEmail(String code) {
        Optional<OtpCode> otpOptional = otpCodeRepository.findOtpCodesByCode(code);
        if (!otpOptional.isPresent()) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Недействительный или истёкший OTP-код.")
                    .build();
        }
        OtpCode otpCode = otpOptional.get();
        if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("OTP-код истёк. Запросите новый.")
                    .build();
        }
        User user = pendingUsers.get(otpCode.getEmail());
        if (user == null) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("Пользователь не найден.")
                    .build();
        }
        user.setAccountVerified(true);
        user.setEmailConfirmed(true);
        userRepository.save(user);
        otpCodeRepository.delete(otpCode);
        pendingUsers.remove(otpCode.getEmail());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Email успешно подтверждён.")
                .build();
    }

    public String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    public void sendOtpToEmail(String email) {
        String otp = generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        log.info("Generated OTP: {} for email: {}", otp, email);
        otpCodeRepository.deleteByEmail(email);
        OtpCode otpCode = new OtpCode();
        otpCode.setEmail(email);
        otpCode.setCode(otp);
        otpCode.setExpiresAt(expirationTime);
        otpCodeRepository.save(otpCode);
        log.info("Saved OTP: {} for email: {}", otp, email);
        sendEmail(email, otp);
    }

    private void sendEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Ваш код подтверждения");
        message.setText("Ваш код подтверждения: " + otp + "\nКод действует 5 минут.");
        mailSender.send(message);
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void deleteExpiredOtpCodes() {
        LocalDateTime now = LocalDateTime.now();
        otpCodeRepository.deleteByExpiresAtBefore(now);
        System.out.println("Expired OTP codes deleted at: " + now);
    }
}