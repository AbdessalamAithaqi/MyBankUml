package GUI.Customer;

import javax.swing.*;

public class CustomerTransactions extends JPanel{

    public CustomerTransactions(String type){

        JLabel title = new JLabel(type);
        add(title);
    }
    
}
