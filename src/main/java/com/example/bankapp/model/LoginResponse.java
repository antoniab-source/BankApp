package com.example.bankapp.model;

public class LoginResponse {

    private String token;
    private Integer userID;
    private String username;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            String token,
            Integer userID,
            String username,
            String role) {

        this.token = token;
        this.userID = userID;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}