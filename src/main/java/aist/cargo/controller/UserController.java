package aist.cargo.controller;

import aist.cargo.dto.user.*;
import aist.cargo.entity.User;
import aist.cargo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UserController {
    private final UserService userCrudService;


    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userCrudService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user details by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userCrudService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create new user", description = "Create a new user with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided")
    })
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userCrudService.createUser(userRequest));
    }

    @Operation(summary = "Update user", description = "Update an existing user's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userCrudService.updateUser(id, userRequest));
    }

    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SimpleResponse> deleteUser(@PathVariable Long id) {
        SimpleResponse simpleResponse = userCrudService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(simpleResponse);
    }

}