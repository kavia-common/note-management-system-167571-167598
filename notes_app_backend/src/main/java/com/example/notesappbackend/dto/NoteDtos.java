package com.example.notesappbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * DTOs for note operations.
 */
public class NoteDtos {

    public static class CreateRequest {
        @Schema(description = "Note title", example = "Meeting Notes")
        public String title;
        @Schema(description = "Note content", example = "Discuss project milestones...")
        public String content;
    }

    public static class UpdateRequest {
        @Schema(description = "Note title", example = "Meeting Notes (Updated)")
        public String title;
        @Schema(description = "Note content", example = "Updated content...")
        public String content;
        @Schema(description = "Archived flag", example = "false")
        public Boolean archived;
    }

    public static class NoteResponse {
        public UUID id;
        public String title;
        public String content;
        public boolean archived;
        public Instant createdAt;
        public Instant updatedAt;
    }
}
