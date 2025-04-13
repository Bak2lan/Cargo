package aist.cargo.controller;
import aist.cargo.dto.user.*;
import aist.cargo.service.SendingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sendings")
public class SendingController {
    @Autowired
    private final SendingService sendingService;

    public SendingController(SendingService sendingService) {
        this.sendingService = sendingService;
    }

    // Получение всех отправок
    @Operation(
            summary = "Get all sendings",
            description = "This endpoint retrieves all the sendings from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all sendings"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<SendingResponse>> getSendings() {
        List<SendingResponse> sendings = sendingService.getAllSendings();
        return ResponseEntity.ok(sendings);
    }

    @Operation(
            summary = "Get all sendings for user",
            description = "This endpoint retrieves all the sendings for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sendings for the user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAll/sendingsUser")
    public List<SendingResponse> getAllUserSendings() {
        return sendingService.getAllSendingsUser();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SendingResponseGetId> getSendingById(@PathVariable Long id) {
        SendingResponseGetId response = sendingService.getSendingById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create true sending",
            description = "This API endpoint allows the authenticated user to create a true sending by providing the necessary details like 'fromWhere', 'toWhere', and other relevant data."
    )
    @PostMapping("/createTrue")
    public SimpleResponseCreate createSending(@RequestBody SendingCreateRequest request) {
        return sendingService.createTrueSending(request);
    }

    @Operation(
            summary = "Update sending by ID",
            description = "This endpoint updates the sending information based on the provided ID and sending details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sending updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to update this sending"),
            @ApiResponse(responseCode = "404", description = "Sending not found for the provided ID")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSending(@PathVariable Long id, @RequestBody SendingRequest sendingRequest) {
        try {
            sendingService.updateSending(id, sendingRequest);
            return ResponseEntity.ok("Sending updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSending(@PathVariable Long id) {
        try {
            sendingService.deleteSending(id);
            return ResponseEntity.ok("Отправка успешно удалено ");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/getAllArchived")
    @Operation(summary = "Все архивированные отправки")
    public ResponseEntity<List<SendingResponse>> getAllArchived() {
        List<SendingResponse> sendings = sendingService.getAllArchived();
        return ResponseEntity.ok(sendings);
    }

    @PutMapping("/archive/{senderId}")
    @Operation(summary = "Архивирует отправки", description = "Изменяет статус отправления на 'ARCHIVED'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sending archived successfully"),
            @ApiResponse(responseCode = "400", description = "Sending is already archived"),
            @ApiResponse(responseCode = "404", description = "Sending not found or does not belong to the user")
    })
    public ResponseEntity<String> archiveSending(@PathVariable Long senderId) {
        return sendingService.archiveSending(senderId);
    }

    @PutMapping("/activate/{senderId}")
    @Operation(summary = "Активирует отправки", description = "Изменяет статус отправления на 'ACTIVE'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sending activated successfully"),
            @ApiResponse(responseCode = "400", description = "Sending is already active"),
            @ApiResponse(responseCode = "404", description = "Sending not found or does not belong to the user")
    })
    public ResponseEntity<String> activateSending(@PathVariable Long senderId) {
        return sendingService.activateSending(senderId);
    }

    @PostMapping("CreateAll")
    @Operation(summary = "Создание отправки", description = "Этот эндпойнт позволяет создать отправку, передавая данные в теле запроса.")
    public SimpleResponseCreate createSendingAll(@RequestBody SendingRequest sendingRequest) {
        return sendingService.createSending(sendingRequest);
    }
    @Operation(
            summary = "Create a new sending",
            description = "This API endpoint allows the authenticated user to create a new sending. " +
                    "You need to provide the details such as 'fromWhere', 'toWhere', 'dispatchDate', " +
                    "and 'arrivalDate' for the new sending. A successful response will indicate the creation of the sending."
    )
    @PostMapping("/create")
    public ResponseEntity<SimpleResponseCreateSending> createSending(@RequestBody SendingCreateUpdateRequest sendingRequest) {
        SimpleResponseCreateSending response = sendingService.createSending(sendingRequest);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }
    @Operation(
            summary = "Update sending",
            description = "This API endpoint allows you to update the details of a sending for the authenticated user. " +
                    "It requires the sending ID and the updated fields such as 'fromWhere', 'toWhere', 'dispatchDate', etc."
    )
    @PutMapping("/update")
    public ResponseEntity<SimpleResponseCreate> updateSending(@RequestBody SendingUpdateRequest sendingRequest) {
        SimpleResponseCreate response = sendingService.updateSending(sendingRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

