package com.example.bankapp.controller;

import com.example.bankapp.dto.LoginRequest;
import com.example.bankapp.dto.LoginResponse;
import com.example.bankapp.dto.RegisterRequest;
import com.example.bankapp.dto.RegisterResponse;
import com.example.bankapp.model.User;
import com.example.bankapp.service.AccountService;
import com.example.bankapp.service.AuthService;
import com.example.bankapp.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AccountService accountService;

    public AuthController(
            AuthService authService,
            UserService userService,
            AccountService accountService) {

        this.authService = authService;
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest loginRequest) {

        return authService.login(loginRequest);
    }

    @Transactional
    @PostMapping("/register")
    public RegisterResponse register(
            @RequestBody RegisterRequest registerRequest) {

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());

        User savedUser = userService.registerUser(user);

        if (!registerRequest.isChecking()
                && !registerRequest.isSavings()) {

            throw new IllegalArgumentException(
                    "Please select at least one account type"
            );
        }

        if (registerRequest.isChecking()) {

            accountService.createAccountForRegistration(
                    savedUser,
                    "Checking",
                    registerRequest.getCheckingInitialDeposit()
            );
        }

        if (registerRequest.isSavings()) {

            accountService.createAccountForRegistration(
                    savedUser,
                    "Savings",
                    registerRequest.getSavingsInitialDeposit()
            );
        }

        return new RegisterResponse(
                savedUser.getUserID(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}