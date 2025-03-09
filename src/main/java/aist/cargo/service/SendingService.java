package aist.cargo.service;
import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.dto.user.SimpleResponseCreate;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SendingService {

    SimpleResponseCreate createSending(SendingRequest sendingRequest);

    List<SendingResponse> getAllSendings();

    SendingResponse getSendingById(Long sendingId);

    void updateSending(Long sendingId, SendingRequest sendingRequest);

    void deleteSending(Long sendingId);

    ResponseEntity<String> archiveSending(Long sendingId);

    ResponseEntity<String> activateSending(Long sendingId);


    List<SendingResponse> getAllArchived();

    List<SendingResponse> getAllSendingsUser();

        SimpleResponseCreate createSending(SendingRequest sendingRequest, String userEmail);
}

