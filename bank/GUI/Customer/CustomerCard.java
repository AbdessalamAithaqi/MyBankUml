package GUI.Customer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;


import GUI.Buttons.defaultButton;

public class CustomerCard extends JPanel{
    
    private defaultButton back;
    private defaultButton makeTransaction;

    public CustomerCard(){

        back = new defaultButton("Back");
        makeTransaction = new defaultButton("Make Transaction");

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JLabel title = new JLabel("Your Card");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerPanel = makeCenterPanel();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1,2));
        bottomPanel.add(back);
        bottomPanel.add(makeTransaction);

        add(title, BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER );
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //NOT DONE
    private JPanel makeCenterPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));

        JLabel cardNum = new JLabel("adfasdfasdf");
        JLabel cardHolder = new JLabel("Ulysse");
        JLabel expDate = new JLabel("adfasfdas");
        JLabel CCV = new JLabel("123");

        JPanel front = new JPanel();
        front.setLayout(new GridBagLayout());
        front.add(cardNum);
        front.add(cardHolder);
        front.add(expDate);

        JPanel back = new JPanel();
        back.add(CCV);

        panel.add(front);
        panel.add(back);

        return panel;
    }


    public defaultButton getBackButton(){
        return back;
    }

    public defaultButton getMakeTransactionButton(){
        return makeTransaction;
    }

}
