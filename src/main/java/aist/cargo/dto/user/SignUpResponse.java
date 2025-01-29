package aist.cargo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponse {
        private String token;
        private String email;
        private String message;

    public SignUpResponse(String token, String email, String message) {
        this.token = token;
        this.email = email;
        this.message = message;
    }
}