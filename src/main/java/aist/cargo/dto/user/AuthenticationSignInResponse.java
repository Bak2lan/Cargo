package aist.cargo.dto.user;

import lombok.Builder;

@Builder
public record AuthenticationSignInResponse(
        Long id,
        String fullName,
        String image,
        String token,
        String email
) {
}