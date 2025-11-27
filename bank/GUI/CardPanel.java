package bank.GUI;

import java.awt.CardLayout;

import javax.swing.*;

import bank.GUI.Customer.CustomerCardPanel;
import bank.GUI.Teller.TellerPanel;
import bank.models.users.Customer;

public class CardPanel extends JPanel{

    private CardLayout cardLayout;

    private LoginRegister loginRegister;
    private Login customerLogin;
    private Login tellerLogin;
    private Login adminLogin;
    private CustomerCardPanel customerCardPanel;
    private TellerPanel tellerPanel;

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

        tellerLogin.getLoginButton().addActionListener(e -> {
            // Temporary while database and controllers ar not done
            //Auth will be added here.

            tellerPanel = new TellerPanel();
            add(tellerPanel, "LOGGED_IN");

            cardLayout.show(this, "LOGGED_IN");
        });

        adminLogin.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "HOME");
        });

        customerLogin.getLoginButton().addActionListener(e -> {
        // Temporary while database and controllers are not done

            //this will contain auth and fetching the account 
            // this is just for testing the GUI 
            customerCardPanel = new CustomerCardPanel(new Customer("Ulysse"));
            add(customerCardPanel, "LOGGED_IN");

            
            customerCardPanel.getCustomerHome().getLogoutButton().addActionListener(ev -> {
                if (customerCardPanel != null) {
                    remove(customerCardPanel);
                }
                // 2. Clear the reference so GC can clean everything
                customerCardPanel = null;
                // 3. Go back to HOME screen
                cardLayout.show(this, "HOME");
                
                // 4. Revalidate layout to avoid empty areas / redraw issues
                revalidate();
            });
            
            cardLayout.show(this, "LOGGED_IN");

        });


    }
}
