package aist.cargo.controller;

import aist.cargo.dto.user.DeliveryResponse;
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
    public DeliveryResponse getDeliveryById(@PathVariable Long deliveryId) {
        return deliveryService.getDeliveryById(deliveryId);
    }

    @GetMapping
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }
}