package aist.cargo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpResponse {
        private Long id;
        private String token;
        private String email;
        private String message;


}