package aist.cargo.repository;

import aist.cargo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> getUserByEmail(String email);
    User getUserById(Long id);
    @Query("SELECT COUNT(s) > 0 FROM Subscription s WHERE s.user.email = :email")
    boolean existsSubscriptionsByUserEmail(@Param("email") String email);
    Optional<User> findByEmail(String email);
}