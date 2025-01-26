package aist.cargo.base;

import aist.cargo.entity.Subscription;
import aist.cargo.entity.User;
import aist.cargo.enums.PackageType;
import aist.cargo.enums.Size;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@MappedSuperclass
@Getter
@Setter
public class Package {
    private String name;
    private String image;
    private String fromWhere;
    private String toDestination;
    private LocalDate departureDate;
    private LocalDate arriveDate;
    private LocalTime departureTime;
    private String description;
    @Enumerated(EnumType.STRING)
    private Size size;
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
            CascadeType.REFRESH,

    })
    private Subscription subscription;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    public Package(String name, String image, String fromWhere, String toDestination, LocalDate departureDate, LocalDate arriveDate, LocalTime departureTime, String description, Size size, User user, Subscription subscription, PackageType packageType) {
        this.name = name;
        this.image = image;
        this.fromWhere = fromWhere;
        this.toDestination = toDestination;
        this.departureDate = departureDate;
        this.arriveDate = arriveDate;
        this.departureTime = departureTime;
        this.description = description;
        this.size = size;
        this.user = user;
        this.subscription = subscription;
        this.packageType = packageType;
    }

    public Package() {
    }
}
