package aist.cargo.controller;

import aist.cargo.dto.user.CargoResponse;
import aist.cargo.dto.user.SearchRequest;
import aist.cargo.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveryApi")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class DeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping("/{deliveryId}")
    public CargoResponse getDeliveryById(@PathVariable Long deliveryId) {
        return deliveryService.getDeliveryById(deliveryId);
    }

    @GetMapping("/search")
    public List<CargoResponse> getAllCargo(@RequestBody SearchRequest searchRequest) {
        return deliveryService.getAllCargo(searchRequest);
    }
}