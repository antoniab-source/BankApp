package com.example.bankapp.dto;

public class RegisterResponse {

    private Integer userID;
    private String username;
    private String email;
    private String role;

    public RegisterResponse() {
    }

    public RegisterResponse(
            Integer userID,
            String username,
            String email,
            String role) {

        this.userID = userID;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}