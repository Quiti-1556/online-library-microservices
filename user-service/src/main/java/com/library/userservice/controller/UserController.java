package com.library.userservice.controller;

import com.library.userservice.dto.UserRequestDTO;
import com.library.userservice.dto.UserResponseDTO;
import com.library.userservice.entity.UserProfile;
import com.library.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO request) {

        return userService.createUser(request);
    }
    @GetMapping
    public List<UserProfile> getAllUsers() {

        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public UserProfile getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return "Usuario eliminado";
    }
}
