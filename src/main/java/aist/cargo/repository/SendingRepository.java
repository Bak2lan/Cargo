package aist.cargo.repository;

import aist.cargo.entity.Sending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SendingRepository extends JpaRepository<Sending, Long> {
    List<Sending> findByUserId(Long userId);
}
