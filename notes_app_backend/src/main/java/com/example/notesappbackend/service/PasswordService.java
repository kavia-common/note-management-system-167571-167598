package com.example.notesappbackend.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Simple salted SHA-256 password hashing for demo. Do not use in production.
 */
@Service
public class PasswordService {

    private final SecureRandom random = new SecureRandom();

    // PUBLIC_INTERFACE
    public String hash(String password) {
        /** Creates a salted hash "salt:hash" using SHA-256 and Base64. */
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltB64 = Base64.getEncoder().encodeToString(salt);
        String digest = digest(salt, password);
        return saltB64 + ":" + digest;
    }

    // PUBLIC_INTERFACE
    public boolean verify(String password, String stored) {
        /** Verifies a password against a stored "salt:hash". */
        if (stored == null || !stored.contains(":")) return false;
        String[] parts = stored.split(":", 2);
        byte[] salt = Base64.getEncoder().decode(parts[0]);
        String expected = parts[1];
        String actual = digest(salt, password);
        return constantTimeEquals(expected, actual);
    }

    private String digest(byte[] salt, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] out = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash password", e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r |= a.charAt(i) ^ b.charAt(i);
        }
        return r == 0;
    }
}
