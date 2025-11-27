package bank.GUI;

import java.awt.CardLayout;

import javax.swing.*;

import bank.GUI.Customer.CustomerCardPanel;
import bank.GUI.Teller.TellerPanel;
import bank.database.Database;
import bank.models.users.Customer;

public class CardPanel extends JPanel{

    private CardLayout cardLayout;

    private final Database db;

    private LoginRegister loginRegister;
    private Login customerLogin;
    private Login customerRegister;
    private Login tellerLogin;
    private Login adminLogin;
    private CustomerCardPanel customerCardPanel;
    private TellerPanel tellerPanel;

    public CardPanel() {
        cardLayout = new CardLayout();

        db = Database.getInstance();
        db.getConnection();

        loginRegister = new LoginRegister();
        customerLogin = new Login("Customer");
        customerRegister = new Login("Customer");
        customerRegister.setHeading("Customer Registration");
        customerRegister.getLoginButton().setText("Register");
        customerRegister.enableConfirmPassword(true);
        tellerLogin = new Login("Teller");
        adminLogin = new Login("Admin");

        setLayout(cardLayout);   

        add(loginRegister, "HOME");
        add(customerLogin, "CUSTOMER_LOGIN");
        add(customerRegister, "CUSTOMER_REGISTER");
        add(tellerLogin, "TELLER_LOGIN");
        add(adminLogin, "ADMIN_LOGIN");

        loginRegister.getCustomerLogin().addActionListener(e -> {
            cardLayout.show(this, "CUSTOMER_LOGIN");
        });

        loginRegister.getTellerLogin().addActionListener(e -> {
            cardLayout.show(this, "TELLER_LOGIN");
        });

        loginRegister.getAdminButton().addActionListener(e -> {
            cardLayout.show(this, "ADMIN_LOGIN");
        });

        customerLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

        customerRegister.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

        tellerLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

        tellerLogin.getLoginButton().addActionListener(e -> {
            tellerPanel = new TellerPanel();
            tellerPanel.getLogoutButton().addActionListener(ev -> {
                cardLayout.show(this, "HOME");
            });
            add(tellerPanel, "LOGGED_IN");
            cardLayout.show(this, "LOGGED_IN");
        });

        adminLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

        customerLogin.getLoginButton().addActionListener(e -> handleCustomerLogin());

        customerRegister.getLoginButton().addActionListener(e -> handleCustomerRegistration());
    }

    private void handleCustomerLogin() {
        String username = customerLogin.getUsernameInput();
        String password = customerLogin.getPasswordInput();

        String role = db.authenticateUser(username, password);
        if (role == null || !role.equalsIgnoreCase("CUSTOMER")) {
            showError("Invalid customer credentials");
            return;
        }

        customerLogin.clearInputs();
        showCustomerDashboard(username);
    }

    private void handleCustomerRegistration() {
        String username = customerRegister.getUsernameInput();
        String password = customerRegister.getPasswordInput();
        String confirm = customerRegister.getConfirmPasswordInput();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            showError("Username and password are required");
            return;
        }

        if (confirm == null || !confirm.equals(password)) {
            showError("Passwords do not match");
            return;
        }

        boolean created = db.createUser(username, password, "CUSTOMER");
        if (!created) {
            showError("Could not create user account");
            return;
        }

        customerRegister.clearInputs();
        showCustomerDashboard(username);
    }

    private void showCustomerDashboard(String username) {
        if (customerCardPanel != null) {
            remove(customerCardPanel);
        }

        customerCardPanel = new CustomerCardPanel(new Customer(username));
        add(customerCardPanel, "LOGGED_IN");

        customerCardPanel.getCustomerHome().getLogoutButton().addActionListener(ev -> logoutCustomer());

        cardLayout.show(this, "LOGGED_IN");
        revalidate();
        repaint();
    }

    private void logoutCustomer() {
        if (customerCardPanel != null) {
            remove(customerCardPanel);
            customerCardPanel = null;
        }
        cardLayout.show(this, "HOME");
        revalidate();
        repaint();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
