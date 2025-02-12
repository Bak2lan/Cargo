package aist.cargo.dto.user;

import aist.cargo.enums.PackageType;
import aist.cargo.enums.Size;
import aist.cargo.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SendingResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String description;
    private String fromWhere;
    private String toWhere;
    private LocalDate dispatchDate;
    private LocalDate arrivalDate;
    private PackageType packageType;
    private Size size;
    private Status status;

}

