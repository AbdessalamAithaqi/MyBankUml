package bank.controllers.employee;

import bank.GUI.Teller.TellerPanel;
import bank.controllers.auth.LoginController;
import bank.controllers.common.CustomerActionHelper;
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
    private final CustomerActionHelper customerActions = new CustomerActionHelper();

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
                String ln = view.getCustomerLastNameInput();
                String birthplace = view.getBirthplaceFilter();
                String address = view.getAddressFilter();
                String createdAfter = view.getCreatedAfterFilter();
                String dobAfter = view.getDobAfterFilter();
                if (ln.isBlank() && birthplace.isBlank() && address.isBlank() && createdAfter.isBlank() && dobAfter.isBlank()) {
                    showError("Enter at least one filter (name/birthplace/address/created-after/dob-after).");
                    return;
                }
                results = db.getPrimaryAccountsByCustomerFilters(ln, birthplace, address, createdAfter, dobAfter);
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
        String birthplace = acc.birthplace != null ? acc.birthplace : "N/A";
        String addr = acc.address != null ? acc.address : "N/A";
        String dob = acc.dateOfBirth != null ? acc.dateOfBirth : "N/A";
        String created = acc.createdAt != null ? acc.createdAt : "N/A";
        return "<html><b>Account:</b> " + masked + " (id " + acc.accountId + ") | <b>Type:</b> " + acc.accountType +
               " | <b>Balance:</b> $" + String.format("%.2f", acc.balance) +
               "<br/><b>Owner:</b> " + owner +
               "<br/><b>Birthplace:</b> " + birthplace +
               "<br/><b>Address:</b> " + addr +
               "<br/><b>DOB:</b> " + dob +
               "<br/><b>User Created:</b> " + created +
               "</html>";
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

    private void loadCustomerAccounts() {
        String username = view.getActionUsername();
        if (username.isBlank()) {
            showError("Enter a customer username to load.");
            return;
        }
        customerActions.loadCustomer(username, view);
    }
}
