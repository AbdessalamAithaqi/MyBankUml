package bank.controllers.auth;

import bank.models.org.Bank;
import bank.views.LoginView;


public class AppController {
    private final Bank model;
    private final LoginController loginController;

    public AppController(Bank model, LoginView loginView) {
        this.model = model;
        this.loginController = new LoginController(model, loginView);
    }

    public void start() {
        loginController.showLogin();
    }
}
