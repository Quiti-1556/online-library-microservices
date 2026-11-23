package com.library.authservice.security;

import com.library.authservice.controller.AdminAccountController;
import com.library.authservice.entity.*;
import com.library.authservice.entity.Role;
import com.library.authservice.repository.UserRepository;
import com.library.authservice.service.AdminAccountService;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.*;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminSecurityTest {
    @Configuration @EnableWebMvc @EnableWebSecurity
    @Import({SecurityConfig.class, JwtAuthenticationFilter.class, AdminAccountController.class, com.library.authservice.controller.AuthSessionController.class})
    static class Config {
        @Bean UserRepository users() { return mock(UserRepository.class); }
        @Bean AdminAccountService accounts() { return mock(AdminAccountService.class); }
        @Bean JwtService jwt() { return new JwtService(); }
    }
    AnnotationConfigWebApplicationContext context;
    MockMvc mvc;
    UserRepository users;
    JwtService jwt;
    User user;
    @BeforeEach void setup() {
        context = new AnnotationConfigWebApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(new org.springframework.core.env.MapPropertySource("test", Map.of(
                "jwt.secret", Base64.getEncoder().encodeToString("clave-exclusiva-de-pruebas-123456789".getBytes()), "jwt.expiration", "60000")));
        context.setServletContext(new MockServletContext()); context.register(Config.class); context.refresh();
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        users = context.getBean(UserRepository.class); jwt = context.getBean(JwtService.class);
        user = new User(1L, "Admin", "admin@example.com", "hash", Role.ADMIN);
    }
    @AfterEach void close() { context.close(); }
    @Test void protegeInclusoAccesoDirectoSinGateway() throws Exception {
        mvc.perform(get("/auth/users")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/auth/users/7")).andExpect(status().isUnauthorized());
    }
    @Test void permiteAdminYNoDevuelvePassword() throws Exception {
        when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        mvc.perform(get("/auth/users").header("Authorization", "Bearer " + jwt.generateToken(user))).andExpect(status().isOk());
    }
    @Test void clienteNoPuedeLeerCrearEditarNiEliminarCuentas() throws Exception {
        user.setRole(Role.CLIENT);
        when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        String bearer = "Bearer " + jwt.generateToken(user);
        for (var request : List.of(get("/auth/users"), post("/auth/users"), put("/auth/users/7"), delete("/auth/users/7")))
            mvc.perform(request.header("Authorization", bearer)).andExpect(status().isForbidden());
    }
    @Test void compruebaRolActualAunqueElTokenAntiguoDigaAdmin() throws Exception {
        String bearer = "Bearer " + jwt.generateToken(user);
        user.setRole(Role.CLIENT);
        when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        mvc.perform(get("/auth/users").header("Authorization", bearer)).andExpect(status().isForbidden());
    }
    @Test void cuentaEliminadaOTokenFalsoNoDaAcceso() throws Exception {
        mvc.perform(get("/auth/users").header("Authorization", "Bearer " + jwt.generateToken(user))).andExpect(status().isUnauthorized());
        mvc.perform(get("/auth/users").header("Authorization", "Bearer falso")).andExpect(status().isUnauthorized());
    }
    @Test void consultaDeSesionDevuelveElRolActual() throws Exception {
        String bearer = "Bearer " + jwt.generateToken(user);
        user.setRole(Role.CLIENT);
        when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        mvc.perform(get("/auth/session").header("Authorization", bearer))
                .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("CLIENT"));
    }
}
