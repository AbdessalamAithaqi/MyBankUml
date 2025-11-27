package bank.GUI.Teller;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import bank.GUI.Buttons.defaultButton;

import java.util.List;

public class TellerPanel extends JPanel{
    // Search controls
    private JRadioButton byCustomerRadio;
    private JRadioButton byAccountIdRadio;
    private JRadioButton byTypeRadio;

    private JTextField customerIdField;
    private JTextField accountIdField;
    private JComboBox<String> typeCombo;

    private JButton searchButton;

    // Results area
    private JPanel resultsPanel;   // holds result rows vertically
    private JScrollPane resultsScroll;
    private final defaultButton logout;

    // Customer actions
    private JTextField actionUsername;
    private JButton loadCustomerButton;
    private JButton openTransactionButton;
    private JButton openTransferButton;

    private final boolean includeLogout;
    private final boolean includeCustomerActions;

    public TellerPanel(String heading) {
        this(heading, true, true);
    }

    public TellerPanel(String heading, boolean includeLogout, boolean includeCustomerActions){
        this.includeLogout = includeLogout;
        this.includeCustomerActions = includeCustomerActions;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // ===== TITLE + LOGOUT =====
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel title = new JLabel(heading, SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        if (includeLogout) {
            logout = new defaultButton("Logout");
            top.add(title, BorderLayout.WEST);
            top.add(logout, BorderLayout.EAST);
        } else {
            logout = null;
            top.add(title, BorderLayout.WEST);
        }
        add(top, BorderLayout.NORTH);

        // ===== CENTER CONTENT =====
        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setOpaque(false);

        center.add(createSearchPanel(), BorderLayout.NORTH);
        center.add(createResultsArea(), BorderLayout.CENTER);
        if (includeCustomerActions) {
            center.add(createCustomerActions(), BorderLayout.SOUTH);
        }

        add(center, BorderLayout.CENTER);
        
    }

    public TellerPanel() {
        this("Account Search (Teller)");
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // --- Radio buttons ---
        byCustomerRadio = new JRadioButton("Search by Customer Last Name");
        byAccountIdRadio = new JRadioButton("Search by Account ID");
        byTypeRadio = new JRadioButton("Search by Account Type");

        ButtonGroup group = new ButtonGroup();
        group.add(byCustomerRadio);
        group.add(byAccountIdRadio);
        group.add(byTypeRadio);

        byCustomerRadio.setSelected(true); // default

        JPanel radioRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioRow.setOpaque(false);
        radioRow.add(byCustomerRadio);
        radioRow.add(byAccountIdRadio);
        radioRow.add(byTypeRadio);

        // --- Inputs ---
        customerIdField = new JTextField(15);
        accountIdField = new JTextField(15);
        typeCombo = new JComboBox<>(new String[] { "Checking", "Saving" });

        JPanel customerRow = makeLabeledRow("Last Name:", customerIdField);
        JPanel accountRow  = makeLabeledRow("Account ID:", accountIdField);
        JPanel typeRow     = makeLabeledRow("Type:", typeCombo);

        // --- Search button ---
        searchButton = new JButton("Search");
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(searchButton);

        panel.add(radioRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(customerRow);
        panel.add(accountRow);
        panel.add(typeRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(buttonRow);

        // GUI-only behavior: enable/disable appropriate fields
        updateInputsForMode();
        byCustomerRadio.addActionListener(e -> updateInputsForMode());
        byAccountIdRadio.addActionListener(e -> updateInputsForMode());
        byTypeRadio.addActionListener(e -> updateInputsForMode());

        return panel;
    }

    private JPanel makeLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));

        row.add(label);
        row.add(comp);

        return row;
    }

    // Just GUI: enable only the relevant input for the selected mode
    private void updateInputsForMode() {
        boolean byCustomer = byCustomerRadio.isSelected();
        boolean byAccount  = byAccountIdRadio.isSelected();
        boolean byType     = byTypeRadio.isSelected();

        customerIdField.setEnabled(byCustomer);
        accountIdField.setEnabled(byAccount);
        typeCombo.setEnabled(byType);
    }

    private JScrollPane createResultsArea() {
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);

        resultsScroll = new JScrollPane(resultsPanel);
        resultsScroll.setBorder(BorderFactory.createTitledBorder("Results"));
        resultsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return resultsScroll;
    }

    private JPanel createCustomerActions() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Customer Actions"));

        actionUsername = new JTextField(15);
        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userRow.setOpaque(false);
        userRow.add(new JLabel("Username:"));
        userRow.add(actionUsername);

        loadCustomerButton = new JButton("Load Customer");
        openTransactionButton = new JButton("Deposit/Withdraw");
        openTransferButton = new JButton("Transfer");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(loadCustomerButton);
        buttons.add(openTransactionButton);
        buttons.add(openTransferButton);

        panel.add(userRow);
        panel.add(buttons);
        return panel;
    }

    // One visual row for an account (pure GUI, display-only)
    private JPanel createResultRow(String text) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(new EmptyBorder(6, 10, 6, 10));
        row.setBackground(new Color(245, 245, 245));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.PLAIN, 12));
        row.add(label, BorderLayout.CENTER);

        return row;
    }

    // ===== Public API for the rest of the team =====

    public JButton getSearchButton() {
        return searchButton;
    }

    public boolean isSearchByCustomer() {
        return byCustomerRadio.isSelected();
    }

    public boolean isSearchByAccountId() {
        return byAccountIdRadio.isSelected();
    }

    public boolean isSearchByType() {
        return byTypeRadio.isSelected();
    }

    public String getCustomerIdInput() {
        return customerIdField.getText().trim();
    }

    // Alias for clarity with last-name search
    public String getCustomerLastNameInput() {
        return getCustomerIdInput();
    }

    public String getAccountIdInput() {
        return accountIdField.getText().trim();
    }

    public String getSelectedType() {
        return (String) typeCombo.getSelectedItem();
    }

    // Call this from the controller with formatted strings per account
    public void showAccounts(List<String> accountLines) {
        resultsPanel.removeAll();
        if (accountLines != null) {
            for (String line : accountLines) {
                resultsPanel.add(createResultRow(line));
                resultsPanel.add(Box.createVerticalStrut(5));
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    public defaultButton getLogoutButton() {
        return logout;
    }

    public String getActionUsername() {
        return actionUsername != null ? actionUsername.getText().trim() : "";
    }

    public JButton getLoadCustomerButton() {
        return loadCustomerButton;
    }

    public JButton getOpenTransactionButton() {
        return openTransactionButton;
    }

    public JButton getOpenTransferButton() {
        return openTransferButton;
    }
}
    
