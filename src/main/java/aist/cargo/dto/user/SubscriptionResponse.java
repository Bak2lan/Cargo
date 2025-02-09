package aist.cargo.dto.user;

import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private double price;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userEmail;
    private TransportType transportType;
    private SubsDuration duration;
    private String status;
}
