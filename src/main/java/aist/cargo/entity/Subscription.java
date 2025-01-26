package aist.cargo.entity;

import aist.cargo.base.Transport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subs_gen")
    @SequenceGenerator(name = "subs_gen", sequenceName = "subs_seq",allocationSize = 1,initialValue = 5)
    private Long id;
    private String description;
    private int price;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToMany(mappedBy = "subscriptions")
    private List<User> users;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "subscription")
    private List<Payment> payments;
    @OneToOne(mappedBy = "subscription")
    private Car car;
    @OneToOne (mappedBy = "subscription")
    private AirPlane airPlane;
    @OneToOne(mappedBy = "subscription")
    private Truck truck;
    @OneToOne (mappedBy = "subscription")
    private Luggage luggage;
    @OneToOne (mappedBy = "subscription")
    private Box box;
    @OneToOne(mappedBy = "subscription")
    private Envelope envelope;

    public Subscription(String description, int price, LocalDate startDate, LocalDate endDate, List<User> users, List<Payment> payments, Car car, AirPlane airPlane, Truck truck, Luggage luggage, Box box, Envelope envelope) {
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.users = users;
        this.payments = payments;
        this.car = car;
        this.airPlane = airPlane;
        this.truck = truck;
        this.luggage = luggage;
        this.box = box;
        this.envelope = envelope;
    }

    public Subscription() {
    }
}
