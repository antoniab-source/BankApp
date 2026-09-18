package com.example.bankapp.model;

import java.math.BigDecimal;

public class Transfer {

    private Integer fromAccountID;
    private Integer toAccountID;
    private BigDecimal amount;

    public Transfer() {
    }

    public Integer getFromAccountID() {
        return fromAccountID;
    }

    public void setFromAccountID(Integer fromAccountID) {
        this.fromAccountID = fromAccountID;
    }

    public Integer getToAccountID() {
        return toAccountID;
    }

    public void setToAccountID(Integer toAccountID) {
        this.toAccountID = toAccountID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}