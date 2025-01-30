package aist.cargo.dto.user;

import lombok.Builder;

@Builder
public record SignInResponse(
        Long id,
        String fullName,
        String token,
        String email
) {
}