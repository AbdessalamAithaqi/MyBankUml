package bank.GUI.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bank.models.users.Customer;

public class CustomerCardPanel extends JPanel{
    private CardLayout cardLayout;

    private final Customer customer;
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

        this.customer = customer;
        customerHome = new CustomerHome(customer);
        
       
        card = new CustomerCard(customer);
        transactionPanel = new CustomerMakeTransaction();
        transferPanel = new CustomerMakeTransfer();

        add(customerHome, "Home");   
        add(card, "Card");
        add(transactionPanel, "Transaction");
        add(transferPanel, "Transfer");

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

    public CustomerMakeTransaction getTransactionPanel() {
        return transactionPanel;
    }

    public CustomerMakeTransfer getTransferPanel() {
        return transferPanel;
    }

    public void showCard(String name) {
        cardLayout.show(this, name);
    }

    public CustomerTransactions ensureCheckTransactionsPanel() {
        if (checkTransactions == null) {
            checkTransactions = new CustomerTransactions("Check Transactions");
            add(checkTransactions, "Check_Transactions");
            checkTransactions.getBackButton().addActionListener(ev -> showCard("Home"));
        }
        return checkTransactions;
    }

    public CustomerTransactions ensureSavingTransactionsPanel() {
        if (savingTransactions == null) {
            savingTransactions = new CustomerTransactions("Saving Transactions");
            add(savingTransactions, "Saving_Transactions");
            savingTransactions.getBackButton().addActionListener(ev -> showCard("Home"));
        }
        return savingTransactions;
    }


}
