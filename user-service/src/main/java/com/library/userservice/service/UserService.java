package com.library.userservice.service;

import com.library.userservice.dto.UserRequestDTO;
import com.library.userservice.dto.UserResponseDTO;
import com.library.userservice.entity.UserProfile;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO request);
    List<UserProfile> getAllUsers();

    UserProfile getUserById(Long id);

    void deleteUser(Long id);
}

