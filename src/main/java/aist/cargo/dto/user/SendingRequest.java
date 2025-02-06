package aist.cargo.dto.user;

import aist.cargo.enums.PackageType;
import aist.cargo.enums.Size;
import aist.cargo.enums.SubsDuration;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SendingRequest {
    private String firstName;
    private String lastName;
    private String fromWhere;
    private String toWhere;
    private String description;
    private LocalDate dispatchDate;
    private LocalDate arrivalDate;
    private PackageType packageType;
    private Size size;
    private SubsDuration subsDuration;  // если нужно для проверки подписки
}
