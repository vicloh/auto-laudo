package br.com.autolaudo.repository;

import java.util.Optional;

import br.com.autolaudo.models.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthRepository {

    // Busca usuário por email
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(User.find("email", email).firstResult());
    }

    // Busca usuário por refresh token
    public Optional<User> findByRefreshToken(String refreshToken) {
        return Optional.ofNullable(User.find("refreshToken", refreshToken).firstResult());
    }

    // Persiste novo usuário
    public void persist(User user) {
        user.persist();
    }

    // Atualiza usuário
    public void update(User user) {
        user.update();
    }

    public Optional<User> findByCrq(String crq) {
        return Optional.ofNullable(User.find("crq", crq).firstResult());
    }
}