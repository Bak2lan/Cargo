package aist.cargo.entity;
import aist.cargo.enums.PackageType;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subs_gen")
    @SequenceGenerator(name = "subs_gen", sequenceName = "subs_seq",allocationSize = 1,initialValue = 11)
    private Long id;
    private double  price;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private User user;
    @OneToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private Payment payment;
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    @Enumerated(EnumType.STRING)
    private SubsDuration duration;

    @Enumerated
    private PackageType packageType;


}
