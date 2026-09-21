package com.example.bankapp.model;

import java.math.BigDecimal;

public class Deposit {

    private BigDecimal amount;

    public Deposit() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}