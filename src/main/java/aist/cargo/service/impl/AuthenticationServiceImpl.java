package aist.cargo.service.impl;

import aist.cargo.config.JwtService;
import aist.cargo.dto.user.AuthenticationSignInResponse;
import aist.cargo.dto.user.AuthenticationSignUpResponse;
import aist.cargo.dto.user.SignInRequest;
import aist.cargo.dto.user.SignUpRequest;
import aist.cargo.entity.SecureToken;
import aist.cargo.entity.User;
import aist.cargo.exception.AlreadyExistException;
import aist.cargo.exception.BadCredentialException;
import aist.cargo.exception.InvalidTokenException;
import aist.cargo.exception.NotFoundException;
import aist.cargo.mailing.AccountVerificationEmailContext;
import aist.cargo.mailing.EmailService;
import aist.cargo.repository.UserRepository;
import aist.cargo.service.AuthenticationService;
import aist.cargo.service.SecureTokenService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecureTokenService secureTokenService;
    private final EmailService emailService;
    @Value("${site.base.url.https}")
    private  String baseUrl;

    @Override
    public AuthenticationSignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistException("Пользователь с адресом электронной почты:"
                    + request.getEmail() + " уже существует");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        sendRegistrationConfirmationEmail(user);
        String jwtToken = jwtService.generateToken(user);
        log.info("Пользователь успешно сохранен с идентификатором:" + user.getId());
        return new AuthenticationSignUpResponse(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                jwtToken,
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public void sendRegistrationConfirmationEmail(User user) {
        SecureToken secureToken = secureTokenService.createToken();
        secureToken.setUser(user);
        secureTokenService.saveSecureToken(secureToken);

        AccountVerificationEmailContext context = new AccountVerificationEmailContext();
        context.init(user);
        context.setToken(secureToken.getToken());
        context.buildVerificationUrl(baseUrl, secureToken.getToken());

        try {
            emailService.sendMail(context);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyUser(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if (Objects.isNull(token) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()) {
            throw new InvalidTokenException("Токен недействителен.");
        }
        User user = userRepository.getById(secureToken.getUser().getId());
        if (Objects.isNull(user)) {
            return false;
        }
        user.setAccountVerified(true);
        user.setEmailConfirmed(true);
        userRepository.save(user);
        secureTokenService.removeToken(secureToken);
        return true;
    }

    @Override
    public AuthenticationSignInResponse signIn(SignInRequest signInRequest) {
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
        return AuthenticationSignInResponse.builder()
                .id(user.getId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .token(token)
                .email(user.getEmail())
                .build();
    }
}