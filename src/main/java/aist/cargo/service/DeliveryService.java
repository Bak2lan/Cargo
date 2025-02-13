package aist.cargo.service;

import aist.cargo.dto.user.CargoResponse;
import aist.cargo.dto.user.DeliveryRequest;
import aist.cargo.dto.user.SearchRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeliveryService {
    CargoResponse getDeliveryById(Long deliveryId);

    List<CargoResponse> getAllCargo(SearchRequest searchRequest);

    String createDelivery(DeliveryRequest deliveryRequest, String userEmail);

    ResponseEntity<String> archiveDelivery(Long id);

    ResponseEntity<String> activateDelivery(Long id);

    List<CargoResponse> getAllArchivedDeliveries();
}