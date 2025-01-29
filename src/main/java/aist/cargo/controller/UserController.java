package aist.cargo.controller;

import aist.cargo.dto.user.AuthenticationSignInResponse;
import aist.cargo.dto.user.AuthenticationSignUpResponse;
import aist.cargo.dto.user.SignInRequest;
import aist.cargo.dto.user.SignUpRequest;
import aist.cargo.entity.User;
import aist.cargo.exception.AlreadyExistException;
import aist.cargo.exception.InvalidTokenException;
import aist.cargo.service.AuthenticationService;
import aist.cargo.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UserController {
    private final AuthenticationService userService;
    private final UserService userCrudService;
    @Autowired
    private MessageSource messageSource;
    private static final String REDIRECT_LOGIN = "redirect:/login";

    @PostMapping("/signUp")
    public AuthenticationSignUpResponse signUp(@Valid @RequestBody SignUpRequest request) throws AlreadyExistException {
        AuthenticationSignUpResponse response = userService.signUp(request);
        log.info("User successfully saved with the identifier!!!");
        return response;
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String token, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(token)) {
            redirectAttributes.addFlashAttribute("tokenError", "Token is invlid");
            return REDIRECT_LOGIN;
        }
        try {
            userService.verifyUser(token);
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("tokenError", "Token is invlid");
            return REDIRECT_LOGIN;
        }
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage("verification.email.msg", null, LocaleContextHolder.getLocale()));
        return REDIRECT_LOGIN;
    }

    @PostMapping("/signIn")
    public AuthenticationSignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
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