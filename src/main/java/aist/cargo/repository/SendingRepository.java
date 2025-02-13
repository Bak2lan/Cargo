package aist.cargo.repository;

import aist.cargo.entity.Sending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SendingRepository extends JpaRepository<Sending, Long> {
    @Query("SELECT s.fromWhere FROM Sending s WHERE s.fromWhere = ?1")
    List<String> getSenderByFromWhere(@Param("address") String address);

    @Query("SELECT s.toWhere FROM Sending s WHERE s.toWhere = ?1")
    List<String> getSenderByToWhere(@Param("address") String address);

}
