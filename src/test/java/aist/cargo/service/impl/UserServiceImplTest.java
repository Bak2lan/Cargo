package aist.cargo.service.impl;

import aist.cargo.dto.user.UserRequest;
import aist.cargo.entity.User;
import aist.cargo.enums.Role;
import aist.cargo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("John");
        userRequest.setLastName("Doe");
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("password123");
        userRequest.setPhoneNumber("1234567890");
        userRequest.setDateOfBirth(LocalDate.parse("2000-01-01"));
        userRequest.setRole(Role.USER);

        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(userRequest);

        assertNotNull(createdUser);
        assertEquals(userRequest.getFirstName(), createdUser.getFirstName());
        assertEquals(userRequest.getLastName(), createdUser.getLastName());
        assertEquals(userRequest.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowException_whenEmailExists() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("existing@example.com");

        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.createUser(userRequest));
        assertEquals("User with this email already exist!", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}