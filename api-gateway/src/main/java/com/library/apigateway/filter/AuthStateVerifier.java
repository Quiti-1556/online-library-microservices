package com.library.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthStateVerifier {
    public record Identity(String email, String role) {}
    private final RestTemplate client;
    @Value("${services.auth-url:http://auth-service:8080}")
    private String authUrl;

    public AuthStateVerifier() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); factory.setReadTimeout(5000);
        client = new RestTemplate(factory);
    }
    public Identity verify(String token) {
        // No cacheamos roles: eliminar una cuenta o quitar ADMIN debe tener efecto inmediato.
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return client.exchange(authUrl + "/auth/session", HttpMethod.GET,
                new HttpEntity<>(headers), Identity.class).getBody();
    }
}
