package com.example.notesappbackend.repository;

import com.example.notesappbackend.domain.Note;
import com.example.notesappbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Note entity.
 */
public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findAllByUserOrderByUpdatedAtDesc(User user);
    Optional<Note> findByIdAndUser(UUID id, User user);
}
