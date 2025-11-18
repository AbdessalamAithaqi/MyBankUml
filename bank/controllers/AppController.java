package controllers;

public class AppController {
    private Bank model;
    private Login view;

    public AppController(Bank model, Login view) {
        this.model = model;
        this.view = view;

        attachEventHandlers();
    }

    private void attachEventHandlers() {
        //view.setCustomerLoginListener(e -> handleCustomerLogin())
        //view.setCustomerRegistrationListener(e -> handleCustomerRegistation())
        //view.setTellerLoginListener(e -> handleTellerLogin())
        //view.setAdminLoginListener(e -> handleAdminLogin())
    }

    private void handleCustomerLogin() {
        //view.dispose();
        //new CustomerLoginController(model, new CustomerLoginView());
    }

    private void handleCustomerRegistation() {
        //view.dispose();
        //new CustomerRegistrationController(model, new CustomerRegistrationView());
    }

    private void handleTellerLogin() {
        //view.dispose();
        //new TellerLoginController(model, new TellerLoginView());
    }

    private void handleAdminLogin() {
        //view.dispose();
        //new AdminLoginController(model, new AdminLoginView());
    }

}
