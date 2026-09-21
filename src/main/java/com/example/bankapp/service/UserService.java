package com.example.bankapp.service;

import com.example.bankapp.model.User;
import com.example.bankapp.repository.UserRepository;
import com.example.bankapp.security.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthorizationService authorizationService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    public List<User> getAllUsers() {
        authorizationService.requireAdmin();
        return userRepository.findAll();
    }

    public User getUserById(Integer userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        authorizationService.requireUserAccess(user);

        return user;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    public User createUser(User user) {
        authorizationService.requireAdmin();

        user.setUserID(null);
        user.setRole("CUSTOMER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(Integer userID, User updatedUser) {
        authorizationService.requireAdmin();

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null
                && !updatedUser.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(updatedUser.getPassword())
            );
        }

        return userRepository.save(user);
    }

    public void deleteUser(Integer userID) {
        authorizationService.requireAdmin();

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        userRepository.delete(user);
    }
}