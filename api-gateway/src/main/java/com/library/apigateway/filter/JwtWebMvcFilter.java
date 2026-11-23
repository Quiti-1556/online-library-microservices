package com.library.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.crypto.SecretKey;

@Component
public class JwtWebMvcFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    private final AuthStateVerifier accounts;

    public JwtWebMvcFilter(AuthStateVerifier accounts) { this.accounts = accounts; }

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {

        String path = request.path();

        // Solo estas rutas exactas son públicas; un prefijo parecido no autoriza acceso.
        if (request.method() == HttpMethod.POST
                && (path.equals("/auth/login") || path.equals("/auth/register"))) {
            return next.handle(request);
        }

        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        Claims claims;
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            // El parser comprueba la firma y el vencimiento antes de confiar en el rol.
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (JwtException | IllegalArgumentException e) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object role = claims.get("role");
        if (claims.getSubject() == null || claims.getSubject().isBlank()
                || claims.getExpiration() == null
                || !("ADMIN".equals(role) || "CLIENT".equals(role))) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean lectura = request.method() == HttpMethod.GET || request.method() == HttpMethod.HEAD;
        try {
            // Además de la firma, confirma que la cuenta exista y conserve sus permisos.
            var identity = accounts.verify(token);
            if (identity == null || !claims.getSubject().equals(identity.email())
                    || !("ADMIN".equals(identity.role()) || "CLIENT".equals(identity.role()))) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
            }
            role = identity.role();
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        } catch (org.springframework.web.client.RestClientException ex) {
            // Ante caída de Auth no concedemos permisos con información antigua.
            return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        // Los perfiles contienen datos personales: también su lectura exige administrador.
        boolean requiereAdmin = pertenece(path, "/users") || pertenece(path, "/auth/users")
                || (!lectura && (pertenece(path, "/books") || pertenece(path, "/inventory")));
        if (requiereAdmin && !"ADMIN".equals(role)) {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }

        // Los errores del microservicio conservan su causa; no se convierten en un falso 401.
        return next.handle(request);
    }

    private boolean pertenece(String path, String recurso) {
        return path.equals(recurso) || path.startsWith(recurso + "/");
    }
}
