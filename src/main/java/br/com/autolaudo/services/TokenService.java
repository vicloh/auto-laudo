package br.com.autolaudo.services;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {

    // Gera access token JWT válido por 15 minutos
    public String generateAccessToken(String email) {
        return Jwt.issuer("auto-laudo")
                .subject(email)
                .expiresAt(Instant.now().plus(Duration.ofMinutes(15)))
                .sign();
    }

    // Gera refresh token (UUID)
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
}