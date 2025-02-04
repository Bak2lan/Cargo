package aist.cargo.service.impl;

import aist.cargo.dto.user.SearchRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.entity.User;
import aist.cargo.repository.jdbcTemplate.SendingJDBCTemplate;
import aist.cargo.service.SendingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendingServiceImpl implements SendingService {
    private final UserServiceImpl userServiceImpl;
    private final SendingJDBCTemplate sendingJDBCTemplate;


    @Override
    public SendingResponse getSendingById(Long sendingId) {
        return sendingJDBCTemplate.getSendingById(sendingId);
    }

    @Override
    public List<SendingResponse> getAllSendingResponse(SearchRequest searchRequest) {
        User user = userServiceImpl.getAuthenticatedUser();
        return sendingJDBCTemplate.getAllSending(user.getEmail());
    }
}