package aist.cargo.entity;

import aist.cargo.enums.PackageType;
import aist.cargo.enums.Role;
import aist.cargo.enums.Size;
import aist.cargo.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "sendings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sending {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "send_gen")
    @SequenceGenerator(name = "send_gen",sequenceName = "send_seq",allocationSize = 1, initialValue = 11)
    private Long id;
    private String fromWhere;
    private String toWhere;
    private String description;
    private LocalDate dispatchDate;
    private String userName;
    private LocalDate arrivalDate;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    @Enumerated(EnumType.STRING)
    private Size size;
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private User user;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;


}
