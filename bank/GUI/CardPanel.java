package GUI;

import java.awt.CardLayout;

import javax.swing.*;

public class CardPanel extends JPanel{

    private CardLayout cardLayout;

    private LoginRegister loginRegister;
    private Login customerLogin;
    private Login tellerLogin;
    private Login adminLogin;

    public CardPanel() {
        cardLayout = new CardLayout();

        loginRegister = new LoginRegister();
        customerLogin = new Login("Customer");
        tellerLogin = new Login("Teller");
        adminLogin = new Login("Admin");

        setLayout(cardLayout);   

        add(loginRegister, "HOME");
        add(customerLogin, "CUSTOMER_LOGIN"); 
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

        tellerLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

        adminLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

    }
}
