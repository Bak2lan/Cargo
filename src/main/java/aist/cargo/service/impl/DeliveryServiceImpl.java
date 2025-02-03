package aist.cargo.service.impl;

import aist.cargo.dto.user.DeliveryResponse;
import aist.cargo.entity.User;
import aist.cargo.repository.jdbcTemplate.DeliveryJDBCTemplate;
import aist.cargo.service.DeliveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryJDBCTemplate deliveryJDBCTemplate;
    private final UserServiceImpl userServiceImpl;

    @Override
    public DeliveryResponse getDeliveryById(Long deliveryId) {
        return deliveryJDBCTemplate.getDeliveryById(deliveryId);
    }

    @Override
    public List<DeliveryResponse> getAllDeliveries() {
        User user = userServiceImpl.getAuthenticatedUser();
        return deliveryJDBCTemplate.getAllDeliveries(user.getEmail());
    }
}