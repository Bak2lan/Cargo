package aist.cargo.service.impl;

import aist.cargo.entity.User;
import aist.cargo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPassword(updatedUser.getPassword());
                    existingUser.setConfirmPassword(updatedUser.getConfirmPassword());
                    existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                    existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
                    existingUser.setRole(updatedUser.getRole());
                    existingUser.setSubscriptions(updatedUser.getSubscriptions());
                    existingUser.setPayments(updatedUser.getPayments());
                    existingUser.setDeliveries(updatedUser.getDeliveries());
                    existingUser.setSendings(updatedUser.getSendings());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
