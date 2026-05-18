package com.library.userservice.service;

import com.library.userservice.dto.UserRequestDTO;
import com.library.userservice.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO request);
}

