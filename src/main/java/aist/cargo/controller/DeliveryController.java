package aist.cargo.controller;

import aist.cargo.dto.user.CargoResponse;
import aist.cargo.dto.user.DeliveryRequest;
import aist.cargo.dto.user.SearchRequest;
import aist.cargo.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PostMapping("create/Delivery")
    public String createDelivery(@RequestBody DeliveryRequest deliveryRequest, Principal principal) {
        return deliveryService.createDelivery(deliveryRequest, principal.getName());

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