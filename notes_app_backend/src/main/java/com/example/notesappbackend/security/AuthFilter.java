package com.example.notesappbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Simple bearer token filter extracting userId from token and adding as request attribute.
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    public static final String ATTR_USER_ID = "AUTH_USER_ID";

    private final SimpleTokenService tokenService;

    public AuthFilter(SimpleTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // allow open endpoints
        return path.startsWith("/api/auth/") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api-docs") ||
               path.equals("/") ||
               path.equals("/docs") ||
               path.equals("/health") ||
               path.startsWith("/actuator") ||
               path.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7).trim();
            UUID userId = tokenService.validate(token);
            if (userId != null) {
                request.setAttribute(ATTR_USER_ID, userId);
            }
        }
        filterChain.doFilter(request, response);
    }
}
