package aist.cargo.dto.user;

import aist.cargo.validation.EmailValidation;
import aist.cargo.validation.PasswordValidation;

public record SignInRequest(
        @EmailValidation
        String email,
        @PasswordValidation
        String password
) {
}