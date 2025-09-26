package br.com.autolaudo.services;

import org.mindrot.jbcrypt.BCrypt;

import br.com.autolaudo.enums.UserRole;
import br.com.autolaudo.models.User;
import br.com.autolaudo.repository.AuthRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    AuthRepository authRepository;

    // Busca usuário por email
    public User findByEmail(String email) {
        return authRepository.findByEmail(email).orElse(null);
    }

    // Verifica senha usando BCrypt
    public boolean checkPassword(String plain, String hash) {
        return BCrypt.checkpw(plain, hash);
    }

    // Atualiza refresh token do usuário
    public void updateRefreshToken(User user, String refreshToken) {
        user.refreshToken = refreshToken;
        user.update();
    }

    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    public boolean crqExists(String crq) {
        return authRepository.findByCrq(crq).orElse(null) != null;
    }

    // Cria e persiste novo usuário com senha hash
    public void registerUserAdm(String email, String plainPassword) {
        String passwordHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRefreshToken(null);
        user.setRole(UserRole.ADM);
        authRepository.persist(user);
    }

    public void registerUserQuimico(String email, String plainPassword, String crq) {
        String passwordHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRefreshToken(null);
        user.setRole(UserRole.QUIMICO);
        authRepository.persist(user);
    }

    public User findByRefreshToken(String refreshToken) {
    return authRepository.findByRefreshToken(refreshToken).orElse(null);
}
}