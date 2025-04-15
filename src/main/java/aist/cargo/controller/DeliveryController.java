package aist.cargo.controller;
import aist.cargo.dto.user.*;
import aist.cargo.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public CargoDeliveryResponse getDeliveryById(@PathVariable Long deliveryId) {
        return deliveryService.getDeliveryById(deliveryId);
    }

    @GetMapping("/search")
    public List<CargoResponseGetAll> getAllCargo() {
        return deliveryService.getAllCargo();
    }

    @PostMapping("/deliveriesTrue")
    @Operation(
            summary = "Create a delivery if the user is authenticated",
            description = "This endpoint creates a new delivery if the user is authenticated. If the user is not authenticated, it returns an unauthorized response."
    )
    public ResponseEntity<SimpleResponseCreate> createDelivery(@RequestBody DeliveryForRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new SimpleResponseCreate("Колдонуучу кире элек", false));
        }
        return ResponseEntity.ok(deliveryService.TrueDelivery(request, principal.getName()));
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

    @PostMapping("/create")
    @Operation(summary = "Create a new delivery", description = "This endpoint creates a new delivery record based on the provided delivery request.")
    public ResponseEntity<SimpleResponseCreateDelivery> updateDelivery(@RequestBody DeliveryUpdateRequest deliveryRequest) {
        SimpleResponseCreateDelivery response = deliveryService.createDelivery(deliveryRequest);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Update delivery",
            description = "This endpoint allows the user to update delivery details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the delivery"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public ResponseEntity<SimpleResponseCreate> updateDelivery(@RequestBody DeliveryUpdateForRequest deliveryRequest) {
        SimpleResponseCreate response = deliveryService.updateDelivery(deliveryRequest);

        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }
}