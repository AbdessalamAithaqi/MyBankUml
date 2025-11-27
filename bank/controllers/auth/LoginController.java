package bank.controllers.auth;

import bank.GUI.Login;
import bank.GUI.LoginRegister;
import bank.controllers.admin.AdminController;
import bank.controllers.customer.CustomerController;
import bank.controllers.employee.EmployeeController;
import bank.database.Database;
import bank.models.org.Bank;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LoginController {
    public static final String CARD_LOGIN_REGISTER = "LOGIN_REGISTER";
    public static final String CARD_CUSTOMER_LOGIN = "CUSTOMER_LOGIN";
    public static final String CARD_CUSTOMER_REGISTER = "CUSTOMER_REGISTER";
    public static final String CARD_TELLER_LOGIN = "TELLER_LOGIN";
    public static final String CARD_ADMIN_LOGIN = "ADMIN_LOGIN";

    private final Bank model;
    private final Database db;
    private final JPanel container;
    private final CardLayout cardLayout;

    private final LoginRegister loginRegisterView;
    private final Login customerLoginView;
    private final Login customerRegisterView;
    private final Login tellerLoginView;
    private final Login adminLoginView;

    private CustomerController customerController;
    private EmployeeController employeeController;
    private AdminController adminController;

    public LoginController(Bank model, JPanel container, CardLayout cardLayout) {
        this.model = model;
        this.container = container;
        this.cardLayout = cardLayout;

        this.db = Database.getInstance();
        db.getConnection();

        loginRegisterView = new LoginRegister();
        customerLoginView = new Login("Customer");
        customerRegisterView = new Login("Customer");
        customerRegisterView.setHeading("Customer Registration");
        customerRegisterView.getLoginButton().setText("Register");
        customerRegisterView.enableConfirmPassword(true);
        customerRegisterView.enableCustomerDetails(true);
        tellerLoginView = new Login("Teller");
        adminLoginView = new Login("Admin");

        container.add(loginRegisterView, CARD_LOGIN_REGISTER);
        container.add(customerLoginView, CARD_CUSTOMER_LOGIN);
        container.add(customerRegisterView, CARD_CUSTOMER_REGISTER);
        container.add(tellerLoginView, CARD_TELLER_LOGIN);
        container.add(adminLoginView, CARD_ADMIN_LOGIN);

        attachEventHandlers();
    }

    public void showLoginHome() {
        cardLayout.show(container, CARD_LOGIN_REGISTER);
    }

    private void attachEventHandlers() {
        loginRegisterView.getCustomerLogin().addActionListener(e -> {
            customerLoginView.clearInputs();
            cardLayout.show(container, CARD_CUSTOMER_LOGIN);
        });

        loginRegisterView.getTellerLogin().addActionListener(e -> {
            tellerLoginView.clearInputs();
            cardLayout.show(container, CARD_TELLER_LOGIN);
        });

        loginRegisterView.getAdminButton().addActionListener(e -> {
            adminLoginView.clearInputs();
            cardLayout.show(container, CARD_ADMIN_LOGIN);
        });

        customerLoginView.getBackButton().addActionListener(e -> showLoginHome());
        customerRegisterView.getBackButton().addActionListener(e -> showLoginHome());
        tellerLoginView.getBackButton().addActionListener(e -> showLoginHome());
        adminLoginView.getBackButton().addActionListener(e -> showLoginHome());

        customerLoginView.getLoginButton().addActionListener(e -> handleCustomerLogin());
        customerRegisterView.getLoginButton().addActionListener(e -> handleCustomerRegistration());
        tellerLoginView.getLoginButton().addActionListener(e -> handleTellerLogin());
        adminLoginView.getLoginButton().addActionListener(e -> handleAdminLogin());
    }

    private void handleCustomerLogin() {
        String username = customerLoginView.getUsernameInput();
        String password = customerLoginView.getPasswordInput();

        String role = authenticate(username, password);
        if (!isAuthorized(role, "CUSTOMER")) {
            System.out.println("Customer login failed for user: " + username);
            showError("Invalid customer credentials");
            return;
        }

        System.out.println("Customer login successful for user: " + username);
        showCustomerDashboard(username);
        customerLoginView.clearInputs();
    }

    private void handleCustomerRegistration() {
        String username = customerRegisterView.getUsernameInput();
        String password = customerRegisterView.getPasswordInput();
        String confirm = customerRegisterView.getConfirmPasswordInput();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            showError("Username and password are required");
            return;
        }
        if (confirm == null || !confirm.equals(password)) {
            showError("Passwords do not match");
            return;
        }

        boolean registered = registerCustomer(username, password);
        if (!registered) {
            System.out.println("Customer registration failed for user: " + username);
            showError("Could not create user account");
            return;
        }

        System.out.println("Customer registration succeeded for user: " + username);
        showCustomerDashboard(username);
        customerRegisterView.clearInputs();
    }

    private void handleTellerLogin() {
        String username = tellerLoginView.getUsernameInput();
        String password = tellerLoginView.getPasswordInput();

        String role = authenticate(username, password);
        if (!isAuthorized(role, "TELLER")) {
            System.out.println("Teller login failed for user: " + username);
            showError("Invalid teller credentials");
            return;
        }

        System.out.println("Teller login successful for user: " + username);
        if (employeeController != null) {
            employeeController.dispose();
        }
        employeeController = new EmployeeController(model, container, cardLayout, username, this::showLoginHome);
        employeeController.showDashboard();
        tellerLoginView.clearInputs();
    }

    private void handleAdminLogin() {
        String username = adminLoginView.getUsernameInput();
        String password = adminLoginView.getPasswordInput();

        String role = authenticate(username, password);
        if (!isAuthorized(role, "ADMIN")) {
            System.out.println("Admin login failed for user: " + username);
            showError("Invalid admin credentials");
            return;
        }

        System.out.println("Admin login successful for user: " + username);
        if (adminController != null) {
            adminController.dispose();
        }
        adminController = new AdminController(model, container, cardLayout, username, this::showLoginHome);
        adminController.showDashboard();
        adminLoginView.clearInputs();
    }

    private String authenticate(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return null;
        }
        String role = db.authenticateUser(username, password);
        System.out.println("Auth attempt for user '" + username + "' returned role: " + role);
        return role;
    }

    private boolean isAuthorized(String role, String expectedRole) {
        return role != null && role.equalsIgnoreCase(expectedRole);
    }

    private boolean registerCustomer(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        if (username.isBlank() || password.isBlank()) {
            return false;
        }
        if (!db.createUserPrimary(username, password, "CUSTOMER")) {
            return false;
        }
        // Derive first/last name from username by splitting on the first '.' if present.
        String firstName = username;
        String lastName = "";
        int dot = username.indexOf('.');
        if (dot > 0 && dot < username.length() - 1) {
            firstName = username.substring(0, dot);
            lastName = username.substring(dot + 1);
        }
        String birthplace = customerRegisterView.getBirthplaceInput();
        String address = customerRegisterView.getAddressInput();
        String dob = customerRegisterView.getDobInput();
        String ssn = customerRegisterView.getSsnInput();
        String phone = customerRegisterView.getPhoneInput();
        String branchStr = customerRegisterView.getBranchInput();
        String email = customerRegisterView.getEmailInput();
        if (!isValidDob(dob)) {
            showError("Enter a valid date of birth (YYYY-MM-DD), not in the future.");
            return false;
        }
        if (ssn == null || ssn.isBlank() || !ssn.matches("\\d{9}")) {
            showError("SSN is required (9 digits).");
            return false;
        }
        if (phone == null || phone.isBlank()) {
            showError("Phone is required.");
            return false;
        }
        if (email == null || email.isBlank()) {
            showError("Email is required.");
            return false;
        }
        int branchId;
        try {
            branchId = Integer.parseInt(branchStr.trim());
            if (branchId <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Branch ID must be a positive number.");
            return false;
        }
        birthplace = birthplace == null ? "" : birthplace.trim();
        address = address == null ? "" : address.trim();
        String maskedSsn = "*****" + ssn.substring(ssn.length() - 4);
        if (!db.createCustomerPrimary(username, firstName, lastName, birthplace, dob, address, maskedSsn, phone, branchId, email)) {
            return false;
        }
        Integer customerId = db.getPrimaryCustomerIdByUsername(username);
        if (customerId != null) {
            db.createPrimaryAccount(customerId, "CHECK", 0.0, branchId);
            db.createPrimaryAccount(customerId, "SAVING", 0.0, branchId);
        }
        return true;
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

    private void showCustomerDashboard(String username) {
        if (customerController != null) {
            customerController.dispose();
        }
        customerController = new CustomerController(model, container, cardLayout, username, this::showLoginHome);
        customerController.showDashboard();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(container, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
}
