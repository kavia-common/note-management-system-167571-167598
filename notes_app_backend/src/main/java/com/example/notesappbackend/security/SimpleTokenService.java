package com.example.notesappbackend.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory token service for demo purposes.
 * Not for production use.
 */
@Component
public class SimpleTokenService {

    private final Map<String, UUID> tokenToUser = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    // PUBLIC_INTERFACE
    public String issueToken(UUID userId) {
        /** Issues a new random token for the given user id. */
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokenToUser.put(token, userId);
        return token;
    }

    // PUBLIC_INTERFACE
    public UUID validate(String token) {
        /** Returns the userId if token is valid; otherwise null. */
        return tokenToUser.get(token);
    }

    // PUBLIC_INTERFACE
    public void revoke(String token) {
        /** Revokes a token. */
        tokenToUser.remove(token);
    }
}
