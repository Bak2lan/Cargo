package aist.cargo.entity;

import aist.cargo.enums.PackageType;
import aist.cargo.enums.Role;
import aist.cargo.enums.Size;
import aist.cargo.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "sendings")
@Getter
@Setter
public class Sending {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "send_gen")
    @SequenceGenerator(name = "send_gen",sequenceName = "send_seq",allocationSize = 1, initialValue = 5)
    private Long id;
    private String fromWhere;
    private String toWhere;
    private String description;
    private LocalDate dispatchDate;
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

    public Sending(String fromWhere, String toWhere, String description, LocalDate dispatchDate, LocalDate arrivalDate, PackageType packageType, Size size, User user) {
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.description = description;
        this.dispatchDate = dispatchDate;
        this.arrivalDate = arrivalDate;
        this.packageType = packageType;
        this.size = size;
        this.user = user;
    }

    public Sending() {
    }
}
