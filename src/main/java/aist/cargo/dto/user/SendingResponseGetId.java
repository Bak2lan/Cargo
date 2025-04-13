package aist.cargo.dto.user;
import aist.cargo.enums.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Builder
public class SendingResponseGetId {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String description;
    private String fromWhere;
    private String toWhere;
    private LocalDate dispatchDate;
    private LocalDate arrivalDate;
    private String imageUser;
    private Size size;
}
