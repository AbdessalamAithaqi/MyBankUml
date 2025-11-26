package GUI.Customer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;

import GUI.Buttons.defaultButton;


public class CustomerMakeTransaction extends JPanel{

    private defaultButton back;
    private JTextField receiverAccount;
    private JTextField amount;
    private defaultButton makeTransaction;

    public CustomerMakeTransaction(){

        back = new defaultButton("Back");

    
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JLabel title = new JLabel("Make a Transaction", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));

        JPanel centerPanel = createCenterPanel();

        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);

    }

    private JPanel createCenterPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel receiverLabel = new JLabel("To Account:", SwingConstants.LEFT);
        receiverLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        receiverAccount = new JTextField();
        receiverAccount.setPreferredSize(new Dimension(250, 60));
        receiverAccount.setMaximumSize(new Dimension(250,60));
        receiverAccount.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel receiverWrapper = createTextFieldPanel(receiverLabel, receiverAccount);
        
        JLabel amountLabel = new JLabel("Amount :", SwingConstants.LEFT);
        amountLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        amount = new JTextField();
        amount.setPreferredSize(new Dimension(250, 60));
        amount.setMaximumSize(new Dimension(250,60));
        amount.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel amountWrapper = createTextFieldPanel(amountLabel, amount);

        makeTransaction = new defaultButton("Make Transaction");

        panel.add(Box.createVerticalStrut(10));
        panel.add(receiverWrapper);
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountWrapper);
        panel.add(Box.createVerticalStrut(10));
        panel.add(makeTransaction);

        return panel;
    }

    private JPanel createTextFieldPanel(JLabel label, JTextField textField){

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelRow.add(label);
        labelRow.setMaximumSize(new Dimension(200, 25)); // MATCH TEXTFIELD WIDTH

        // Textfield row (center aligned automatically by wrapper)
        JPanel textFieldRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        textFieldRow.add(textField);
        textFieldRow.setMaximumSize(new Dimension(200, 40));

        wrapper.add(labelRow);
        wrapper.add(textFieldRow);

        return wrapper;
    }

    public defaultButton getBackButton(){
        return back;
    }

    public defaultButton getMakeTransactionButton(){
        return makeTransaction;
    }
    
}
