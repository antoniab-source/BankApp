package com.example.bankapp.controller;

import com.example.bankapp.dto.ChangePasswordRequest;
import com.example.bankapp.dto.UpdateEmailRequest;
import com.example.bankapp.model.User;
import com.example.bankapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userID}")
    public User getUserById(
            @PathVariable Integer userID) {

        return userService.getUserById(userID);
    }

    @PostMapping
    public User createUser(
            @RequestBody User user) {

        return userService.createUser(user);
    }

    @PutMapping("/{userID}")
    public User updateUser(
            @PathVariable Integer userID,
            @RequestBody User updatedUser) {

        return userService.updateUser(
                userID,
                updatedUser
        );
    }

    @PutMapping("/{userID}/email")
    public User updateEmail(
            @PathVariable Integer userID,
            @RequestBody UpdateEmailRequest request) {

        return userService.updateEmail(
                userID,
                request.getEmail()
        );
    }

    @PutMapping("/{userID}/password")
    public String changePassword(
            @PathVariable Integer userID,
            @RequestBody ChangePasswordRequest request) {

        userService.changePassword(
                userID,
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return "Password changed successfully";
    }

    @DeleteMapping("/{userID}")
    public String deleteUser(
            @PathVariable Integer userID) {

        userService.deleteUser(userID);

        return "User deleted successfully";
    }
}