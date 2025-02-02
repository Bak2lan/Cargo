package aist.cargo.service;

import aist.cargo.dto.user.DeliveryResponse;
import java.util.List;

public interface DeliveryService {
    List<DeliveryResponse> getAllDeliveries();
}