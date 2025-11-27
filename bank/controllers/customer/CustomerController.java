package bank.controllers.customer;

import bank.GUI.Customer.CustomerCardPanel;
import bank.controllers.auth.LoginController;
import bank.database.Database;
import bank.database.Database.PrimaryAccount;
import bank.models.org.Bank;
import bank.models.users.Customer;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

public class CustomerController {
    private final Database db;
    private final JPanel container;
    private final CardLayout cardLayout;
    private final String cardName;
    private final CustomerCardPanel view;
    private final Runnable onLogout;
    private final Bank bank;
    private final String username;
    private Integer primaryCustomerId;
    private PrimaryAccount checkAccount;
    private PrimaryAccount savingAccount;
    private bank.GUI.Customer.CustomerTransactions checkTxPanel;
    private bank.GUI.Customer.CustomerTransactions savingTxPanel;
    private String checkBalanceDisplay = "(none)";
    private String savingBalanceDisplay = "(none)";

    public CustomerController(Bank bank, JPanel container, CardLayout cardLayout, String username, Runnable onLogout) {
        this.bank = bank;
        this.container = container;
        this.cardLayout = cardLayout;
        this.onLogout = onLogout;
        this.username = username;

        this.db = Database.getInstance();
        db.getConnection();

        Customer customer = new Customer(username);

        this.cardName = "CUSTOMER_HOME_" + username;
        this.view = new CustomerCardPanel(customer);

        container.add(view, cardName);
        attachEventHandlers();
        loadCustomerContext();
    }

