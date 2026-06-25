package com.library.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.authservice.dto.LoginRequestDTO;
import com.library.authservice.dto.LoginResponseDTO;
import com.library.authservice.dto.RegisterRequestDTO;
import com.library.authservice.dto.UserResponseDTO;
import com.library.authservice.handler.GlobalExceptionHandler;
import com.library.authservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Debe hacer login correctamente y retornar 200")
    void shouldLoginSuccessfully() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("juan@email.com");
        request.setPassword("123456");

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("fake-jwt-token");

        when(userService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    @DisplayName("Debe registrar usuario correctamente y retornar 201")
    void shouldRegisterSuccessfully() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Juan Pérez");
        request.setEmail("juan@email.com");
        request.setPassword("123456");

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setName("Juan Pérez");
        response.setEmail("juan@email.com");

        when(userService.registerUser(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@email.com"));
    }

    @Test
    @DisplayName("Debe manejar RuntimeException en login y retornar 400")
    void shouldHandleRuntimeExceptionOnLogin() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("juan@email.com");
        request.setPassword("123456");

        when(userService.login(any(LoginRequestDTO.class)))
                .thenThrow(new RuntimeException("Password incorrecta"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Error en la solicitud"))
                .andExpect(jsonPath("$.details").value("Password incorrecta"));
    }

    @Test
    @DisplayName("Debe manejar RuntimeException en register y retornar 400")
    void shouldHandleRuntimeExceptionOnRegister() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Juan Pérez");
        request.setEmail("juan@email.com");
        request.setPassword("123456");

        when(userService.registerUser(any(RegisterRequestDTO.class)))
                .thenThrow(new RuntimeException("El correo ya está registrado"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Error en la solicitud"))
                .andExpect(jsonPath("$.details").value("El correo ya está registrado"));
    }
}