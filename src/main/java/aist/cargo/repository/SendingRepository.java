package aist.cargo.repository;

import aist.cargo.entity.Delivery;
import aist.cargo.entity.Sending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SendingRepository extends JpaRepository<Sending, Long> {
    List<Sending> findByUserId(Long userId);

    @Query("SELECT d.fromWhere FROM Delivery d WHERE d.fromWhere = ?1")
    List<String> getSenderByFromWhere(@Param("address") String address);

    @Query("SELECT d.toWhere FROM Delivery d WHERE d.toWhere = ?1")
    List<String> getSenderByToWhere(@Param("address") String address);
    Optional<Sending> findByUserEmail(String email);


}
