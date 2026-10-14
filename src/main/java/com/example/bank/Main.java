package com.example.bank;

import java.math.BigDecimal;
import java.util.Scanner;

/** Simple console menu for the bank app. Run it and follow the prompts. */
public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner in = new Scanner(System.in);

        System.out.println("=== Simple Bank App ===");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> openAccount(bank, in);
                    case "2" -> {
                        Account a = bank.getAccount(ask(in, "Account number: "));
                        a.deposit(askAmount(in));
                        System.out.println("New balance: " + a.getBalance());
                    }
                    case "3" -> {
                        Account a = bank.getAccount(ask(in, "Account number: "));
                        a.withdraw(askAmount(in));
                        System.out.println("New balance: " + a.getBalance());
                    }
                    case "4" -> {
                        String from = ask(in, "From account: ");
                        String to = ask(in, "To account: ");
                        bank.transfer(from, to, askAmount(in));
                        System.out.println("Transfer complete.");
                    }
                    case "5" -> {
                        Account a = bank.getAccount(ask(in, "Account number: "));
                        System.out.println(a.getOwner() + " balance: " + a.getBalance());
                    }
                    case "6" -> {
                        Account a = bank.getAccount(ask(in, "Account number: "));
                        System.out.println("--- Statement for " + a.getAccountNumber() + " ---");
                        a.getHistory().forEach(System.out::println);
                    }
                    case "7" -> bank.getAccounts().forEach(a ->
                            System.out.println(a.getAccountNumber() + "  " + a.getOwner() + "  " + a.getBalance()));
                    case "0" -> running = false;
                    default -> System.out.println("Unknown option.");
                }
            } catch (IllegalArgumentException | InsufficientFundsException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1) Open account   2) Deposit   3) Withdraw   4) Transfer");
        System.out.println("5) Balance        6) Statement 7) List accounts   0) Exit");
        System.out.print("> ");
    }

    private static void openAccount(Bank bank, Scanner in) {
        String owner = ask(in, "Owner name: ");
        BigDecimal initial = askAmount(in, "Initial deposit (0 for none): ", true);
        Account account = bank.openAccount(owner, initial);
        System.out.println("Opened account " + account.getAccountNumber() + " for " + owner);
    }

    private static String ask(Scanner in, String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static BigDecimal askAmount(Scanner in) {
        return askAmount(in, "Amount: ", false);
    }

    private static BigDecimal askAmount(Scanner in, String prompt, boolean allowZero) {
        String text = ask(in, prompt);
        try {
            BigDecimal value = new BigDecimal(text);
            if (allowZero && value.signum() == 0) {
                return BigDecimal.ZERO;
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + text + "' is not a valid number");
        }
    }
}
