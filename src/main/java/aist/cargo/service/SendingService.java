package aist.cargo.service;

import aist.cargo.dto.user.SendingResponse;
import java.util.List;

public interface SendingService {
    List<SendingResponse> getAllSendingResponse();
}