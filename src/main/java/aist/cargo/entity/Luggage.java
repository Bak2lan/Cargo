package aist.cargo.entity;

import aist.cargo.base.Package;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "luggages")
@Getter
public class Luggage extends Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lug_gen")
    @SequenceGenerator(name = "lug_gen", sequenceName = "lug_seq",allocationSize = 1,initialValue = 5)
    private Long id;
}
