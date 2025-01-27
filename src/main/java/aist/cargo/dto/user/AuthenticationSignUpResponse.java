package aist.cargo.dto.user;

import aist.cargo.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationSignUpResponse{
        private Long id;
        private String fullName;
        private String image;
        private String token;
        private String email;
        private Role role;

    public AuthenticationSignUpResponse(Long id, String fullName, String image, String token, String email, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.image = image;
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public AuthenticationSignUpResponse(Long id, String fullName, String token, String email, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.token = token;
        this.email = email;
        this.role = role;
    }
}