    private void attachEventHandlers() {
        view.getCustomerHome().getLogoutButton().addActionListener(e -> {
            cardLayout.show(container, LoginController.CARD_LOGIN_REGISTER);
            container.revalidate();
            container.repaint();
            if (onLogout != null) {
                onLogout.run();
            }
        });

        view.getCustomerHome().getMakeTransactionButton().addActionListener(e -> {
            populateTransactionAccounts();
            view.showCard("Transaction");
            cardLayout.show(container, cardName);
        });

        view.getCustomerHome().getMakeTransferButton().addActionListener(e -> {
            populateTransferOptions();
            view.showCard("Transfer");
            cardLayout.show(container, cardName);
        });

        view.getTransactionPanel().getMakeTransactionButton().addActionListener(e -> handleTransaction());
        view.getTransactionPanel().getBackButton().addActionListener(e -> {
            cardLayout.show(container, cardName);
            view.showCard("Home");
        });

        view.getTransferPanel().getTransferButton().addActionListener(e -> handleTransfer());
        view.getTransferPanel().getBackButton().addActionListener(e -> {
            cardLayout.show(container, cardName);
            view.showCard("Home");
        });
        view.getTransferPanel().getSelectCheckRadio().addActionListener(e -> updateTransferDirectionLabels());
        view.getTransferPanel().getSelectSavingRadio().addActionListener(e -> updateTransferDirectionLabels());

        view.getCustomerHome().getCheckPanel().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                showAccountTransactions(checkAccount, "Check Transactions", "Check_Transactions");
            }
        });
        view.getCustomerHome().getSavingPanel().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                showAccountTransactions(savingAccount, "Saving Transactions", "Saving_Transactions");
            }
        });
    }

    public void showDashboard() {
        cardLayout.show(container, cardName);
    }

    public void dispose() {
        container.remove(view);
        container.revalidate();
        container.repaint();
    }

    private void loadCustomerContext() {
        primaryCustomerId = db.getPrimaryCustomerIdByUsername(username);
        populateTransactionAccounts();
        populateAccountSummaries();
        populateTransferOptions();
    }

    private void populateTransactionAccounts() {
        if (primaryCustomerId == null) {
            return;
        }
        List<PrimaryAccount> accounts = db.getPrimaryAccountsByCustomerId(primaryCustomerId);
        List<bank.GUI.Customer.CustomerMakeTransaction.AccountOption> options = accounts.stream()
                .map(a -> new bank.GUI.Customer.CustomerMakeTransaction.AccountOption(
                        a.accountId,
                        maskAccount(a.accountNumber) + " (" + a.accountType + ") - $" + String.format("%.2f", a.balance)
                ))
                .collect(Collectors.toList());
        view.getTransactionPanel().setAccounts(options);
    }

    private void populateTransferOptions() {
        if (primaryCustomerId == null) {
            return;
        }
        view.getTransferPanel().setAvailability(checkAccount != null, savingAccount != null);
        updateTransferDirectionLabels();
    }

    private void updateTransferDirectionLabels() {
        boolean fromCheck = view.getTransferPanel().isFromCheck();
        String fromMasked = "(none)";
        String toMasked = "(none)";
        if (fromCheck) {
            fromMasked = checkAccount != null ? maskAccount(checkAccount.accountNumber) : "(none)";
            toMasked = savingAccount != null ? maskAccount(savingAccount.accountNumber) : "(none)";
            view.getTransferPanel().setBalanceLabels(
                    checkAccount != null ? "$" + String.format("%.2f", checkAccount.balance) : "(none)",
                    savingAccount != null ? "$" + String.format("%.2f", savingAccount.balance) : "(none)");
        } else {
            fromMasked = savingAccount != null ? maskAccount(savingAccount.accountNumber) : "(none)";
            toMasked = checkAccount != null ? maskAccount(checkAccount.accountNumber) : "(none)";
            view.getTransferPanel().setBalanceLabels(
                    savingAccount != null ? "$" + String.format("%.2f", savingAccount.balance) : "(none)",
                    checkAccount != null ? "$" + String.format("%.2f", checkAccount.balance) : "(none)");
        }
        view.getTransferPanel().setDirectionLabels(fromMasked, toMasked);
    }

    private void populateAccountSummaries() {
        if (primaryCustomerId == null) {
            return;
        }
        List<PrimaryAccount> accounts = db.getPrimaryAccountsByCustomerId(primaryCustomerId);
        PrimaryAccount check = accounts.stream()
                .filter(a -> a.accountType.equalsIgnoreCase("CHECK") || a.accountType.equalsIgnoreCase("CHECKING"))
                .findFirst().orElse(null);
        PrimaryAccount saving = accounts.stream()
                .filter(a -> a.accountType.equalsIgnoreCase("SAVING") || a.accountType.equalsIgnoreCase("SAVINGS"))
                .findFirst().orElse(null);
        this.checkAccount = check;
        this.savingAccount = saving;

        if (check != null) {
            view.getCustomerHome().setCheckAccount(maskAccount(check.accountNumber), check.balance);
        }
        if (saving != null) {
            view.getCustomerHome().setSavingAccount(maskAccount(saving.accountNumber), saving.balance);
        }
    }

    private void handleTransaction() {
        var panel = view.getTransactionPanel();
        Integer accountId = panel.getSelectedAccountId();
        if (accountId == null) {
            showError("Select an account first.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(panel.getAmountInput());
        } catch (NumberFormatException ex) {
            showError("Enter a valid amount.");
            return;
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            showError("Amount must be a real number.");
            return;
        }
        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }
        if (amount > 1_000_000_000d) {
            showError("Amount is too large.");
            return;
        }

        boolean isDeposit = panel.isDeposit();
        Double currentBalance = db.getPrimaryAccountBalance(accountId);
        if (currentBalance == null) {
            showError("Could not load account balance.");
            return;
        }

        double newBalance = isDeposit ? currentBalance + amount : currentBalance - amount;
        if (!isDeposit && newBalance < 0) {
            showError("Insufficient funds for withdrawal.");
            return;
        }

        if (!db.updatePrimaryAccountBalance(accountId, newBalance)) {
            showError("Could not update account balance.");
            return;
        }

        String type = isDeposit ? "DEPOSIT" : "WITHDRAWAL";
        int txId = db.createPrimaryTransaction(accountId, type, amount,
                type + " via customer portal (" + panel.getSelectedAccountLabel() + ")");
        if (txId == -1) {
            showError("Balance updated, but failed to record transaction.");
        } else {
            JOptionPane.showMessageDialog(container,
                    "Transaction successful (ID: " + txId + ").\nNew balance: $" + String.format("%.2f", newBalance),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Refresh account listings and summaries so balances reflect the change.
        loadCustomerContext();
        panel.getBackButton().doClick();
    }

    private void handleTransfer() {
        if (checkAccount == null && savingAccount == null) {
            showError("No accounts available for transfer.");
            return;
        }
        boolean fromCheck = view.getTransferPanel().isFromCheck();
        PrimaryAccount from = fromCheck ? checkAccount : savingAccount;
        PrimaryAccount to = fromCheck ? savingAccount : checkAccount;
        if (from == null || to == null) {
            showError("Both accounts are required to transfer.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(view.getTransferPanel().getAmountInput());
        } catch (NumberFormatException ex) {
            showError("Enter a valid amount.");
            return;
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            showError("Amount must be a real number.");
            return;
        }
        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }
        if (amount > 1_000_000_000d) {
            showError("Amount is too large.");
            return;
        }

        if (from.balance < amount) {
            showError("Insufficient funds.");
            return;
        }

        double newFromBalance = from.balance - amount;
        double newToBalance = to.balance + amount;

        // Apply debits/credits
        if (!db.updatePrimaryAccountBalance(from.accountId, newFromBalance)) {
            showError("Could not update source account.");
            return;
        }
        if (!db.updatePrimaryAccountBalance(to.accountId, newToBalance)) {
            showError("Could not update destination account.");
            // best effort rollback
            db.updatePrimaryAccountBalance(from.accountId, from.balance);
            return;
        }

        int txId = db.createPrimaryTransfer(from.accountId, to.accountId, amount,
                "Transfer via customer portal " + maskAccount(from.accountNumber) + " -> " + maskAccount(to.accountNumber));
        if (txId == -1) {
            showError("Balances updated, but failed to record transfer.");
        } else {
            JOptionPane.showMessageDialog(container,
                    "Transfer successful (ID: " + txId + ").\nFrom new balance: $" + String.format("%.2f", newFromBalance) +
                            "\nTo new balance: $" + String.format("%.2f", newToBalance),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // refresh cached accounts/balances
        loadCustomerContext();
        view.getTransferPanel().getBackButton().doClick();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(container, message, "Transaction Failed", JOptionPane.ERROR_MESSAGE);
    }

    private String maskAccount(String accountNumber) {
        if (accountNumber == null) {
            return "N/A";
        }
        String clean = accountNumber.replaceAll("\\s+", "");
        if (clean.length() <= 4) {
            return clean;
        }
        String last4 = clean.substring(clean.length() - 4);
        return "****" + last4;
    }

    private void showAccountTransactions(PrimaryAccount account, String title, String cardName) {
        if (account == null) {
            showError("No account found.");
            return;
        }
        List<Database.PrimaryTransaction> txs = db.getPrimaryTransactionsForAccount(account.accountId);
        List<String> lines = txs.stream()
                .map(t -> formatTransactionLine(account.accountId, t))
                .collect(Collectors.toList());

        if ("Check_Transactions".equals(cardName)) {
            checkTxPanel = view.ensureCheckTransactionsPanel();
            checkTxPanel.setTitle(title);
            checkTxPanel.setTransactions(lines);
        } else {
            savingTxPanel = view.ensureSavingTransactionsPanel();
            savingTxPanel.setTitle(title);
            savingTxPanel.setTransactions(lines);
        }
        view.showCard(cardName);
        cardLayout.show(container, cardName);
    }

    private String formatTransactionLine(int accountId, Database.PrimaryTransaction t) {
        String direction;
        if (t.fromAccountId != null && t.fromAccountId == accountId && t.toAccountId != null) {
            direction = "-" + String.format("$%.2f", t.amount) + " to " + maskAccountId(t.toAccountId);
        } else if (t.toAccountId != null && t.toAccountId == accountId && t.fromAccountId != null) {
            direction = "+" + String.format("$%.2f", t.amount) + " from " + maskAccountId(t.fromAccountId);
        } else if (t.toAccountId != null && t.toAccountId == accountId) {
            direction = "+" + String.format("$%.2f", t.amount) + " (deposit)";
        } else if (t.fromAccountId != null && t.fromAccountId == accountId) {
            direction = "-" + String.format("$%.2f", t.amount) + " (withdrawal)";
        } else {
            direction = String.format("$%.2f", t.amount);
        }
        return "[" + t.type + "] " + direction + " on " + t.timestamp;
    }

    private String maskAccountId(int accountId) {
        Database.PrimaryAccount acc = db.getPrimaryAccountById(accountId);
        if (acc != null) {
            return maskAccount(acc.accountNumber);
        }
        return "acct#" + accountId;
    }
}
