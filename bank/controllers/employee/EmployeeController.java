package bank.controllers.employee;

import bank.GUI.Teller.TellerPanel;
import bank.controllers.auth.LoginController;
import bank.database.Database;
import bank.database.Database.PrimaryAccount;
import bank.models.org.Bank;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class EmployeeController {
    private final Database db;
    private final JPanel container;
    private final CardLayout cardLayout;
    private final String cardName;
    private final TellerPanel view;
    private final Runnable onLogout;
    private final Bank bank;

    public EmployeeController(Bank bank, JPanel container, CardLayout cardLayout, String username, Runnable onLogout) {
        this.bank = bank;
        this.container = container;
        this.cardLayout = cardLayout;
        this.onLogout = onLogout;

        this.db = Database.getInstance();
        db.getConnection();

        this.cardName = "EMPLOYEE_HOME_" + username;
        this.view = new TellerPanel("Account Search (Teller)");
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(container, message, "Search Failed", JOptionPane.ERROR_MESSAGE);
    }
}
