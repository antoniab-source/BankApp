package com.example.bankapp.model;
import java.math.BigDecimal;

public class Account {

    private int accountID;
    private String accountNumber;
    private BigDecimal balance;
    private String accountType;

    public Account(int accountID, String accountNumber, BigDecimal balance, String accountType) {
        this.accountID = accountID;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }
        public int getAccountID() {
            return accountID;
        }
        public String getAccountNumber() {
            return accountNumber;
        }
        public BigDecimal getBalance() {
            return balance;
        }
        public String getAccountType() {
            return accountType;
        }
         public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
        public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
        public void setAccountType(String accountType) {
            this.accountType = accountType;
    }
}