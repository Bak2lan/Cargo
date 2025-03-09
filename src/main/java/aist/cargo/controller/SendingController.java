package aist.cargo.controller;
import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.dto.user.SimpleResponseCreate;
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
    @GetMapping("/getAll")
    public ResponseEntity<List<SendingResponse>> getSendings() {
        List<SendingResponse> sendings = sendingService.getAllSendings();
        return ResponseEntity.ok(sendings);
    }

    @GetMapping("/getAll/sendingsUser")
    @Operation(summary = "get all sendings user", operationId = "Получить свои посылки")
    public List<SendingResponse> getAllUserSendings() {
        return sendingService.getAllSendingsUser();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SendingResponse> getSendingById(@PathVariable Long id) {
        SendingResponse response = sendingService.getSendingById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/{id}")
    public SimpleResponseCreate createSending(@RequestBody SendingRequest request) {
        return sendingService.createSending(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSending(@PathVariable Long id, @RequestBody SendingRequest sendingRequest) {
        try {
            sendingService.updateSending(id, sendingRequest);
            return ResponseEntity.ok("Отправка успешно обновлена");
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

}

