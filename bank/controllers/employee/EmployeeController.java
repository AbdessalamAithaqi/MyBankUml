package bank.controllers.employee;

import bank.GUI.Teller.TellerPanel;
import bank.controllers.auth.LoginController;
import bank.database.Database;
import bank.models.org.Bank;

import java.awt.*;

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
