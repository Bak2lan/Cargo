package aist.cargo.repository;

import aist.cargo.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d.fromWhere FROM Delivery d WHERE d.fromWhere = ?1")
    List<String> getSenderByFromWhere(@Param("address") String address);

    @Query("SELECT d.toWhere FROM Delivery d WHERE d.toWhere = ?1")
    List<String> getSenderByToWhere(@Param("address") String address);;
    Optional<Delivery> findByUserEmail(String email);
    boolean existsByTransportNumber(String transportNumber);
    boolean existsByRandom(Long random);
}
