package aist.cargo.entity;

import aist.cargo.base.Transport;
import aist.cargo.enums.TransportType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "cars")
@Getter
public class Car extends Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_gen")
    @SequenceGenerator(name = "car_gen", sequenceName = "car_seq",allocationSize = 1,initialValue = 5)

    private Long id;
    private String carNumber;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private List<Luggage> luggages;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private List<Box> boxes;
    @OneToMany(cascade =CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private List<Envelope> envelopes;
    public Car(String name, String image, String fromWhere, String toDestination, LocalDate departureDate, LocalDate arriveDate, LocalTime departureTime, String description, User user, Subscription subscription, TransportType transportType, String carNumber, List<Luggage> luggages, List<Box> boxes, List<Envelope> envelopes) {
        super(name, image, fromWhere, toDestination, departureDate, arriveDate, departureTime, description, user, subscription, transportType);
        this.carNumber = carNumber;
        this.luggages = luggages;
        this.boxes = boxes;
        this.envelopes = envelopes;
    }

    public Car() {
    }
}
