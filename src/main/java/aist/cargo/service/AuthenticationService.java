package aist.cargo.service;

import aist.cargo.dto.user.AuthenticationSignInResponse;
import aist.cargo.dto.user.AuthenticationSignUpResponse;
import aist.cargo.dto.user.SignInRequest;
import aist.cargo.dto.user.SignUpRequest;
import aist.cargo.entity.User;
import aist.cargo.exception.InvalidTokenException;

public interface AuthenticationService {
    AuthenticationSignUpResponse signUp(SignUpRequest request);
    AuthenticationSignInResponse signIn(SignInRequest signInRequest);
    void sendRegistrationConfirmationEmail(User user);
    boolean verifyUser(String token) throws InvalidTokenException;
}