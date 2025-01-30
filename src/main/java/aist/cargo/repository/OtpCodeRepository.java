package aist.cargo.repository;

import aist.cargo.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findOtpCodesByCode(String code);
    void deleteByEmail(String email);
    void deleteByExpiresAtBefore(LocalDateTime expirationTime);
}