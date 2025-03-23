package aist.cargo.dto.user;
import aist.cargo.enums.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
@Getter
@Setter
public class DeliveryUpdateRequest {
    private String userName;
    private String transportNumber;
    private String fromWhere;
    private String toWhere;
    private String description;
    private LocalDate dispatchDate;
    private LocalDate arrivalDate;
    private Size size;

}
