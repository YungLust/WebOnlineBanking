package com.example.WebOnlineBanking.service;

import com.example.WebOnlineBanking.model.User;
import com.example.WebOnlineBanking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {
    }

    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username.toLowerCase());
    }

    public User register(String username, String password) {
        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            String normalizedUsername = username.toLowerCase();

            if (this.userRepository.findByUsername(normalizedUsername).isPresent()) {
                throw new IllegalArgumentException("Username already exists");
            }
            else {
                User user = new User();
                user.setUsername(normalizedUsername);
                user.setPassword(password);
                return this.userRepository.save(user);
            }
        }
        else {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }
    }

    public User authenticate(String username, String password) {
        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            String normalizedUsername = username.toLowerCase();
            return this.userRepository.findByUsername(normalizedUsername).filter((user) ->
                    user.getPassword().equals(password))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        }
        else {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }
    }
}
