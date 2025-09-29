package com.example.notesappbackend.mapper;

import com.example.notesappbackend.domain.Note;
import com.example.notesappbackend.dto.NoteDtos;

/**
 * Mapper for Note and Note DTOs.
 */
public final class NoteMapper {
    private NoteMapper() {}

    // PUBLIC_INTERFACE
    public static NoteDtos.NoteResponse toResponse(Note note) {
        /** Convert Note entity to NoteResponse DTO. */
        NoteDtos.NoteResponse r = new NoteDtos.NoteResponse();
        r.id = note.getId();
        r.title = note.getTitle();
        r.content = note.getContent();
        r.archived = note.isArchived();
        r.createdAt = note.getCreatedAt();
        r.updatedAt = note.getUpdatedAt();
        return r;
    }
}
