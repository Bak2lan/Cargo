package aist.cargo.service;
import aist.cargo.dto.user.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface DeliveryService {
    CargoResponse getDeliveryById(Long deliveryId);

    List<CargoResponse> getAllCargo(SearchRequest searchRequest);


    SimpleResponseCreate createDelivery(DeliveryRequest deliveryRequest, String userEmail);

    ResponseEntity<String> archiveDelivery(Long deliveryId);

    ResponseEntity<String> activateDelivery(Long deliveryId);

    List<CargoResponse> getAllArchivedDeliveries();
}