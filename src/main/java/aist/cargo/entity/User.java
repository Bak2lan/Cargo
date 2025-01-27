package aist.cargo.entity;

import aist.cargo.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq",allocationSize = 1,initialValue = 5)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean accountVerified;
    private boolean loginDisabled;
    private boolean emailConfirmed;
    @OneToMany(mappedBy = "user")
    Set<SecureToken> tokens;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Payment> payments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Delivery> deliveries;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Sending> sendings;

    public User(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate dateOfBirth, Role role, List<Subscription> subscriptions, List<Payment> payments, List<Delivery> deliveries, List<Sending> sendings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.subscriptions = subscriptions;
        this.payments = payments;
        this.deliveries = deliveries;
        this.sendings = sendings;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}