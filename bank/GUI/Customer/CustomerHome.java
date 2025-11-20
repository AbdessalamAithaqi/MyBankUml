package GUI.Customer;

import javax.swing.*;

import java.awt.*;

import Models.Customer;

public class CustomerHome extends JPanel{

    private JButton logout;
    private JButton makeTransaction;
    private JButton makeTransfer;
    private JButton showCard;
    private JPanel checkPanel;
    private JPanel savingPanel;


    public CustomerHome(Customer customer){
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        
        logout = createButton("LOGOUT");
        makeTransaction = createButton("Make Transaction");
        makeTransfer = createButton("Make Transfer");
        showCard = createButton("Card");

        //TOP PANEL
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));

        JLabel customerName = new JLabel(customer.getName());
        customerName.setFont(new Font("SansSerif", Font.BOLD, 20));
        topPanel.add(customerName, BorderLayout.WEST);

        topPanel.add(logout, BorderLayout.EAST);

        
        //CENTER PANEL
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel checkLabel = new JLabel("Check:", SwingConstants.LEFT);
        checkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel savingLabel = new JLabel("Saving:", SwingConstants.LEFT);
        savingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        savingLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        
        checkPanel = createAccountPanel("000000", 10000);
        savingPanel = createAccountPanel("000000", 10000);
        
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(checkLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(checkPanel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(savingLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(savingPanel);
        
        //BOTTOM PANEL
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(makeTransaction);
        bottomPanel.add(makeTransfer);
        bottomPanel.add(showCard);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

    }

    //Temporary the id and balance will later be loaded from DB
    private JPanel createAccountPanel(String id, int balance){
        JPanel panel = new JPanel();
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2, true),
            BorderFactory.createEmptyBorder(0, 50, 0, 50)
        ));
        panel.setMaximumSize(new Dimension(1200, 80));
        
        JLabel ID = new JLabel("ID: "+id);
        ID.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel Balance = new JLabel(balance+"$");
        Balance.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        

        panel.add(ID, BorderLayout.WEST);
        panel.add(Balance, BorderLayout.EAST);

        return panel;
    }

    private JButton createButton(String text){ // Temporary method for button that don't exist yet
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btn.setPreferredSize(new Dimension(250, 60));
        btn.setMaximumSize(new Dimension(250, 60));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    public JButton getLogoutButton(){
        return logout;
    }

    public JPanel getCheckPanel(){
        return checkPanel;
    }

    public JPanel getSavingPanel(){
        return savingPanel;
    }
}
