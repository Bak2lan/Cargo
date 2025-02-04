package aist.cargo.controller;

import aist.cargo.dto.user.SendingResponse;
import aist.cargo.service.SendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sendingApi")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class SendingController {
    private final SendingService sendingService;

    @GetMapping("/{sendingId}")
    public SendingResponse getSendingById(@PathVariable Long sendingId) {
        return sendingService.getSendingById(sendingId);
    }
}