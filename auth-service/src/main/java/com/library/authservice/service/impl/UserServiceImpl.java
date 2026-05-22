package com.library.authservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.library.authservice.dto.LoginRequestDTO;
import com.library.authservice.dto.RegisterRequestDTO;
import com.library.authservice.dto.UserResponseDTO;
import com.library.authservice.entity.User;
import com.library.authservice.repository.UserRepository;
import com.library.authservice.security.JwtService;
import com.library.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        return response;
    }
    @Override
    public String login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        if(!user.getPassword().equals(request.getPassword())) {

            throw new RuntimeException("Password incorrecta");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
