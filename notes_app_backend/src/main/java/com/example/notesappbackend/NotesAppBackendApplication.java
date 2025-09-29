package com.example.notesappbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the Notes Application backend.
 * Provides REST API for user management and note CRUD operations.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Notes API - Ocean Professional",
                version = "1.0.0",
                description = """
                        Minimalist Notes backend with a modern, clean aesthetic.
                        Colors:
                        - Primary: #2563EB
                        - Secondary: #F59E0B
                        - Error: #EF4444
                        - Background: #f9fafb
                        - Surface: #ffffff
                        - Text: #111827
                        """,
                contact = @Contact(name = "Notes API", email = "support@example.com")
        ),
        servers = {
                @Server(url = "/", description = "Default Server")
        }
)
public class NotesAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesAppBackendApplication.class, args);
    }
}
