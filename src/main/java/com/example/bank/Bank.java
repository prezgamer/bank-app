package com.example.bank;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Holds all accounts and handles operations that involve more than one account. */
public class Bank {

    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private int nextNumber = 1001;

    /** Opens a new account. Pass BigDecimal.ZERO for no initial deposit. */
    public Account openAccount(String owner, BigDecimal initialDeposit) {
        Account account = new Account(String.valueOf(nextNumber++), owner);
        if (initialDeposit != null && initialDeposit.signum() > 0) {
            account.deposit(initialDeposit);
        }
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("No such account: " + accountNumber);
        }
        return account;
    }

    public Collection<Account> getAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        if (fromNumber.equals(toNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        Account from = getAccount(fromNumber);
        Account to = getAccount(toNumber);
        // withdraw first: if it fails, nothing has changed on either account
        from.applyTransferOut(amount, toNumber);
        to.applyTransferIn(amount, fromNumber);
    }
}
