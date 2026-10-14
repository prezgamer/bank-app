package com.example.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankTest {

    private Bank bank;
    private Account alice;
    private Account bob;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        alice = bank.openAccount("Alice", new BigDecimal("500"));
        bob = bank.openAccount("Bob", BigDecimal.ZERO);
    }

    @Test
    void openAccountAssignsUniqueNumbers() {
        assertNotEquals(alice.getAccountNumber(), bob.getAccountNumber());
        assertEquals(2, bank.getAccounts().size());
    }

    @Test
    void openAccountWithInitialDeposit() {
        assertEquals(new BigDecimal("500.00"), alice.getBalance());
        assertEquals(new BigDecimal("0.00"), bob.getBalance());
    }

    @Test
    void transferMovesMoneyBetweenAccounts() {
        bank.transfer(alice.getAccountNumber(), bob.getAccountNumber(), new BigDecimal("125.75"));
        assertEquals(new BigDecimal("374.25"), alice.getBalance());
        assertEquals(new BigDecimal("125.75"), bob.getBalance());
    }

    @Test
    void failedTransferChangesNothing() {
        assertThrows(InsufficientFundsException.class,
                () -> bank.transfer(alice.getAccountNumber(), bob.getAccountNumber(), new BigDecimal("1000")));
        assertEquals(new BigDecimal("500.00"), alice.getBalance());
        assertEquals(new BigDecimal("0.00"), bob.getBalance());
    }

    @Test
    void cannotTransferToSameAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> bank.transfer(alice.getAccountNumber(), alice.getAccountNumber(), BigDecimal.TEN));
    }

    @Test
    void unknownAccountIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> bank.getAccount("9999"));
        assertThrows(IllegalArgumentException.class,
                () -> bank.transfer(alice.getAccountNumber(), "9999", BigDecimal.TEN));
    }
}
