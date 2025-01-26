package aist.cargo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_gen")
    @SequenceGenerator(name = "pay_gen", sequenceName = "pay_seq",allocationSize = 1,initialValue = 5)
    private Long id;
    private String cardNumber;
    private int cvcCode;
    private String expiryDate;
    @ManyToOne
    private User user;
    @ManyToOne
    private Subscription subscription;

    public Payment(String cardNumber, int cvcCode, String expiryDate, User user, Subscription subscription) {
        this.cardNumber = cardNumber;
        this.cvcCode = cvcCode;
        this.expiryDate = expiryDate;
        this.user = user;
        this.subscription = subscription;
    }

    public Payment() {
    }

}
