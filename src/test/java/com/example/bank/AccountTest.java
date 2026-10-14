package com.example.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("1001", "Alice");
    }

    @Test
    void newAccountStartsAtZero() {
        assertEquals(new BigDecimal("0.00"), account.getBalance());
    }

    @Test
    void depositIncreasesBalance() {
        account.deposit(new BigDecimal("100.50"));
        assertEquals(new BigDecimal("100.50"), account.getBalance());
    }

    @Test
    void withdrawDecreasesBalance() {
        account.deposit(new BigDecimal("100"));
        account.withdraw(new BigDecimal("40.25"));
        assertEquals(new BigDecimal("59.75"), account.getBalance());
    }

    @Test
    void withdrawMoreThanBalanceFails() {
        account.deposit(new BigDecimal("50"));
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(new BigDecimal("50.01")));
        assertEquals(new BigDecimal("50.00"), account.getBalance());
    }

    @Test
    void rejectsZeroAndNegativeAmounts() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("-5")));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("-1")));
    }

    @Test
    void historyRecordsEachTransaction() {
        account.deposit(new BigDecimal("100"));
        account.withdraw(new BigDecimal("30"));
        assertEquals(2, account.getHistory().size());
        assertEquals("DEPOSIT", account.getHistory().get(0).type());
        assertEquals("WITHDRAW", account.getHistory().get(1).type());
    }

    @Test
    void requiresOwnerAndNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Account("", "Bob"));
        assertThrows(IllegalArgumentException.class, () -> new Account("1002", " "));
    }
}
