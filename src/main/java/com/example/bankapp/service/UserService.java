package com.example.bankapp.service;

import com.example.bankapp.model.User;
import com.example.bankapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer userID) {
        return userRepository.findById(userID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    public User createUser(User user) {
        user.setUserID(null);
        return userRepository.save(user);
    }

    public User updateUser(Integer userID, User updatedUser) {
        User user = getUserById(userID);

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }

    public void deleteUser(Integer userID) {
        User user = getUserById(userID);
        userRepository.delete(user);
    }
}