package aist.cargo.dto.user;

import aist.cargo.enums.Size;
import lombok.Builder;
import java.time.LocalDate;
@Builder
public record CargoResponseGetAll (
    Long id,
     String fullName,
    String transportNumber,
    String fromWhere,
    LocalDate dispatchDate,
    String toWhere,
    LocalDate arrivalDate,
    Size size,
    Long random,
   boolean isOwner
) {
}

