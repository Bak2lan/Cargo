package aist.cargo.dto.user;

import aist.cargo.enums.Role;
import aist.cargo.validation.EmailValidation;
import aist.cargo.validation.PasswordValidation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class UserRequestProfile {
    private String firstName;
    private String lastName;
    @EmailValidation
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String image;
}
