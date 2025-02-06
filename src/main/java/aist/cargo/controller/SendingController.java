package aist.cargo.controller;

import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.service.SendingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sendings")
public class SendingController {

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

    @GetMapping("/get/{id}")
    public ResponseEntity<SendingResponse> getSendingById(@PathVariable Long id) {
        SendingResponse response = sendingService.getSendingById(id);
        return ResponseEntity.ok(response);
    }

    // Создание новой отправки
    @PostMapping("/create/{id}")
    public boolean createSending(@RequestBody SendingRequest request) {

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
}
