package com.example.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A single bank account. Money is stored as BigDecimal (never double) with 2 decimal places. */
public class Account {

    /** One line in the account statement. */
    public record Transaction(String type, BigDecimal amount, BigDecimal balanceAfter) {
        @Override
        public String toString() {
            return String.format("%-12s %10s   balance: %10s", type, amount, balanceAfter);
        }
    }

    private final String accountNumber;
    private final String owner;
    private BigDecimal balance = BigDecimal.ZERO.setScale(2);
    private final List<Transaction> history = new ArrayList<>();

    public Account(String accountNumber, String owner) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner name is required");
        }
        this.accountNumber = accountNumber;
        this.owner = owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void deposit(BigDecimal amount) {
        BigDecimal value = validate(amount);
        balance = balance.add(value);
        history.add(new Transaction("DEPOSIT", value, balance));
    }

    public void withdraw(BigDecimal amount) {
        BigDecimal value = validate(amount);
        if (value.compareTo(balance) > 0) {
            throw new InsufficientFundsException(accountNumber, balance, value);
        }
        balance = balance.subtract(value);
        history.add(new Transaction("WITHDRAW", value, balance));
    }

    /** Used by Bank.transfer so the statement shows transfers rather than plain deposits/withdrawals. */
    void applyTransferOut(BigDecimal amount, String toAccount) {
        BigDecimal value = validate(amount);
        if (value.compareTo(balance) > 0) {
            throw new InsufficientFundsException(accountNumber, balance, value);
        }
        balance = balance.subtract(value);
        history.add(new Transaction("TRANSFER OUT", value, balance));
    }

    void applyTransferIn(BigDecimal amount, String fromAccount) {
        BigDecimal value = validate(amount);
        balance = balance.add(value);
        history.add(new Transaction("TRANSFER IN", value, balance));
    }

    private static BigDecimal validate(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        BigDecimal value = amount.setScale(2, RoundingMode.HALF_UP);
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        return value;
    }
}
