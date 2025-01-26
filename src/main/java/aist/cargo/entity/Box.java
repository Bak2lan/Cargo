package aist.cargo.entity;

import aist.cargo.base.Package;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "boxes")
@Getter
public class Box extends Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "box_gen")
    @SequenceGenerator(name = "box_gen", sequenceName = "box_seq",allocationSize = 1,initialValue = 5)

    private Long id;
}
