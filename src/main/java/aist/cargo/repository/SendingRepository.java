package aist.cargo.repository;

import aist.cargo.entity.Sending;
import aist.cargo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SendingRepository extends JpaRepository<Sending, Long> {

    @Query("SELECT s FROM Sending s WHERE s.status = :status")
    List<Sending> findByStatus(@Param("status") Status status);
}
