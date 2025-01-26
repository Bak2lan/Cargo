package aist.cargo.entity;

import aist.cargo.base.Transport;
import aist.cargo.enums.TransportType;
import aist.cargo.enums.TruckSize;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "trucks")
@Getter
public class Truck extends Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "truck_gen")
    @SequenceGenerator(name = "truck_gen", sequenceName = "truck_seq",allocationSize = 1,initialValue = 5)
    private Long id;
    private String truckNumber;
    @Enumerated(EnumType.STRING)
    private TruckSize truckSize;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "truck_id")
    private List<Luggage> luggages;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "truck_id")
    private List<Box> boxes;

    public Truck(String name, String image, String fromWhere, String toDestination, LocalDate departureDate, LocalDate arriveDate, LocalTime departureTime, String description, User user, Subscription subscription, TransportType transportType, String truckNumber, TruckSize truckSize, List<Luggage> luggages, List<Box> boxes) {
        super(name, image, fromWhere, toDestination, departureDate, arriveDate, departureTime, description, user, subscription, transportType);
        this.truckNumber = truckNumber;
        this.truckSize = truckSize;
        this.luggages = luggages;
        this.boxes = boxes;

    }
    public Truck() {
    }
}
