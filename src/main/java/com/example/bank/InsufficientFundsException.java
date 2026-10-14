package com.example.bank;

import java.math.BigDecimal;

/** Thrown when a withdrawal or transfer would take the balance below zero. */
public class InsufficientFundsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientFundsException(String accountNumber, BigDecimal balance, BigDecimal requested) {
        super("Insufficient funds in account " + accountNumber
                + ": balance " + balance + ", requested " + requested);
    }
}
