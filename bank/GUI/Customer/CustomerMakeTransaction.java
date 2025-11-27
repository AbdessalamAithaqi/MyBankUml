package bank.GUI.Customer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;

import bank.GUI.Buttons.defaultButton;

import java.util.ArrayList;
import java.util.List;

public class CustomerMakeTransaction extends JPanel{

    private defaultButton back;
    private JComboBox<AccountOption> accountSelect;
    private JRadioButton depositRadio;
    private JRadioButton withdrawRadio;
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

        JLabel accountLabel = new JLabel("Account:", SwingConstants.LEFT);
        accountLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        accountSelect = new JComboBox<>();
        accountSelect.setPreferredSize(new Dimension(320, 40));
        accountSelect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        accountSelect.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel accountWrapper = createComboPanel(accountLabel, accountSelect);

        depositRadio = new JRadioButton("Deposit");
        withdrawRadio = new JRadioButton("Withdraw");
        depositRadio.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(depositRadio);
        group.add(withdrawRadio);

        JPanel radioRow = new JPanel();
        radioRow.add(depositRadio);
        radioRow.add(withdrawRadio);
        radioRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel amountLabel = new JLabel("Amount :", SwingConstants.LEFT);
        amountLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        amount = new JTextField();
        amount.setPreferredSize(new Dimension(320, 40));
        amount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        amount.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel amountWrapper = createTextFieldPanel(amountLabel, amount);

        makeTransaction = new defaultButton("Make Transaction");

        panel.add(Box.createVerticalStrut(10));
        panel.add(accountWrapper);
        panel.add(Box.createVerticalStrut(10));
        panel.add(radioRow);
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
        labelRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // allow full width

        // Textfield row (center aligned automatically by wrapper)
        JPanel textFieldRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        textFieldRow.add(textField);
        textFieldRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        wrapper.add(labelRow);
        wrapper.add(textFieldRow);

        return wrapper;
    }

    private JPanel createComboPanel(JLabel label, JComboBox<?> combo){
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelRow.add(label);
        labelRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JPanel comboRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        comboRow.add(combo);
        comboRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        wrapper.add(labelRow);
        wrapper.add(comboRow);

        return wrapper;
    }

    public void setAccounts(List<AccountOption> options) {
        accountSelect.removeAllItems();
        if (options != null) {
            for (AccountOption opt : options) {
                accountSelect.addItem(opt);
            }
        }
    }

    public Integer getSelectedAccountId() {
        AccountOption opt = (AccountOption) accountSelect.getSelectedItem();
        return opt == null ? null : opt.accountId();
    }

    public String getSelectedAccountLabel() {
        AccountOption opt = (AccountOption) accountSelect.getSelectedItem();
        return opt == null ? "" : opt.label();
    }

    public boolean isDeposit() {
        return depositRadio.isSelected();
    }

    public String getAmountInput() {
        return amount.getText().trim();
    }

    public defaultButton getBackButton(){
        return back;
    }

    public defaultButton getMakeTransactionButton(){
        return makeTransaction;
    }

    public record AccountOption(int accountId, String label) {
        @Override public String toString() { return label; }
    }
}
