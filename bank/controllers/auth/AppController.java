package bank.controllers.auth;

import bank.models.org.Bank;

import javax.swing.*;
import java.awt.*;

/**
 * Thin wrapper kept for compatibility; delegates to LoginController.
 */
public class AppController {
    private final LoginController loginController;

    public AppController(Bank model, JPanel container, CardLayout cardLayout) {
        this.loginController = new LoginController(model, container, cardLayout);
    }

    public void start() {
        loginController.showLoginHome();
    }
}
