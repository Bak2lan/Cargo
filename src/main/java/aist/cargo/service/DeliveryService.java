package aist.cargo.service;

import aist.cargo.dto.user.CombinedResponse;
import aist.cargo.dto.user.CargoResponse;
import aist.cargo.dto.user.SearchRequest;

import java.util.List;

public interface DeliveryService {
    CargoResponse getDeliveryById(Long deliveryId);
    List<CargoResponse> getAllCargo(SearchRequest searchRequest);
}