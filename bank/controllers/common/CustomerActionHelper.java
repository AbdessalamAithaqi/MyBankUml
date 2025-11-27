package bank.controllers.common;

import bank.GUI.Customer.CustomerMakeTransaction;
import bank.GUI.Customer.CustomerMakeTransfer;
import bank.database.Database;
import bank.database.Database.PrimaryAccount;
import bank.database.Database.PrimaryTransaction;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reusable helper to perform deposits/withdrawals/transfers on behalf of a specified customer.
 */
public class CustomerActionHelper {
    private final Database db = Database.getInstance();

    private Integer currentCustomerId;
    private String currentUsername;
    private List<PrimaryAccount> accounts;
    private PrimaryAccount checkAccount;
    private PrimaryAccount savingAccount;

    private final CustomerMakeTransaction transactionPanel = new CustomerMakeTransaction();
    private final CustomerMakeTransfer transferPanel = new CustomerMakeTransfer();

    public boolean loadCustomer(String username, JComponent parent) {
        currentUsername = username;
        currentCustomerId = db.getPrimaryCustomerIdByUsername(username);
        if (currentCustomerId == null) {
            JOptionPane.showMessageDialog(parent, "Customer not found", "Load Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        accounts = db.getPrimaryAccountsByCustomerId(currentCustomerId);
        checkAccount = accounts.stream().filter(a -> a.accountType.equalsIgnoreCase("CHECK") || a.accountType.equalsIgnoreCase("CHECKING")).findFirst().orElse(null);
        savingAccount = accounts.stream().filter(a -> a.accountType.equalsIgnoreCase("SAVING") || a.accountType.equalsIgnoreCase("SAVINGS")).findFirst().orElse(null);
        transactionPanel.setAccounts(accounts.stream()
                .map(a -> new CustomerMakeTransaction.AccountOption(a.accountId, maskAccount(a.accountNumber) + " (" + a.accountType + ") - $" + String.format("%.2f", a.balance)))
                .collect(Collectors.toList()));
        transferPanel.setAvailability(checkAccount != null, savingAccount != null);
        updateTransferLabels();
        return true;
    }

    public void showTransactionDialog(JComponent parent) {
        if (currentCustomerId == null) {
            JOptionPane.showMessageDialog(parent, "Load a customer first", "Action Needed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(parent, transactionPanel, "Deposit / Withdraw", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            handleTransaction(parent);
        }
    }

    public void showTransferDialog(JComponent parent) {
        if (currentCustomerId == null) {
            JOptionPane.showMessageDialog(parent, "Load a customer first", "Action Needed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(parent, transferPanel, "Transfer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            handleTransfer(parent);
        }
    }

    private void handleTransaction(JComponent parent) {
        Integer accountId = transactionPanel.getSelectedAccountId();
        if (accountId == null) {
            showError(parent, "Select an account first.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(transactionPanel.getAmountInput());
        } catch (NumberFormatException ex) {
            showError(parent, "Enter a valid amount.");
            return;
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            showError(parent, "Amount must be a real number.");
            return;
        }
        if (amount <= 0) {
            showError(parent, "Amount must be greater than zero.");
            return;
        }
        if (amount > 1_000_000_000d) {
            showError(parent, "Amount is too large.");
            return;
        }
        Double currentBalance = db.getPrimaryAccountBalance(accountId);
        if (currentBalance == null) {
            showError(parent, "Could not load account balance.");
            return;
        }
        boolean isDeposit = transactionPanel.isDeposit();
        double newBalance = isDeposit ? currentBalance + amount : currentBalance - amount;
        if (!isDeposit && newBalance < 0) {
            showError(parent, "Insufficient funds for withdrawal.");
            return;
        }
        if (!db.updatePrimaryAccountBalance(accountId, newBalance)) {
            showError(parent, "Could not update account balance.");
            return;
        }
        String type = isDeposit ? "DEPOSIT" : "WITHDRAWAL";
        int txId = db.createPrimaryTransaction(accountId, type, amount,
                type + " by staff for " + currentUsername + " (" + transactionPanel.getSelectedAccountLabel() + ")");
        if (txId == -1) {
            showError(parent, "Balance updated, but failed to record transaction.");
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Transaction successful (ID: " + txId + ").\nNew balance: $" + String.format("%.2f", newBalance),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // refresh cached data
        loadCustomer(currentUsername, parent);
    }

    private void handleTransfer(JComponent parent) {
        if (checkAccount == null && savingAccount == null) {
            showError(parent, "Customer has no accounts to transfer.");
            return;
        }
        boolean fromCheck = transferPanel.isFromCheck();
        PrimaryAccount from = fromCheck ? checkAccount : savingAccount;
        PrimaryAccount to = fromCheck ? savingAccount : checkAccount;
        if (from == null || to == null) {
            showError(parent, "Customer needs both accounts to transfer.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(transferPanel.getAmountInput());
        } catch (NumberFormatException ex) {
            showError(parent, "Enter a valid amount.");
            return;
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            showError(parent, "Amount must be greater than zero.");
            return;
        }
        if (amount > 1_000_000_000d) {
            showError(parent, "Amount is too large.");
            return;
        }
        if (from.balance < amount) {
            showError(parent, "Insufficient funds.");
            return;
        }

        double newFromBalance = from.balance - amount;
        double newToBalance = to.balance + amount;

        if (!db.updatePrimaryAccountBalance(from.accountId, newFromBalance)) {
            showError(parent, "Could not update source account.");
            return;
        }
        if (!db.updatePrimaryAccountBalance(to.accountId, newToBalance)) {
            showError(parent, "Could not update destination account.");
            db.updatePrimaryAccountBalance(from.accountId, from.balance);
            return;
        }

        int txId = db.createPrimaryTransfer(from.accountId, to.accountId, amount,
                "Transfer by staff for " + currentUsername + " " + maskAccount(from.accountNumber) + " -> " + maskAccount(to.accountNumber));
        if (txId == -1) {
            showError(parent, "Balances updated, but failed to record transfer.");
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Transfer successful (ID: " + txId + ").",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        loadCustomer(currentUsername, parent);
    }

    private void updateTransferLabels() {
        boolean fromCheck = transferPanel.isFromCheck();
        String fromMasked = fromCheck
                ? (checkAccount != null ? maskAccount(checkAccount.accountNumber) : "(none)")
                : (savingAccount != null ? maskAccount(savingAccount.accountNumber) : "(none)");
        String toMasked = fromCheck
                ? (savingAccount != null ? maskAccount(savingAccount.accountNumber) : "(none)")
                : (checkAccount != null ? maskAccount(checkAccount.accountNumber) : "(none)");
        transferPanel.setDirectionLabels(fromMasked, toMasked);
        transferPanel.setBalanceLabels(
                fromCheck
                        ? (checkAccount != null ? "$" + String.format("%.2f", checkAccount.balance) : "(none)")
                        : (savingAccount != null ? "$" + String.format("%.2f", savingAccount.balance) : "(none)"),
                fromCheck
                        ? (savingAccount != null ? "$" + String.format("%.2f", savingAccount.balance) : "(none)")
                        : (checkAccount != null ? "$" + String.format("%.2f", checkAccount.balance) : "(none)")
        );
    }

    private String maskAccount(String accountNumber) {
        if (accountNumber == null) return "N/A";
        String clean = accountNumber.replaceAll("\\s+", "");
        if (clean.length() <= 4) return clean;
        return "****" + clean.substring(clean.length() - 4);
    }

    private void showError(JComponent parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Action Failed", JOptionPane.ERROR_MESSAGE);
    }

    public CustomerMakeTransaction getTransactionPanel() {
        return transactionPanel;
    }

    public CustomerMakeTransfer getTransferPanel() {
        return transferPanel;
    }
}
