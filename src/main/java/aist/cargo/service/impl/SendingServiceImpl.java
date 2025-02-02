package aist.cargo.service.impl;

import aist.cargo.dto.user.SendingResponse;
import aist.cargo.entity.User;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.UserRepository;
import aist.cargo.repository.jdbcTemplate.SendingJDBCTemplate;
import aist.cargo.service.SendingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendingServiceImpl implements SendingService {
    private final UserRepository userRepository;
    private final SendingJDBCTemplate sendingJDBCTemplate;

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> {
                    log.info("User with email: " + email + " not found!");
                    return new NotFoundException(String.format("Пользователь с адресом электронной почты: %s не найден!", email));
                });
    }

    @Override
    public List<SendingResponse> getAllSendingResponse() {
        User user = getAuthenticatedUser();
        return sendingJDBCTemplate.getAllSending(user.getEmail());
    }
}