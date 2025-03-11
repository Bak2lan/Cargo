package aist.cargo.dto.user;

import aist.cargo.enums.Role;
import aist.cargo.validation.PasswordValidation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    @PasswordValidation
    private String password;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Role role;
    private String image;
}
