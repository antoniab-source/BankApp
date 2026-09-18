package com.example.bankapp.service;

import com.example.bankapp.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> users = new ArrayList<>(List.of(
            new User(1, "antonia", "antonia@example.com"),
            new User(2, "customer2", "customer2@example.com")
    ));

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(Integer userID) {
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
        );
    }

    public User createUser(User user) {
        Integer newUserID = users.stream()
                .map(User::getUserID)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        user.setUserID(newUserID);
        users.add(user);

        return user;
    }

    public User updateUser(Integer userID, User updatedUser) {
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                user.setUsername(updatedUser.getUsername());
                user.setEmail(updatedUser.getEmail());

                return user;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
        );
    }

    public void deleteUser(Integer userID) {
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                users.remove(user);
                return;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
        );
    }
}