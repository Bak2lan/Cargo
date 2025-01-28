package aist.cargo.dto.user;

import aist.cargo.validation.EmailValidation;
import aist.cargo.validation.PasswordValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SignUpRequest {
    @NotBlank(message = "Имя не должно быть пустым!")
    private String firstName;
    @NotBlank(message = "Фамилия не должна быть пустой!")
    private String lastName;
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
}