package aist.cargo.service;

import aist.cargo.dto.user.SendingResponse;

public interface SendingService {
    SendingResponse getSendingById(Long sendingId);
}