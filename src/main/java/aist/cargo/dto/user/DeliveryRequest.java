package aist.cargo.dto.user;

import aist.cargo.enums.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeliveryRequest {
    private String fromWhere;
    private String toWhere;
    private String description;
    private LocalDate dispatchDate;
    private LocalDate arrivalDate;
    private String fullName;
    private String transportNumber;
    private TransportType transportType;
    private PackageType packageType;
    private TruckSize truckSize;
    private Size size;
    private Role role;
}
