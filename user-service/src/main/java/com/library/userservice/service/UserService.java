package com.library.userservice.service;

import com.library.userservice.dto.UserRequestDTO;
import com.library.userservice.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDTO request);
}

