package com.library.apigateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JwtWebMvcFilterTest {
    // Clave exclusiva de pruebas; los casos firman JWT reales, sin simular el parser.
    private static final byte[] KEY = "clave-de-pruebas-exclusiva-de-32-bytes".getBytes(StandardCharsets.UTF_8);
    private JwtWebMvcFilter filter;
    private AuthStateVerifier accounts;

    @BeforeEach void setup() {
        accounts = org.mockito.Mockito.mock(AuthStateVerifier.class);
        org.mockito.Mockito.lenient().when(accounts.verify(org.mockito.ArgumentMatchers.anyString())).thenAnswer(call -> {
            var claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(KEY)).parseClaimsJws(call.getArgument(0)).getBody();
            return new AuthStateVerifier.Identity(claims.getSubject(), claims.get("role", String.class));
        });
        filter = new JwtWebMvcFilter(accounts);
        ReflectionTestUtils.setField(filter, "secretKey", Base64.getEncoder().encodeToString(KEY));
    }

    private String token(String role, long remaining) {
        return Jwts.builder().setSubject("prueba@example.com").claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + remaining))
                .signWith(Keys.hmacShaKeyFor(KEY)).compact();
    }

    private ServerRequest request(String method, String path, String jwt) {
        MockHttpServletRequest servlet = new MockHttpServletRequest(method, path);
        if (jwt != null) servlet.addHeader("Authorization", "Bearer " + jwt);
        return ServerRequest.create(servlet, List.of());
    }

    @ParameterizedTest
    @CsvSource({"GET,/users", "GET,/users/1", "POST,/users", "DELETE,/users/1",
            "DELETE,/auth/users/1", "POST,/books", "PUT,/books/1", "PATCH,/books/1",
            "DELETE,/books/1", "POST,/inventory"})
    void clienteNoPuedeAdministrar(String method, String path) throws Exception {
        assertEquals(403, filter.filter(request(method, path, token("CLIENT", 60000)),
                req -> { fail("No debe alcanzar el microservicio"); return null; }).statusCode().value());
        assertEquals(204, filter.filter(request(method, path, token("ADMIN", 60000)),
                req -> ServerResponse.noContent().build()).statusCode().value());
    }

    @Test void clientePuedeConsultarLibros() throws Exception {
        assertEquals(200, filter.filter(request("GET", "/books", token("CLIENT", 60000)),
                req -> ServerResponse.ok().build()).statusCode().value());
    }

    @Test void rechazaTokensAusentesInvalidosVencidosYRolesDesconocidos() throws Exception {
        for (String jwt : new String[]{null, "invalido", token("ADMIN", -1000), token("SELLER", 60000)}) {
            assertEquals(401, filter.filter(request("GET", "/users", jwt),
                    req -> { fail("No debe alcanzar el microservicio"); return null; }).statusCode().value());
        }
    }

    @Test void soloLoginYRegistroExactosSonPublicos() throws Exception {
        assertEquals(200, filter.filter(request("POST", "/auth/login", null),
                req -> ServerResponse.ok().build()).statusCode().value());
        assertEquals(200, filter.filter(request("POST", "/auth/register", null),
                req -> ServerResponse.ok().build()).statusCode().value());
        assertEquals(401, filter.filter(request("POST", "/auth/login/otro", null),
                req -> ServerResponse.ok().build()).statusCode().value());
    }

    @Test void noDisfrazaUnFalloDelServicioComoErrorDeSesion() {
        assertThrows(IllegalStateException.class, () -> filter.filter(
                request("GET", "/users", token("ADMIN", 60000)),
                req -> { throw new IllegalStateException("Fallo del servicio"); }));
    }
    @Test void rolRevocadoNoPuedeModificarLibrosAunqueElJwtDigaAdmin() throws Exception {
        org.mockito.Mockito.doReturn(new AuthStateVerifier.Identity("prueba@example.com", "CLIENT"))
                .when(accounts).verify(org.mockito.ArgumentMatchers.anyString());
        assertEquals(403, filter.filter(request("PUT", "/books/7", token("ADMIN", 60000)),
                req -> { fail("Permiso revocado"); return null; }).statusCode().value());
    }
    @Test void authCaidoNoAutorizaConRolAntiguo() throws Exception {
        org.mockito.Mockito.doThrow(new org.springframework.web.client.ResourceAccessException("offline"))
                .when(accounts).verify(org.mockito.ArgumentMatchers.anyString());
        assertEquals(503, filter.filter(request("GET", "/books", token("ADMIN", 60000)),
                req -> { fail("Debe fallar cerrado"); return null; }).statusCode().value());
    }
}
