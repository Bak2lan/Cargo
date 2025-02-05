package aist.cargo.service.impl;

import aist.cargo.dto.user.SendingResponse;
import aist.cargo.repository.jdbcTemplate.SendingJDBCTemplate;
import aist.cargo.service.SendingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendingServiceImpl implements SendingService {
    private final SendingJDBCTemplate sendingJDBCTemplate;

    @Override
    public SendingResponse getSendingById(Long sendingId) {
        return sendingJDBCTemplate.getSendingById(sendingId);
    }
}