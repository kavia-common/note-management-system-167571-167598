package com.example.notesappbackend.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Note entity for user notes with soft delete flag.
 */
@Entity
@Table(name = "notes", indexes = {
        @Index(name = "idx_notes_user", columnList = "user_id")
})
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_notes_user"))
    private User user;

    @Column(nullable = false, length = 150)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(nullable = false)
    private boolean archived = false;

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title == null ? "" : title.trim(); }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content == null ? "" : content; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean isArchived() { return archived; }

    public void setArchived(boolean archived) { this.archived = archived; }
}
