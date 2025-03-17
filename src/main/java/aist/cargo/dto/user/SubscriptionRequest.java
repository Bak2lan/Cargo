package aist.cargo.dto.user;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionRequest {
    @Schema(description = "Жазылуунун узактыгы, мүмкүн болгон маанилер: ONE_MONTH (1 ай, 99 руб), THREE_MONTH (3 ай, 999 руб), SIX_MONTH (6 ай, 1499 руб), TWELVE_MONTH (12 ай, 1999 руб)")
    @NotNull(message = "Duration cannot be null")
    private SubsDuration duration;

    @Schema(description = "Транспорт түрү")
    @NotNull(message = "Transport type cannot be null")
    private TransportType transportType;

    public SubscriptionRequest(SubsDuration duration, TransportType transportType) {
        this.duration = duration;
        this.transportType = transportType;
    }
}
