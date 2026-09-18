package com.example.bankapp.model;

public class User {

    private Integer userID;
    private String username;
    private String email;

    public User() {
    }

    public User(Integer userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}