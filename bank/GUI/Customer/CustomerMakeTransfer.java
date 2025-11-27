package bank.GUI.Customer;

import javax.swing.*;
import java.awt.*;

import bank.GUI.Buttons.defaultButton;

public class CustomerMakeTransfer extends JPanel{

    private defaultButton back;
    private JTextField amount;
    private JRadioButton selectCheck;
    private JRadioButton selectSaving;
    private ButtonGroup fromAccount;
    private defaultButton transfer;
    private JPanel centerPanel;
    private JLabel fromLabel;
    private JLabel toLabel;
    private JLabel fromBalance;
    private JLabel toBalance;

    public CustomerMakeTransfer(){
        back = new defaultButton("Back");

        amount = new JTextField();
        amount.setPreferredSize(new Dimension(250, 60));
        amount.setMaximumSize(new Dimension(250,60));
        amount.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        selectCheck = new JRadioButton("From Check");
        selectCheck.setFont(new Font("SansSerif", Font.BOLD, 20));
        selectSaving = new JRadioButton("From Saving", true);
        selectSaving.setFont(new Font("SansSerif", Font.BOLD, 20));
        
        fromAccount =  new ButtonGroup();
        fromAccount.add(selectCheck);
        fromAccount.add(selectSaving);

        transfer = new defaultButton("Transfer");


        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JLabel title = new JLabel("Transfer Funds", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));


        centerPanel = createCenterPanel();

        add(title, BorderLayout.NORTH);
        add( centerPanel, BorderLayout.CENTER);
        add(back,BorderLayout.SOUTH);

    }

    private JPanel createCenterPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        
        JPanel radioButtons = new JPanel();
        radioButtons.setMaximumSize(new Dimension(400, 40));
        radioButtons.add(selectCheck);
        radioButtons.add(selectSaving);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));


        JLabel amountLabel = new JLabel("Amount: ", SwingConstants.LEFT);
        amountLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        amount.setAlignmentX(Component.LEFT_ALIGNMENT);

        textFieldPanel.add(amountLabel);
        textFieldPanel.add(amount);

        textFieldPanel.setMaximumSize(textFieldPanel.getPreferredSize());;

        fromLabel = new JLabel();
        toLabel = new JLabel();
        fromLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        toLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fromBalance = new JLabel();
        toBalance = new JLabel();
        fromBalance.setFont(new Font("SansSerif", Font.PLAIN, 12));
        toBalance.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JPanel direction = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        row1.add(fromLabel);
        row1.add(fromBalance);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        row2.add(toLabel);
        row2.add(toBalance);
        direction.add(row1);
        direction.add(row2);
        direction.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(Box.createVerticalStrut(40));
        panel.add(radioButtons);
        panel.add(Box.createVerticalStrut(10));
        panel.add(direction);
        panel.add(Box.createVerticalStrut(10));
        panel.add(textFieldPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(transfer);
        return panel;
    }

    public defaultButton getBackButton(){
        return back;
    }

    public defaultButton getTransferButton(){
        return transfer;
    }

    public boolean isFromCheck() {
        return selectCheck.isSelected();
    }

    public JRadioButton getSelectCheckRadio() {
        return selectCheck;
    }

    public JRadioButton getSelectSavingRadio() {
        return selectSaving;
    }

    public void selectCheck() {
        selectCheck.setSelected(true);
    }

    public void selectSaving() {
        selectSaving.setSelected(true);
    }

    public void setAvailability(boolean hasCheck, boolean hasSaving) {
        selectCheck.setEnabled(hasCheck);
        selectSaving.setEnabled(hasSaving);
        if (!hasCheck && hasSaving) {
            selectSaving();
        } else if (hasCheck && !hasSaving) {
            selectCheck();
        }
    }

    public String getAmountInput() {
        return amount.getText().trim();
    }

    public void setDirectionLabels(String from, String to) {
        fromLabel.setText("From: " + from);
        toLabel.setText("To: " + to);
    }

    public void setBalanceLabels(String fromBalanceText, String toBalanceText) {
        fromBalance.setText("(" + fromBalanceText + ")");
        toBalance.setText("(" + toBalanceText + ")");
    }
    
}
