package aist.cargo.service;

import aist.cargo.dto.user.DeliveryResponse;
import java.util.List;

public interface DeliveryService {
    DeliveryResponse getDeliveryById(Long deliveryId);
    List<DeliveryResponse> getAllDeliveries();
}