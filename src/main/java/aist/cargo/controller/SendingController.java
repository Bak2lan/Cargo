package aist.cargo.controller;

import aist.cargo.dto.user.SendingResponse;
import aist.cargo.service.SendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/SendingApi")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class SendingController {
    private final SendingService sendingService;

    @GetMapping("/getAllSending")
    public List<SendingResponse> getAllSending(){
        return sendingService.getAllSendingResponse();
    }
}