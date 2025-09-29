package com.example.notesappbackend.service;

import com.example.notesappbackend.domain.User;
import com.example.notesappbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * User management service.
 */
@Service
public class UserService {

    private final UserRepository users;
    private final PasswordService passwords;

    public UserService(UserRepository users, PasswordService passwords) {
        this.users = users;
        this.passwords = passwords;
    }

    @Transactional
    public User register(String username, String email, String password) {
        Optional<User> byUsername = users.findByUsernameIgnoreCase(username);
        if (byUsername.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        Optional<User> byEmail = users.findByEmailIgnoreCase(email);
        if (byEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(passwords.hash(password));
        return users.save(u);
    }

    public Optional<User> findById(UUID id) {
        return users.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return users.findByUsernameIgnoreCase(username);
    }

    public boolean verifyPassword(User user, String password) {
        return passwords.verify(password, user.getPasswordHash());
    }
}
