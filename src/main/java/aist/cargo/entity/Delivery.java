package aist.cargo.entity;

import aist.cargo.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Random;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_gen")
    @SequenceGenerator(name = "delivery_gen", sequenceName = "delivery_seq", allocationSize = 1, initialValue = 15)
    private Long id;
    private String userName;
    private String fromWhere;
    private String toWhere;
    private String description;
    private LocalDate dispatchDate;
    private LocalDate arrivalDate;
    private String transportNumber;
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    @Enumerated(EnumType.STRING)
    private TruckSize truckSize;
    @Enumerated(EnumType.STRING)
    private Size size;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    private Long random;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private User user;

    public Delivery(String userName, String fromWhere, String toWhere, String description, LocalDate dispatchDate, LocalDate arrivalDate, String transportNumber, TransportType transportType, PackageType packageType, TruckSize truckSize, Size size,Role role, User user) {
        this.userName = userName;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.description = description;
        this.dispatchDate = dispatchDate;
        this.arrivalDate = arrivalDate;
        this.transportNumber = transportNumber;
        this.transportType = transportType;
        this.packageType = packageType;
        this.truckSize = truckSize;
        this.size = size;
        this.role = role;
        this.user = user;
    }

    public Delivery() {
    }
}