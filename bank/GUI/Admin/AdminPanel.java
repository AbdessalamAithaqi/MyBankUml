package bank.GUI.Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import bank.GUI.Buttons.defaultButton;
import bank.GUI.Teller.TellerPanel;

/**
 * Admin panel combines teller search plus user and account management controls.
 */
public class AdminPanel extends JPanel {
    private final TellerPanel searchPanel;

    // User management
    private JTextField userUsername;
    private JPasswordField userPassword;
    private JComboBox<String> userRole;
    private defaultButton addUser;
    private defaultButton updateUser;
    private defaultButton deleteUser;

    // Account management
    private JTextField accountIdField;
    private JTextField balanceField;
    private JComboBox<String> statusCombo;
    private defaultButton updateBalance;
    private defaultButton updateStatus;

    private final defaultButton logout;
    private JTextField actionUsername;
    private JButton loadCustomerButton;
    private JButton openTransactionButton;
    private JButton openTransferButton;

    public AdminPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        searchPanel = new TellerPanel("Account Search (Admin)", false, false);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(0, 10, 0, 0));
        right.add(buildUserSection());
        right.add(Box.createVerticalStrut(20));
        right.add(buildAccountSection());
        right.add(Box.createVerticalStrut(20));
        right.add(buildCustomerActionSection());

        logout = new defaultButton("Logout");
        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        top.add(title, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);
    }

    private JPanel buildUserSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("User Management"));

        userUsername = new JTextField(15);
        userPassword = new JPasswordField(15);
        userRole = new JComboBox<>(new String[] {"TELLER", "ADMIN"});

        addLabeled(panel, "Username:", userUsername);
        addLabeled(panel, "Password:", userPassword);
        addLabeled(panel, "Role:", userRole);

        addUser = new defaultButton("Add User");
        updateUser = new defaultButton("Modify Password");
        deleteUser = new defaultButton("Delete User");
        panel.add(addUser);
        panel.add(Box.createVerticalStrut(6));
        panel.add(updateUser);
        panel.add(Box.createVerticalStrut(6));
        panel.add(deleteUser);
        return panel;
    }

    private JPanel buildAccountSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Account Management"));

        accountIdField = new JTextField(10);
        balanceField = new JTextField(10);
        statusCombo = new JComboBox<>(new String[] {"ACTIVE", "FROZEN", "CLOSED"});

        addLabeled(panel, "Account ID:", accountIdField);
        addLabeled(panel, "New Balance:", balanceField);
        addLabeled(panel, "Status:", statusCombo);

        updateBalance = new defaultButton("Update Balance");
        updateStatus = new defaultButton("Update Status");

        panel.add(updateBalance);
        panel.add(Box.createVerticalStrut(6));
        panel.add(updateStatus);

        return panel;
    }

    private JPanel buildCustomerActionSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Customer Actions"));

        actionUsername = new JTextField(12);
        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        userRow.add(new JLabel("Username:"));
        userRow.add(actionUsername);

        loadCustomerButton = new JButton("Load Customer");
        openTransactionButton = new JButton("Deposit/Withdraw");
        openTransferButton = new JButton("Transfer");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        buttons.add(loadCustomerButton);
        buttons.add(openTransactionButton);
        buttons.add(openTransferButton);

        panel.add(userRow);
        panel.add(buttons);
        return panel;
    }

    private void addLabeled(JPanel parent, String label, JComponent field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(100, 20));
        row.add(jLabel);
        row.add(field);
        parent.add(row);
    }

    // Expose teller search sub-panel API
    public JButton getSearchButton() { return searchPanel.getSearchButton(); }
    public String getCustomerLastNameInput() { return searchPanel.getCustomerLastNameInput(); }
    public String getAccountIdInput() { return searchPanel.getAccountIdInput(); }
    public String getSelectedType() { return searchPanel.getSelectedType(); }
    public void showAccounts(List<String> lines) { searchPanel.showAccounts(lines); }
    public String getBirthplaceFilter() { return searchPanel.getBirthplaceFilter(); }
    public String getAddressFilter() { return searchPanel.getAddressFilter(); }
    public String getCreatedAfterFilter() { return searchPanel.getCreatedAfterFilter(); }
    public String getDobAfterFilter() { return searchPanel.getDobAfterFilter(); }
    public String getBranchFilter() { return searchPanel.getBranchFilter(); }

    // User management getters
    public JButton getAddUserButton() { return addUser; }
    public JButton getUpdateUserButton() { return updateUser; }
    public JButton getDeleteUserButton() { return deleteUser; }
    public String getUserUsername() { return userUsername.getText().trim(); }
    public String getUserPassword() { return new String(userPassword.getPassword()); }
    public String getUserRole() { return (String) userRole.getSelectedItem(); }

    // Account management getters
    public JButton getUpdateBalanceButton() { return updateBalance; }
    public JButton getUpdateStatusButton() { return updateStatus; }
    public String getAccountIdForUpdate() { return accountIdField.getText().trim(); }
    public String getNewBalanceInput() { return balanceField.getText().trim(); }
    public String getNewStatusInput() { return (String) statusCombo.getSelectedItem(); }

    public defaultButton getLogoutButton() { return logout; }

    // Customer actions
    public String getActionUsername() { return actionUsername.getText().trim(); }
    public JButton getLoadCustomerButton() { return loadCustomerButton; }
    public JButton getOpenTransactionButton() { return openTransactionButton; }
    public JButton getOpenTransferButton() { return openTransferButton; }
}
