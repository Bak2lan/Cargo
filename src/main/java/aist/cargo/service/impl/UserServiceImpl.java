package aist.cargo.service.impl;

import aist.cargo.dto.user.SimpleResponse;
import aist.cargo.dto.user.UserRequest;
import aist.cargo.dto.user.UserRequestProfile;
import aist.cargo.dto.user.UserResponse;
import aist.cargo.entity.*;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.UserRepository;
import aist.cargo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("User with this email already exist!");
        }
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setRole(userRequest.getRole());
        return userRepository.save(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequestProfile userRequest) {
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
            updatedUser.setFirstName(userRequest.getFirstName());
            updatedUser.setLastName(userRequest.getLastName());
            updatedUser.setEmail(userRequest.getEmail());
            updatedUser.setPhoneNumber(userRequest.getPhoneNumber());
            updatedUser.setDateOfBirth(userRequest.getDateOfBirth());
            updatedUser.setId(updatedUser.getId());
            userRepository.save(updatedUser);
            return UserResponse.builder()
                    .image(userRequest.getImage())
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .dateOfBirth(userRequest.getDateOfBirth())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .email(userRequest.getEmail())
                    .id(updatedUser.getId()).build();
    }

    @Override
    public SimpleResponse deleteUser(Long id) {
        User user = userRepository.getUserById(id);
        if (user != null) {
            List<Subscription> subscriptions = user.getSubscriptions();
            for (Subscription subscription : subscriptions) {
                subscription.setUser(null);
            }
            List<Delivery> deliveries = user.getDeliveries();
            for (Delivery delivery : deliveries) {
                delivery.setUser(null);
            }
            List<Payment> payments = user.getPayments();
            for (Payment payment : payments) {
                payment.setUser(null);
            }
            List<Sending> sendings = user.getSendings();
            for (Sending sending : sendings) {
                sending.setUser(null);
            }
            userRepository.deleteById(user.getId());
            log.info("User with id {} is deleted", id);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Deleted").build();
        } else throw new NotFoundException("User not found");
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        return UserResponse
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .dateOfBirth(user.getDateOfBirth()).build()).toList();
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> {
                    log.info("User with email: " + email + " not found!");
                    return new NotFoundException(String.format("Пользователь с адресом электронной почты: %s не найден!", email));
                });
    }
}