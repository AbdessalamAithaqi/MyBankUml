package bank.GUI.Customer;

import javax.swing.*;
import java.awt.*;
import bank.models.Transaction;


public class CustomerTransactions extends JPanel{

    private Transaction[] transactions;
    private JButton back;

    public CustomerTransactions(String type){

        //Temporary will load transactions from DB later
        transactions = new Transaction[5];
        transactions[0] = new Transaction(10, "111111111", "000000000");
        transactions[1] = new Transaction(10, "000000000", "111111111");
        transactions[2] = new Transaction(10, "111111111", "000000000");
        transactions[3] = new Transaction(10, "000000000", "111111111");
        transactions[4] = new Transaction(10, "111111111", "000000000");

        back = createButton("Back");

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));


        JLabel title = new JLabel(type, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setAlignmentX(CENTER_ALIGNMENT);

        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        for (Transaction t : transactions) {
            listPanel.add(createTransactionRow(t));
            listPanel.add(Box.createVerticalStrut(8)); // space between rows
        }
        
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(back,BorderLayout.SOUTH);
        
    }

    private JPanel createTransactionRow(Transaction t) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(245, 245, 245));
        row.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // row height

        // Left side: sender -> receiver
        JLabel people = new JLabel(t.getSenderID() + " -> " + t.getReceiverID());
        people.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Right side: amount
        JLabel amount = new JLabel(t.getAmount()+"$");
        amount.setFont(new Font("SansSerif", Font.BOLD, 14));
        amount.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(people, BorderLayout.WEST);
        row.add(amount, BorderLayout.EAST);

        return row;
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

    public JButton getBackButton(){
        return back;
    }
}
