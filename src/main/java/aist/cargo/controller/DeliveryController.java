package aist.cargo.controller;

import aist.cargo.dto.user.CargoResponse;
import aist.cargo.dto.user.DeliveryRequest;
import aist.cargo.dto.user.SearchRequest;
import aist.cargo.dto.user.SimpleResponseCreate;
import aist.cargo.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public SimpleResponseCreate createDelivery(@RequestBody DeliveryRequest deliveryRequest, Principal principal) {
        return deliveryService.createDelivery(deliveryRequest, principal.getName());

    }

    @GetMapping("/getAllArchived")
    @Operation(summary = "Все архивированные доставки")
    public ResponseEntity<List<CargoResponse>> getArchivedDeliveries() {
        List<CargoResponse> deliveries = deliveryService.getAllArchivedDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/archive/{deliveryId}")
    @Operation(summary = "Архивирует доставки", description = "Изменяет статус отправления на 'ARCHIVED'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery archived successfully"),
            @ApiResponse(responseCode = "400", description = "Delivery is already archived"),
            @ApiResponse(responseCode = "404", description = "Delivery not found or does not belong to the user")
    })
    public ResponseEntity<String> archiveDelivery(@PathVariable Long deliveryId) {
       return deliveryService.archiveDelivery(deliveryId);
    }

    @PutMapping("/activate/{id}")
    @Operation(summary = "Активирует доставки", description = "Изменяет статус отправления на 'ACTIVE'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery activated successfully"),
            @ApiResponse(responseCode = "400", description = "Delivery is already active"),
            @ApiResponse(responseCode = "404", description = "Delivery not found or does not belong to the user")
    })
    public ResponseEntity<String> activateDelivery(@PathVariable Long id) {
       return deliveryService.activateDelivery(id);
    }
}