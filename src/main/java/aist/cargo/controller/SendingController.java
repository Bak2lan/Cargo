package aist.cargo.controller;

import aist.cargo.dto.user.AutoCompleteRequest;
import aist.cargo.dto.user.LocationResponse;
import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.service.SendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/sendings")
public class SendingController {
    @Autowired
    private final SendingService sendingService;

    public SendingController(SendingService sendingService) {
        this.sendingService = sendingService;
    }



    @GetMapping("/auto")
    public ResponseEntity<List<LocationResponse>> autocomplete(@RequestParam String query) {
        List<LocationResponse> suggestions = new ArrayList<>();

        // Пример обработки запроса к внешнему API (например, OpenStreetMap)
        String apiUrl = "https://nominatim.openstreetmap.org/search?q=" + query + "&format=json";

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(apiUrl, LocationResponse[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                suggestions = Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(suggestions);
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

    @GetMapping("/autocomplete")
    public ResponseEntity<?> getAutocomplete(@RequestParam String query) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" + query + "&format=json&addressdetails=1";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при запросе автодополнения: " + e.getMessage());
        }
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<?> sendPackage(@RequestBody AutoCompleteRequest autoCompleteRequest) {
        return this.sendingService.userSendPackage(autoCompleteRequest);
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

