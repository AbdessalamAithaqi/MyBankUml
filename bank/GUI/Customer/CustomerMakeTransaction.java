package GUI.Customer;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

import GUI.Buttons.defaultButton;

public class CustomerMakeTransaction extends JPanel{
    private defaultButton back;

    public CustomerMakeTransaction(){
        back = new defaultButton("Back");

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JLabel title = new JLabel("Make a Transaction");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        add(title, BorderLayout.NORTH);
        add(back, BorderLayout.SOUTH);

    }

    public defaultButton getBackButton(){
        return back;
    }
    
}
