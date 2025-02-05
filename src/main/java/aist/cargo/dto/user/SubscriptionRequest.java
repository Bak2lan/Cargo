package aist.cargo.dto.user;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {

    @NotNull
    private Long userId;
    @NotNull
    private double price;
    @NotNull
    private TransportType transportType;
    @NotNull
    private SubsDuration duration;
}
