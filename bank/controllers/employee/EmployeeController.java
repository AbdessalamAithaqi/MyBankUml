package bank.controllers.employee;

import bank.GUI.Buttons.defaultButton;
import bank.GUI.Login;
import bank.GUI.Teller.TellerPanel;
import bank.controllers.auth.LoginController;
import bank.controllers.common.CustomerActionHelper;
import bank.database.Database;
import bank.database.Database.PrimaryAccount;
import bank.models.org.Bank;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    private final JPanel menuPanel;
    private final Login registrationPanel;
    private final String menuCardName;
    private final String registerCardName;
    private final String searchCardName;

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
        this.searchCardName = cardName;

        this.menuCardName = "EMPLOYEE_MENU_" + username;
        this.registerCardName = "EMPLOYEE_REGISTER_" + username;
        this.menuPanel = buildMenuPanel();
        container.add(menuPanel, menuCardName);

        this.registrationPanel = new Login("Customer");
        registrationPanel.setHeading("Customer Registration");
        registrationPanel.getLoginButton().setText("Register");
        registrationPanel.enableConfirmPassword(true);
        registrationPanel.enableCustomerDetails(true);
        registrationPanel.getBackButton().addActionListener(e -> cardLayout.show(container, menuCardName));
        container.add(registrationPanel, registerCardName);

        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.getLogoutButton().addActionListener(e -> {
            cardLayout.show(container, menuCardName);
            container.revalidate();
            container.repaint();
        });

        view.getSearchButton().addActionListener(e -> handleSearch());
        view.getLoadCustomerButton().addActionListener(e -> loadCustomerAccounts());
        view.getOpenTransactionButton().addActionListener(e -> customerActions.showTransactionDialog(view));
        view.getOpenTransferButton().addActionListener(e -> customerActions.showTransferDialog(view));

        // Menu navigation
        JButton searchButton = (JButton) menuPanel.getClientProperty("searchButton");
        JButton registerButton = (JButton) menuPanel.getClientProperty("registerButton");
        JButton backButton = (JButton) menuPanel.getClientProperty("backButton");
        if (searchButton != null) {
            searchButton.addActionListener(e -> cardLayout.show(container, cardName));
        }
        if (registerButton != null) {
            registerButton.addActionListener(e -> {
                registrationPanel.clearInputs();
                cardLayout.show(container, registerCardName);
            });
        }
        if (backButton != null) {
            backButton.addActionListener(e -> cardLayout.show(container, LoginController.CARD_LOGIN_REGISTER));
        }

        registrationPanel.getLoginButton().addActionListener(e -> handleRegistration());
    }

    public void showDashboard() {
        cardLayout.show(container, menuCardName);
    }

    public void dispose() {
        container.remove(view);
        container.remove(menuPanel);
        container.remove(registrationPanel);
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
                String branchStr = view.getBranchFilter();
                Integer branchId = null;
                if (!branchStr.isBlank()) {
                    try {
                        branchId = Integer.parseInt(branchStr);
                    } catch (NumberFormatException ex) {
                        showError("Branch ID must be numeric.");
                        return;
                    }
                }
                if (ln.isBlank() && birthplace.isBlank() && address.isBlank() && createdAfter.isBlank() && dobAfter.isBlank() && branchId == null) {
                    showError("Enter at least one filter (name/birthplace/address/created-after/dob-after/branch).");
                    return;
                }
                results = db.getPrimaryAccountsByCustomerFilters(ln, birthplace, address, createdAfter, dobAfter, branchId);
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

    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        defaultButton searchBtn = new defaultButton("Search / Manage");
        defaultButton registerBtn = new defaultButton("Register Customer");
        center.add(Box.createVerticalGlue());
        center.add(searchBtn);
        center.add(Box.createVerticalStrut(10));
        center.add(registerBtn);
        center.add(Box.createVerticalGlue());

        JPanel bottom = new JPanel(new BorderLayout());
        defaultButton backBtn = new defaultButton("Logout");
        backBtn.setPreferredSize(new Dimension(250, 60));
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        bottom.add(backBtn, BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        panel.putClientProperty("searchButton", searchBtn);
        panel.putClientProperty("registerButton", registerBtn);
        panel.putClientProperty("backButton", backBtn);
        return panel;
    }

    private void handleRegistration() {
        String username = registrationPanel.getUsernameInput();
        String password = registrationPanel.getPasswordInput();
        String confirm = registrationPanel.getConfirmPasswordInput();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            showError("Username and password are required.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }
        String dob = registrationPanel.getDobInput();
        if (!isValidDob(dob)) {
            showError("Enter a valid date of birth (YYYY-MM-DD), not in the future.");
            return;
        }
        String ssn = registrationPanel.getSsnInput();
        if (ssn == null || !ssn.matches("\\d{9}")) {
            showError("SSN is required (9 digits).");
            return;
        }
        String phone = registrationPanel.getPhoneInput();
        if (phone == null || phone.isBlank()) {
            showError("Phone is required.");
            return;
        }
        String email = registrationPanel.getEmailInput();
        if (email == null || email.isBlank()) {
            showError("Email is required.");
            return;
        }
        int branchId;
        try {
            branchId = Integer.parseInt(registrationPanel.getBranchInput());
            if (branchId <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Branch ID must be a positive number.");
            return;
        }

        String birthplace = registrationPanel.getBirthplaceInput();
        String address = registrationPanel.getAddressInput();
        String maskedSsn = "*****" + ssn.substring(ssn.length() - 4);

        if (!db.createUserPrimary(username, password, "CUSTOMER")) {
            showError("Failed to create user.");
            return;
        }
        String[] names = splitUsername(username);
        if (!db.createCustomerPrimary(username, names[0], names[1], birthplace, dob, address, maskedSsn, phone, branchId, email)) {
            showError("Failed to create customer profile.");
            return;
        }
        Integer customerId = db.getPrimaryCustomerIdByUsername(username);
        if (customerId != null) {
            db.createPrimaryAccount(customerId, "CHECK", 0.0, branchId);
            db.createPrimaryAccount(customerId, "SAVING", 0.0, branchId);
        }
        JOptionPane.showMessageDialog(container, "Customer registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        registrationPanel.clearInputs();
        cardLayout.show(container, menuCardName);
    }

    private boolean isValidDob(String dob) {
        if (dob == null || dob.isBlank()) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(dob.trim());
            LocalDate earliest = LocalDate.of(1900, 1, 1);
            LocalDate today = LocalDate.now();
            return !date.isAfter(today) && !date.isBefore(earliest);
        } catch (DateTimeParseException e) {
            return false;
        }
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
}
