package bank.controllers.customer;

import bank.GUI.Customer.CustomerCardPanel;
import bank.controllers.auth.LoginController;
import bank.database.Database;
import bank.models.org.Bank;
import bank.models.users.Customer;

import java.awt.*;

import javax.swing.*;

public class CustomerController {
    private final Database db;
    private final JPanel container;
    private final CardLayout cardLayout;
    private final String cardName;
    private final CustomerCardPanel view;
    private final Runnable onLogout;
    private final Bank bank;

    public CustomerController(Bank bank, JPanel container, CardLayout cardLayout, String username, Runnable onLogout) {
        this.bank = bank;
        this.container = container;
        this.cardLayout = cardLayout;
        this.onLogout = onLogout;

        this.db = Database.getInstance();
        db.getConnection();

        Customer customer = new Customer(username);

        this.cardName = "CUSTOMER_HOME_" + username;
        this.view = new CustomerCardPanel(customer);

        container.add(view, cardName);
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.getCustomerHome().getLogoutButton().addActionListener(e -> {
            cardLayout.show(container, LoginController.CARD_LOGIN_REGISTER);
            container.revalidate();
            container.repaint();
            if (onLogout != null) {
                onLogout.run();
            }
        });
    }

    public void showDashboard() {
        cardLayout.show(container, cardName);
    }

    public void dispose() {
        container.remove(view);
        container.revalidate();
        container.repaint();
    }
}
