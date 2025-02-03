package aist.cargo.controller;

import aist.cargo.dto.user.*;
import aist.cargo.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public SignUpResponse sendOtp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return authenticationService.signUp(signUpRequest);
    }

    @PostMapping("/verify")
    public SimpleResponse verifyOtp(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        return authenticationService.confirmEmail(code);

    }

    @PostMapping("/signIn")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return authenticationService.signIn(signInRequest);
    }
}
