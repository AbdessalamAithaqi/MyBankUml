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
        
       
        card = new CustomerCard();
        transactionPanel = new CustomerMakeTransaction();
        transferPanel = new CustomerMakeTransfer();

        add(customerHome, "Home");   
        add(card, "Card");
        add(transactionPanel, "Transaction");
        add(transferPanel, "Transfer");

        customerHome.getCheckPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                checkTransactions = new CustomerTransactions("Check");
                add(checkTransactions, "Check_Transactions");
                cardLayout.show(CustomerCardPanel.this, "Check_Transactions");

                checkTransactions.getBackButton().addActionListener(ev -> {
                    cardLayout.show(CustomerCardPanel.this, "Home");
                });
            }
        });

        customerHome.getSavingPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                savingTransactions = new CustomerTransactions("Saving");
                add(savingTransactions, "Saving_Transactions");
                cardLayout.show(CustomerCardPanel.this, "Saving_Transactions");

                savingTransactions.getBackButton().addActionListener(ev -> {
                    cardLayout.show(CustomerCardPanel.this, "Home");
                });
            }
        });

        customerHome.getMakeTransferButton().addActionListener(e -> {
            cardLayout.show(this, "Transfer");
        });
        
        customerHome.getMakeTransactionButton().addActionListener(e -> {
            cardLayout.show(this, "Transaction");
        });

        customerHome.getShowCardButton().addActionListener(e -> {
            cardLayout.show(this, "Card");
        });

        transferPanel.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "Home");
        });

        transactionPanel.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "Home");
        });

        card.getBackButton().addActionListener(e -> {
            cardLayout.show(this, "Home");
        });

        card.getMakeTransactionButton().addActionListener(e -> {
            cardLayout.show(this, "Transaction");
        });

    }


    public CustomerHome getCustomerHome(){
        return customerHome;
    }
    


}
