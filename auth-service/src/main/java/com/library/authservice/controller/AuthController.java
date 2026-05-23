package com.library.authservice.controller;


import com.library.authservice.dto.LoginRequestDTO;
import com.library.authservice.dto.LoginResponseDTO;
import com.library.authservice.dto.RegisterRequestDTO;
import com.library.authservice.dto.UserResponseDTO;
import com.library.authservice.security.JwtService;
import com.library.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {

        String token = jwtService.generateToken(request.getEmail());

        return new LoginResponseDTO(token);
    }

    @PostMapping("/register")
    public UserResponseDTO registerUser(@RequestBody RegisterRequestDTO request) {

        return userService.registerUser(request);
    }
}
