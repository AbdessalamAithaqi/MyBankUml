package GUI;

import java.awt.CardLayout;

import javax.swing.*;

public class CardPanel extends JPanel{

    private CardLayout cardLayout;

    private LoginRegister loginRegister = new LoginRegister();
    private CustomerLogin customerLogin = new CustomerLogin();

    public CardPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);   

        add(loginRegister, "HOME");
        add(customerLogin, "CUSTOMER_LOGIN"); 

        loginRegister.getCustomerLogin().addActionListener(e -> {
            cardLayout.show(this, "CUSTOMER_LOGIN");
        });

        customerLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

    }
}
