package com.example.notesappbackend.service;

import com.example.notesappbackend.domain.Note;
import com.example.notesappbackend.domain.User;
import com.example.notesappbackend.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Service for note operations.
 */
@Service
public class NoteService {

    private final NoteRepository notes;

    public NoteService(NoteRepository notes) {
        this.notes = notes;
    }

    public List<Note> list(User user) {
        return notes.findAllByUserOrderByUpdatedAtDesc(user);
    }

    @Transactional
    public Note create(User user, String title, String content) {
        Note n = new Note();
        n.setUser(user);
        n.setTitle(title == null ? "" : title.trim());
        n.setContent(content == null ? "" : content);
        Instant now = Instant.now();
        n.setCreatedAt(now);
        n.setUpdatedAt(now);
        return notes.save(n);
    }

    public Note getOwned(User user, UUID noteId) {
        return notes.findByIdAndUser(noteId, user).orElseThrow(() -> new IllegalArgumentException("Note not found"));
    }

    @Transactional
    public Note update(Note note, String title, String content, Boolean archived) {
        if (title != null) note.setTitle(title.trim());
        if (content != null) note.setContent(content);
        if (archived != null) note.setArchived(archived);
        note.setUpdatedAt(Instant.now());
        return notes.save(note);
    }

    @Transactional
    public void delete(Note note) {
        notes.delete(note);
    }
}
