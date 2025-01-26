package aist.cargo.base;

import aist.cargo.entity.Subscription;
import aist.cargo.entity.User;
import aist.cargo.enums.TransportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@MappedSuperclass
@Getter
@Setter
public class Transport {
    private String name;
    private String image;
    private String fromWhere;
    private String toDestination;
    private LocalDate departureDate;
    private LocalDate arriveDate;
    private LocalTime departureTime;
    private String description;
    @OneToOne
    private User user;
    @OneToOne
    private Subscription subscription;
    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    public Transport(String name, String image, String fromWhere, String toDestination, LocalDate departureDate, LocalDate arriveDate, LocalTime departureTime, String description, User user, Subscription subscription, TransportType transportType) {
        this.name = name;
        this.image = image;
        this.fromWhere = fromWhere;
        this.toDestination = toDestination;
        this.departureDate = departureDate;
        this.arriveDate = arriveDate;
        this.departureTime = departureTime;
        this.description = description;
        this.user = user;
        this.subscription = subscription;
        this.transportType = transportType;
    }

    public Transport() {
    }
}
