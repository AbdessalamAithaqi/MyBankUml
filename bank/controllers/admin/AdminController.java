package bank.controllers.admin;

import bank.GUI.Admin.AdminPanel;
import bank.controllers.auth.LoginController;
import bank.database.Database;
import bank.database.Database.PrimaryAccount;
import bank.models.org.Bank;
import bank.controllers.common.CustomerActionHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class AdminController {
    private final Database db;
    private final JPanel container;
    private final CardLayout cardLayout;
    private final String cardName;
    private final AdminPanel view;
    private final Runnable onLogout;
    private final Bank bank;
    private final CustomerActionHelper customerActions = new CustomerActionHelper();

    public AdminController(Bank bank, JPanel container, CardLayout cardLayout, String username, Runnable onLogout) {
        this.bank = bank;
        this.container = container;
        this.cardLayout = cardLayout;
        this.onLogout = onLogout;

        this.db = Database.getInstance();
        db.getConnection();

        this.cardName = "ADMIN_HOME_" + username;
        this.view = new AdminPanel();
        container.add(view, cardName);

        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.getLogoutButton().addActionListener(e -> {
            cardLayout.show(container, LoginController.CARD_LOGIN_REGISTER);
            container.revalidate();
            container.repaint();
            if (onLogout != null) {
                onLogout.run();
            }
        });

        view.getSearchButton().addActionListener(e -> handleSearch());
        view.getAddUserButton().addActionListener(e -> handleAddUser());
        view.getUpdateUserButton().addActionListener(e -> handleUpdateUser());
        view.getDeleteUserButton().addActionListener(e -> handleDeleteUser());
        view.getUpdateBalanceButton().addActionListener(e -> handleUpdateBalance());
        view.getUpdateStatusButton().addActionListener(e -> handleUpdateStatus());
        view.getLoadCustomerButton().addActionListener(e -> loadCustomerAccounts());
        view.getOpenTransactionButton().addActionListener(e -> customerActions.showTransactionDialog(view));
        view.getOpenTransferButton().addActionListener(e -> customerActions.showTransferDialog(view));
    }

    public void showDashboard() {
        cardLayout.show(container, cardName);
    }

    public void dispose() {
        container.remove(view);
        container.revalidate();
        container.repaint();
    }

    private void handleSearch() {
        List<PrimaryAccount> results = new ArrayList<>();
        try {
            if (view.isSearchByCustomer()) {
                String input = view.getCustomerLastNameInput();
                if (input.isBlank()) {
                    showError("Enter a customer last name.");
                    return;
                }
                results = db.getPrimaryAccountsByCustomerLastName(input);
            } else if (view.isSearchByAccountId()) {
                String input = view.getAccountIdInput();
                if (input.isBlank()) {
                    showError("Enter an account ID.");
                    return;
                }
                int accountId = Integer.parseInt(input);
                PrimaryAccount account = db.getPrimaryAccountById(accountId);
                if (account != null) {
                    results.add(account);
                }
            } else if (view.isSearchByType()) {
                String type = view.getSelectedType();
                String normalized = type.toUpperCase().startsWith("CHECK") ? "CHECK" : "SAVING";
                results = db.getPrimaryAccountsByType(normalized);
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid numeric ID.");
            return;
        }

        List<String> lines = new ArrayList<>();
        for (PrimaryAccount acc : results) {
            lines.add(formatAccount(acc));
        }
        if (lines.isEmpty()) {
            lines.add("No accounts found.");
        }
        view.showAccounts(lines);
    }

    private void handleAddUser() {
        String username = view.getUserUsername();
        String password = view.getUserPassword();
        String role = view.getUserRole();
        if (username.isBlank() || password.isBlank()) {
            showError("Username and password are required.");
            return;
        }
        if (!db.createUserPrimary(username, password, role)) {
            showError("Failed to create user.");
            return;
        }
        String[] names = splitUsername(username);
        if ("CUSTOMER".equalsIgnoreCase(role)) {
            db.createCustomerPrimary(username, names[0], names[1]);
        } else if ("TELLER".equalsIgnoreCase(role)) {
            db.createPrimaryEmployee(username, names[0], names[1]);
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            db.createPrimaryAdministrator(username, names[0], names[1]);
        }
        showInfo("User created: " + username);
    }

    private void handleUpdateUser() {
        String username = view.getUserUsername();
        if (username.isBlank()) {
            showError("Username is required.");
            return;
        }
        String password = view.getUserPassword();
        String role = view.getUserRole();
        boolean ok = db.updatePrimaryUser(username, password.isBlank() ? null : password, role);
        if (!ok) {
            showError("Failed to update user.");
            return;
        }
        showInfo("User updated: " + username);
    }

    private void handleDeleteUser() {
        String username = view.getUserUsername();
        if (username.isBlank()) {
            showError("Username is required.");
            return;
        }
        if (!db.deletePrimaryUser(username)) {
            showError("Failed to delete user.");
            return;
        }
        showInfo("User deleted: " + username);
    }

    private void handleUpdateBalance() {
        String idStr = view.getAccountIdForUpdate();
        String balStr = view.getNewBalanceInput();
        try {
            int id = Integer.parseInt(idStr);
            double bal = Double.parseDouble(balStr);
            if (bal < 0 || Double.isNaN(bal) || Double.isInfinite(bal)) {
                showError("Enter a valid non-negative balance.");
                return;
            }
            if (!db.updatePrimaryAccountBalance(id, bal)) {
                showError("Could not update balance.");
                return;
            }
            showInfo("Balance updated for account " + id);
        } catch (NumberFormatException ex) {
            showError("Enter valid numeric account ID and balance.");
        }
    }

    private void handleUpdateStatus() {
        String idStr = view.getAccountIdForUpdate();
        String status = view.getNewStatusInput();
        try {
            int id = Integer.parseInt(idStr);
            if (!db.updatePrimaryAccountStatus(id, status)) {
                showError("Could not update status.");
                return;
            }
            showInfo("Status updated for account " + id);
        } catch (NumberFormatException ex) {
            showError("Enter a valid numeric account ID.");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(container, msg, "Admin Action Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(container, msg, "Admin", JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatAccount(PrimaryAccount acc) {
        String masked = maskAccount(acc.accountNumber);
        String owner = acc.ownerDisplay != null ? acc.ownerDisplay : "Unknown";
        return masked + " | id=" + acc.accountId + " | type=" + acc.accountType + " | $" + String.format("%.2f", acc.balance) + " | owner=" + owner;
    }

    private String maskAccount(String accountNumber) {
        if (accountNumber == null) return "N/A";
        String clean = accountNumber.replaceAll("\\s+", "");
        if (clean.length() <= 4) return clean;
        return "****" + clean.substring(clean.length() - 4);
    }

    private String[] splitUsername(String username) {
        String first = username;
        String last = "";
        int dot = username.indexOf('.');
        if (dot > 0 && dot < username.length() - 1) {
            first = username.substring(0, dot);
            last = username.substring(dot + 1);
        }
        return new String[] { first, last };
    }

    private void loadCustomerAccounts() {
        String username = view.getActionUsername();
        if (username.isBlank()) {
            showError("Enter a customer username to load.");
            return;
        }
        customerActions.loadCustomer(username, view);
    }
}
