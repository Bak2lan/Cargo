package aist.cargo.service;

import aist.cargo.dto.user.*;

public interface AuthenticationService {
    SignUpResponse signUp(SignUpRequest request);
    SimpleResponse confirmEmail( String code);
    SignInResponse signIn(SignInRequest signInRequest);
}