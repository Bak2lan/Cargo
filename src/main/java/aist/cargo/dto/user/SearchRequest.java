package aist.cargo.dto.user;

import aist.cargo.enums.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;

public record SearchRequest(
        String fromWhere,
        String toWhere,
        LocalDate dispatchDate,
        LocalDate arrivalDate,
        @Enumerated(EnumType.STRING)
        PackageType packageType,
        @Enumerated(EnumType.STRING)
        Size size,
        @Enumerated(EnumType.STRING)
        Role role
) {
}