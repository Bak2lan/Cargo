package aist.cargo.service;
import aist.cargo.dto.user.*;
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

    SimpleResponseCreate createTrueSending(SendingCreateRequest sendingRequest);

    List<SendingResponse> getAllArchived();

    List<SendingResponse> getAllSendingsUser();

    SimpleResponseCreateSending createSending(SendingCreateUpdateRequest sendingRequest);

    SimpleResponseCreate updateSending(SendingUpdateRequest sendingRequest);
}

