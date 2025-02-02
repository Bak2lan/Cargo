package aist.cargo.dto.user;

import aist.cargo.enums.PackageType;
import aist.cargo.enums.Size;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record DeliveryResponse(
        Long sendingId,
        Long userId,
        String userImage,
        String fullName,
        String transportNumber,
        String description,
        String fromWhere,
        LocalDate dispatchDate,
        String toWhere,
        LocalDate arrivalDate,
        PackageType packageType,
        Size size,
        String phoneNumber
) {
}