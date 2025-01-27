package aist.cargo.service;

import aist.cargo.entity.SecureToken;

public interface SecureTokenService {
    SecureToken createToken();
    void saveSecureToken(SecureToken secureToken);
    SecureToken findByToken(String token);
    void removeToken(SecureToken token);
}