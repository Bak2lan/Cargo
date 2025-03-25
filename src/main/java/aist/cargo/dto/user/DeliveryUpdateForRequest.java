package aist.cargo.dto.user;

import aist.cargo.enums.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeliveryUpdateForRequest {
    private String userName;
    private String description;
    private LocalDate dispatchDate;
    private LocalDate arrivalDate;
    private Size size;
}
