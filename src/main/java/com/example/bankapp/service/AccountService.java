package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private List<Account> accounts = List.of(
            new Account(1, "123456789", new java.math.BigDecimal("1000.00"), "Checking"),
            new Account(2, "987654321", new java.math.BigDecimal("500.00"), "Savings"),
            new Account(3, "456789123", new java.math.BigDecimal("2000.00"), "Checking"),
            new Account(4, "789123456", new java.math.BigDecimal("1500.00"), "Savings")
    );

    public List<Account> getAllAccounts() {
        return accounts;
    }
}
