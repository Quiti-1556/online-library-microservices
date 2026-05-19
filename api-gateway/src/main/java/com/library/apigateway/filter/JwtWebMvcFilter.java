package com.library.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.crypto.SecretKey;

@Component
public class JwtWebMvcFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Override
    public ServerResponse filter(ServerRequest request,
                                 HandlerFunction<ServerResponse> next) throws Exception {


        System.out.println("PATH: " + request.path());

        String path = request.path();
        System.out.println("PATH DETECTADO: " + path);

        if (path.equals("/auth/login") || path.equals("/auth/register")) {

            System.out.println("RUTA PUBLICA");

            return next.handle(request);
        }

        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        try {

            byte[] keyBytes = Decoders.BASE64.decode(secretKey);

            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            ServerRequest modifiedRequest = ServerRequest.from(request)
                    .header("X-User-Name", username)
                    .build();

            return next.handle(modifiedRequest);

        } catch (Exception e) {

            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
