package bank;

import bank.controllers.auth.LoginController;
import bank.models.org.Bank;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame jf = new JFrame();
            CardLayout cardLayout = new CardLayout();
            JPanel container = new JPanel(cardLayout);

            // TODO: wire an actual Bank model once domain logic is ready.
            Bank bank = null;
            LoginController loginController = new LoginController(bank, container, cardLayout);
            loginController.showLoginHome();

            jf.setTitle("MyBankUml");
            jf.setResizable(true);
            jf.setMinimumSize(new Dimension(900, 600));
            jf.setSize(1000, 600);
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            jf.setContentPane(container);
            jf.setVisible(true);
        });
    }
}
