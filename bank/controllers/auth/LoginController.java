package bank.controllers;

import bank.database.Database;

//import bank.views.LoginView;
import bank.models.org.Bank;


public class LoginController {
    private final Bank model;
    //private final LoginView view;
    private final Database db;

    public LoginController(Bank model, LoginView view) {
        this.model = model;
        //this.view = view;

        this.db = Database.getInstance();
        db.getConnection();
        
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        /**
        view.addCustomerLoginListener(e -> 
                handleCustomerLogin(view.getCustomerUsernameInput(), view.getCustomerPasswordInput())
            );

        view.addCustomerRegistrationListener(e ->
                handleCustomerRegistration(
                    view.getCustomerUsernameInput(),
                    view.getCustomerPasswordInput(),
                    view.getCustomerConfirmPasswordInput()
                )
            );

        view.addTellerLoginListener(e ->
                handleTellerLogin(view.getTellerUsernameInput(), view.getTellerPasswordInput())
            );

        view.addAdminLoginListener(e ->
                handleAdminLogin(view.getAdminUsernameInput(), view.getAdminPasswordInput())
            );
        */
    }

    public void showLogin() {
        view.show();
    }

    private void handleCustomerLogin(String username, String password) {
        boolean ok = authenticate(username, password, "CUSTOMER");
        if (!ok) {
            view.showError("Invalid customer credentials");
            return;
        }

        view.close();
        new CustomerController(model, new CustomerView()).showDashboard(username);
    }

    private void handleCustomerRegistration(String username, String password, String confirmed_password) {
        boolean registered = registerCustomer(username, password, confirmed_password);
        if (!registered) {
            view.showError("Could not create user account");
            return;
        }

        view.close();
        
        /** 
        TODO: Implement view to add info like name, address that are in DB fields?
        Then have it return back to main login page?
        */
        new CustomerController(model, new CustomerView()).showDashboard(username);
    }

    private void handleTellerLogin(String username, String password) {
        boolean ok = authenticate(username, password, "TELLER");
        if (!ok) {
            view.showError("Invalid teller credentials");
            return;
        }

        view.close();
        new TellerController(model, new TellerView()).showDashboard(username);
    }

    private void handleAdminLogin(String username, String password) {
        boolean ok = authenticate(username, password, "ADMIN");
        if (!ok) {
            view.showError("Invalid admin credentials");
            return;
        }

        view.close();
        new AdminController(model, new AdminView()).showDashboard(username);
    }

    private boolean authenticate(String username, String password, String expectedRole) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return false;
        }
        String role = db.authenticateUser(username, password);
        return role != null && role.equalsIgnoreCase(expectedRole);
    }

    private boolean registerCustomer(String username, String password, String confirmed_password) {
        if (username == null || password == null || confirmed_password == null) {
            return false;
        }
        if (username.isBlank() || password.isBlank()) {
            return false;
        }
        if (!password.equals(confirmed_password)) {
            return false;
        }
        return db.createUser(username, password, "CUSTOMER");

    }
}
