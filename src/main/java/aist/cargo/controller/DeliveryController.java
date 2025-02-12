package aist.cargo.controller;

import aist.cargo.dto.user.CargoResponse;
import aist.cargo.dto.user.SearchRequest;
import aist.cargo.exception.NotFoundException;
import aist.cargo.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/archived")
    public ResponseEntity<List<CargoResponse>> getAllArchivedDeliveries(@RequestParam String email) {
        List<CargoResponse> archivedDeliveries = deliveryService.getArchivedDeliveries(email);
        return ResponseEntity.ok(archivedDeliveries);
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<String> archiveDelivery(@PathVariable Long id) {
        deliveryService.archiveDelivery(id);
        return ResponseEntity.ok("Delivery archived successfully");
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateDelivery(@PathVariable Long id) {
        deliveryService.activateDelivery(id);
        return ResponseEntity.ok("Delivery activated successfully");
    }
}