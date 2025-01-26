package aist.cargo.entity;

import aist.cargo.enums.PaymentStatus;
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
    private String expiryDateOfCard;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private User user;
    @OneToOne(mappedBy="payment",cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private Subscription subscription;

    public Payment(String cardNumber, int cvcCode, String expiryDateOfCard, PaymentStatus status, User user, Subscription subscription) {
        this.cardNumber = cardNumber;
        this.cvcCode = cvcCode;
        this.expiryDateOfCard = expiryDateOfCard;
        this.status = status;
        this.user = user;
        this.subscription = subscription;
    }

    public Payment() {
    }

}
