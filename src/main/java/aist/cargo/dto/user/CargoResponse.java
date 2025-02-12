package aist.cargo.dto.user;

import aist.cargo.enums.PackageType;
import aist.cargo.enums.Role;
import aist.cargo.enums.Size;
import aist.cargo.enums.Status;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record CargoResponse(
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
        PackageType packageType,
        Size size,
        Status status,
        String phoneNumber,
        Role roleType
) {
}