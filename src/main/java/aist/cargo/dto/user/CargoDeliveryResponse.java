package aist.cargo.dto.user;
import aist.cargo.enums.Role;
import aist.cargo.enums.Size;

import java.time.LocalDate;

public record CargoDeliveryResponse(
        Long id,
        Long userId,
        String userImage,
        String fullName,
        String transportNumber,
        String description,
        String fromWhere,
        LocalDate dispatchDate,
        String toWhere,
        LocalDate arrivalDate,
        Size size,
        String phoneNumber,
        Role roleType
) {
}
