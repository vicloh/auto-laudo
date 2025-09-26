package br.com.autolaudo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {
    // Hashes a plain text password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Verifies if the provided plain text password matches the hashed password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedInputPassword = hashPassword(plainPassword);
        return hashedInputPassword.equals(hashedPassword);
    }
}
