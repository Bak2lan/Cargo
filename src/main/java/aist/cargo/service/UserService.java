package aist.cargo.service;

import aist.cargo.dto.user.SimpleResponse;
import aist.cargo.dto.user.UserRequest;
import aist.cargo.dto.user.UserResponse;
import aist.cargo.entity.User;
import java.util.List;

public interface UserService {
    User createUser(UserRequest userRequest);

    User updateUser(Long id, UserRequest userRequest);

    SimpleResponse deleteUser(Long id);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();
}