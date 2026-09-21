package com.example.bankapp.model;

import java.math.BigDecimal;

public class Withdrawal {

    private BigDecimal amount;

    public Withdrawal() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}