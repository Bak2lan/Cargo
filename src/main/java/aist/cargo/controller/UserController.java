package aist.cargo.controller;

import aist.cargo.dto.user.*;
import aist.cargo.entity.User;
import aist.cargo.service.AuthenticationService;
import aist.cargo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userCrudService;

    @PostMapping("/signUp")
    public SignUpResponse sendOtp(@RequestBody SignUpRequest signUpRequest) {
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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userCrudService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userCrudService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userCrudService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userCrudService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userCrudService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}