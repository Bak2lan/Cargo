package aist.cargo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "secureTokens")
public class SecureToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long secureTokenId;
    @Column(unique = true)
    private String token;
    private Timestamp timestamp;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(updatable = false)
    @Basic(optional = false)
    private LocalDateTime expiredAt;
    public boolean isExpired() {
        return getExpiredAt().isBefore(LocalDateTime.now());
    }
}
