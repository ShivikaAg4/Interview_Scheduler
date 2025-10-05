package com.example.scheduler.service;

import com.example.scheduler.model.User;
import com.example.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // READ ALL
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ ONE
    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    // UPDATE
    public User updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            updatedUser.setId(id); // Ensure the same ID is preserved
            return userRepository.save(updatedUser);
        }).orElse(null);
    }

    // DELETE
    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
