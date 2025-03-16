package aist.cargo.dto.user;

import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreateResponse {
    private Long id;
    private double price;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userEmail;
    private TransportType transportType;
    private SubsDuration duration;
}
