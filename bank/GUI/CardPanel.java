package GUI;

import java.awt.CardLayout;

import javax.swing.*;

public class CardPanel extends JPanel{

    private CardLayout cardLayout;

    private LoginRegister loginRegister;
    private CustomerLogin customerLogin;
    private TellerLogin tellerLogin;

    public CardPanel() {
        cardLayout = new CardLayout();

        loginRegister = new LoginRegister();
        customerLogin = new CustomerLogin();
        tellerLogin = new TellerLogin();

        setLayout(cardLayout);   

        add(loginRegister, "HOME");
        add(customerLogin, "CUSTOMER_LOGIN"); 
        add(tellerLogin, "TELLER_LOGIN");

        loginRegister.getCustomerLogin().addActionListener(e -> {
            cardLayout.show(this, "CUSTOMER_LOGIN");
        });

        loginRegister.getTellerLogin().addActionListener(e -> {
            cardLayout.show(this, "TELLER_LOGIN");
        });

        customerLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

        tellerLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

    }
}
