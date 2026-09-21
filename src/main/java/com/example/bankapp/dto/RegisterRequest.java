package com.example.bankapp.dto;

import java.math.BigDecimal;

public class RegisterRequest {

    private String username;
    private String email;
    private String password;

    private boolean checking;
    private boolean savings;

    private BigDecimal checkingInitialDeposit;
    private BigDecimal savingsInitialDeposit;

    public RegisterRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isChecking() {
        return checking;
    }

    public void setChecking(boolean checking) {
        this.checking = checking;
    }

    public boolean isSavings() {
        return savings;
    }

    public void setSavings(boolean savings) {
        this.savings = savings;
    }

    public BigDecimal getCheckingInitialDeposit() {
        return checkingInitialDeposit;
    }

    public void setCheckingInitialDeposit(
            BigDecimal checkingInitialDeposit) {
        this.checkingInitialDeposit = checkingInitialDeposit;
    }

    public BigDecimal getSavingsInitialDeposit() {
        return savingsInitialDeposit;
    }

    public void setSavingsInitialDeposit(
            BigDecimal savingsInitialDeposit) {
        this.savingsInitialDeposit = savingsInitialDeposit;
    }
}