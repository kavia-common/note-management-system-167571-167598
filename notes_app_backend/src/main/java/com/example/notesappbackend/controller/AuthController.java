package com.example.notesappbackend.controller;

import com.example.notesappbackend.domain.User;
import com.example.notesappbackend.dto.UserDtos;
import com.example.notesappbackend.security.SimpleTokenService;
import com.example.notesappbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Authentication endpoints: register and login.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "User registration and authentication")
public class AuthController {

    private final UserService users;
    private final SimpleTokenService tokens;

    public AuthController(UserService users, SimpleTokenService tokens) {
        this.users = users;
        this.tokens = tokens;
    }

    // PUBLIC_INTERFACE
    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Create a user with username, email, and password."
    )
    public ResponseEntity<?> register(@RequestBody UserDtos.RegisterRequest req) {
        if (req == null || !StringUtils.hasText(req.username) || !StringUtils.hasText(req.email) || !StringUtils.hasText(req.password)) {
            return ResponseEntity.badRequest().body(error("Invalid input"));
        }
        try {
            User u = users.register(req.username.trim(), req.email.trim(), req.password);
            UserDtos.UserResponse r = new UserDtos.UserResponse();
            r.id = u.getId();
            r.username = u.getUsername();
            r.email = u.getEmail();
            return ResponseEntity.status(HttpStatus.CREATED).body(r);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error(ex.getMessage()));
        }
    }

    // PUBLIC_INTERFACE
    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticate with username and password. Returns a bearer token."
    )
    public ResponseEntity<?> login(@RequestBody UserDtos.LoginRequest req) {
        if (req == null || !StringUtils.hasText(req.username) || !StringUtils.hasText(req.password)) {
            return ResponseEntity.badRequest().body(error("Invalid credentials"));
        }
        return users.findByUsername(req.username.trim())
                .filter(u -> users.verifyPassword(u, req.password))
                .map(u -> {
                    String token = tokens.issueToken(u.getId());
                    UserDtos.AuthResponse r = new UserDtos.AuthResponse();
                    r.userId = u.getId();
                    r.username = u.getUsername();
                    r.email = u.getEmail();
                    r.token = token;
                    return ResponseEntity.ok(r);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error("Invalid credentials")));
    }

    // PUBLIC_INTERFACE
    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "Revokes the provided bearer token."
    )
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            tokens.revoke(token);
        }
        return ResponseEntity.noContent().build();
    }

    private static ErrorMessage error(String message) {
        return new ErrorMessage(message);
    }

    public static class ErrorMessage {
        public final String message;
        public final String themePrimary = "#2563EB";
        public final String themeError = "#EF4444";
        public final String themeText = "#111827";

        public ErrorMessage(String message) {
            this.message = message;
        }
    }
}
