package aist.cargo.entity;

import aist.cargo.base.Package;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "envelopes")
@Getter
public class Envelope extends Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "env_gen")
    @SequenceGenerator(name = "env_gen", sequenceName = "env_seq",allocationSize = 1,initialValue = 5)
    private Long id;
}
