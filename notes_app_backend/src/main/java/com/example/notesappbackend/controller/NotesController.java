package com.example.notesappbackend.controller;

import com.example.notesappbackend.domain.Note;
import com.example.notesappbackend.domain.User;
import com.example.notesappbackend.dto.NoteDtos;
import com.example.notesappbackend.mapper.NoteMapper;
import com.example.notesappbackend.security.AuthFilter;
import com.example.notesappbackend.service.NoteService;
import com.example.notesappbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Notes endpoints: CRUD operations scoped to the authenticated user.
 */
@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes", description = "CRUD operations for notes")
public class NotesController {

    private final NoteService notes;
    private final UserService users;

    public NotesController(NoteService notes, UserService users) {
        this.notes = notes;
        this.users = users;
    }

    private User requireUser(HttpServletRequest request) {
        Object uid = request.getAttribute(AuthFilter.ATTR_USER_ID);
        if (uid == null) throw new UnauthorizedException("Missing or invalid token");
        UUID userId = (UUID) uid;
        return users.findById(userId).orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    // PUBLIC_INTERFACE
    @GetMapping
    @Operation(summary = "List notes", description = "Returns all notes for the authenticated user.")
    public List<NoteDtos.NoteResponse> list(HttpServletRequest req) {
        User u = requireUser(req);
        return notes.list(u).stream().map(NoteMapper::toResponse).collect(Collectors.toList());
    }

    // PUBLIC_INTERFACE
    @PostMapping
    @Operation(summary = "Create note", description = "Creates a new note for the authenticated user.")
    public ResponseEntity<?> create(@RequestBody NoteDtos.CreateRequest body, HttpServletRequest req) {
        User u = requireUser(req);
        if (body == null || !StringUtils.hasText(body.title)) {
            return ResponseEntity.badRequest().body(new ApiError("Title is required"));
        }
        Note n = notes.create(u, body.title, body.content == null ? "" : body.content);
        return ResponseEntity.ok(NoteMapper.toResponse(n));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(summary = "Get note", description = "Gets a single note by id for the authenticated user.")
    public NoteDtos.NoteResponse get(@PathVariable("id") UUID id, HttpServletRequest req) {
        User u = requireUser(req);
        Note n = notes.getOwned(u, id);
        return NoteMapper.toResponse(n);
    }

    // PUBLIC_INTERFACE
    @PutMapping("/{id}")
    @Operation(summary = "Update note", description = "Updates note title/content/archived fields.")
    public NoteDtos.NoteResponse update(@PathVariable("id") UUID id, @RequestBody NoteDtos.UpdateRequest body, HttpServletRequest req) {
        User u = requireUser(req);
        Note n = notes.getOwned(u, id);
        Note updated = notes.update(n, body == null ? null : body.title, body == null ? null : body.content, body == null ? null : body.archived);
        return NoteMapper.toResponse(updated);
    }

    // PUBLIC_INTERFACE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note", description = "Deletes the note.")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id, HttpServletRequest req) {
        User u = requireUser(req);
        Note n = notes.getOwned(u, id);
        notes.delete(n);
        return ResponseEntity.noContent().build();
    }

    public static class ApiError {
        public final String message;
        public final String color = "#EF4444";
        public ApiError(String message) { this.message = message; }
    }

    @ResponseStatus(code = org.springframework.http.HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String msg) { super(msg); }
    }
}
