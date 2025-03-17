package aist.cargo.dto.user;
import aist.cargo.enums.TransportType;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionCreateResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userEmail;
    private TransportType transportType;
    private double price;

}
