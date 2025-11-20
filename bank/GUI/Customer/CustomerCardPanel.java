package GUI.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Models.Customer;

public class CustomerCardPanel extends JPanel{
    private CardLayout cardLayout;

    private CustomerHome customerHome;
    //private Customer customer;

    private CustomerTransactions checkTransactions;
    private CustomerTransactions savingTransactions;
    private CustomerCard card;
    private CustomerMakeTransaction transactionPanel;
    private CustomerMakeTransfer transferPanel;

    public CustomerCardPanel(Customer customer){
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        //this.customer = customer;
        customerHome = new CustomerHome(customer);
        checkTransactions = new CustomerTransactions("Check");
        savingTransactions = new CustomerTransactions("Saving");
        card = new CustomerCard();
        transactionPanel = new CustomerMakeTransaction();
        transferPanel = new CustomerMakeTransfer();

        add(customerHome, "Home");
        add(checkTransactions, "Check_Transactions");
        add(savingTransactions, "Saving_Transactions");
        add(card, "Card");
        add(transactionPanel, "Transaction");
        add(transferPanel, "Transfer");

        customerHome.getCheckPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                cardLayout.show(CustomerCardPanel.this, "Check_Transactions");
            }
        });

        customerHome.getSavingPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                cardLayout.show(CustomerCardPanel.this, "Saving_Transactions");
            }
        });

    }


    public CustomerHome getCustomerHome(){
        return customerHome;
    }
    


}
