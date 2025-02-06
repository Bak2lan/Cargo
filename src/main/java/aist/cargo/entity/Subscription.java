package aist.cargo.entity;
import aist.cargo.enums.PackageType;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subs_gen")
    @SequenceGenerator(name = "subs_gen", sequenceName = "subs_seq",allocationSize = 1,initialValue = 5)
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

    public Subscription(double price, LocalDate startDate, LocalDate endDate, User user, Payment payment, TransportType transportType, SubsDuration duration, PackageType packageType) {
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.payment = payment;
        this.transportType = transportType;
        this.duration = duration;
        this.packageType = packageType;
    }

    public Subscription() {
    }
}
