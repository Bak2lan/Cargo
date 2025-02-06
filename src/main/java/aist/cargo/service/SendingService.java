package aist.cargo.service;

import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;

import java.util.List;

public interface SendingService {

    boolean createSending(SendingRequest sendingRequest);

    List<SendingResponse> getAllSendings();

    SendingResponse getSendingById(Long sendingId);

    void updateSending(Long sendingId, SendingRequest sendingRequest);

    void deleteSending(Long sendingId);

}
