package aist.cargo.entity;

import aist.cargo.base.Transport;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "air_Planes")
@Getter
public class AirPlane extends Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airPlane_gen")
    @SequenceGenerator(name = "airPlane_gen", sequenceName = "airPlane_seq",allocationSize = 1,initialValue = 5)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "air_Plane_id")
    private List<Luggage> luggages;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "air_Plane_id")
    private List<Box> boxes;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "air_Plane_id")
    private List<Envelope> envelopes;

}
