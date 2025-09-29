package com.example.notesappbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * DTOs for user operations.
 */
public class UserDtos {

    public static class RegisterRequest {
        @Schema(description = "Unique username", example = "ocean_user")
        public String username;
        @Schema(description = "Email address", example = "ocean@example.com")
        public String email;
        @Schema(description = "Plain text password (will be hashed)", example = "StrongPass123!")
        public String password;
    }

    public static class LoginRequest {
        @Schema(description = "Username", example = "ocean_user")
        public String username;
        @Schema(description = "Plain text password", example = "StrongPass123!")
        public String password;
    }

    public static class AuthResponse {
        @Schema(description = "User id")
        public UUID userId;
        @Schema(description = "Username")
        public String username;
        @Schema(description = "Email")
        public String email;
        @Schema(description = "Simple bearer token for demo")
        public String token;
    }

    public static class UserResponse {
        public UUID id;
        public String username;
        public String email;
    }
}
