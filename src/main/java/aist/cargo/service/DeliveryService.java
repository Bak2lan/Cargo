package aist.cargo.service;

import aist.cargo.dto.user.CargoResponse;
import aist.cargo.dto.user.SearchRequest;
import java.util.List;

public interface DeliveryService {
    CargoResponse getDeliveryById(Long deliveryId);
    List<CargoResponse> getAllCargo(SearchRequest searchRequest);

    void archiveDelivery(Long id);

    void activateDelivery(Long id);

    List<CargoResponse> getArchivedDeliveries(String email);
}