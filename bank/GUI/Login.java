package bank.GUI;

import javax.swing.*;
import java.awt.*;

public class Login extends JPanel {

    private JTextField username;
    private JPasswordField password;

    private JButton login;
    private JButton back;
    private JLabel title;
    private JLabel confirmLabel;
    private JPasswordField confirmPassword;
    private JLabel birthplaceLabel;
    private JTextField birthplaceField;
    private JLabel addressLabel;
    private JTextField addressField;
    private JLabel dobLabel;
    private JTextField dobField;
    private JLabel ssnLabel;
    private JTextField ssnField;
    private JLabel phoneLabel;
    private JTextField phoneField;
    private JLabel branchLabel;
    private JTextField branchField;
    private JLabel emailLabel;
    private JTextField emailField;

    public Login(String type){

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        title = new JLabel(type + " Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        centerPanel.add(Box.createVerticalStrut(20));

        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(usernameLabel);

        username = new JTextField();
        username.setPreferredSize(new Dimension(250, 60));
        username.setMaximumSize(new Dimension(250,60));
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(username);

        centerPanel.add(Box.createVerticalStrut(10));

        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(passwordLabel);

        password = new JPasswordField();
        password.setPreferredSize(new Dimension(250, 60));
        password.setMaximumSize(new Dimension(250,60));
        password.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(password);

        centerPanel.add(Box.createVerticalStrut(10));

        confirmLabel = new JLabel("Confirm Password:", SwingConstants.CENTER);
        confirmLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmPassword = new JPasswordField();
        confirmPassword.setPreferredSize(new Dimension(250, 60));
        confirmPassword.setMaximumSize(new Dimension(250,60));
        confirmPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmLabel.setVisible(false);
        confirmPassword.setVisible(false);
        centerPanel.add(confirmLabel);
        centerPanel.add(confirmPassword);

        centerPanel.add(Box.createVerticalStrut(10));
        birthplaceLabel = new JLabel("Birthplace:", SwingConstants.CENTER);
        birthplaceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        birthplaceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        birthplaceField = new JTextField();
        birthplaceField.setPreferredSize(new Dimension(250, 40));
        birthplaceField.setMaximumSize(new Dimension(250,40));
        birthplaceField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(birthplaceLabel);
        centerPanel.add(birthplaceField);

        centerPanel.add(Box.createVerticalStrut(10));
        addressLabel = new JLabel("Address:", SwingConstants.CENTER);
        addressLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(250, 40));
        addressField.setMaximumSize(new Dimension(250,40));
        addressField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(addressLabel);
        centerPanel.add(addressField);

        centerPanel.add(Box.createVerticalStrut(10));
        dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):", SwingConstants.CENTER);
        dobLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        dobLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dobField = new JTextField();
        dobField.setPreferredSize(new Dimension(250, 40));
        dobField.setMaximumSize(new Dimension(250,40));
        dobField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(dobLabel);
        centerPanel.add(dobField);
        centerPanel.add(Box.createVerticalStrut(10));

        ssnLabel = new JLabel("SSN (masked):", SwingConstants.CENTER);
        ssnLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        ssnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ssnField = new JTextField();
        ssnField.setPreferredSize(new Dimension(250, 40));
        ssnField.setMaximumSize(new Dimension(250,40));
        ssnField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(ssnLabel);
        centerPanel.add(ssnField);

        centerPanel.add(Box.createVerticalStrut(10));
        phoneLabel = new JLabel("Phone:", SwingConstants.CENTER);
        phoneLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(250, 40));
        phoneField.setMaximumSize(new Dimension(250,40));
        phoneField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel = new JLabel("Email:", SwingConstants.CENTER);
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(250, 40));
        emailField.setMaximumSize(new Dimension(250,40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(emailLabel);
        centerPanel.add(emailField);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(phoneLabel);
        centerPanel.add(phoneField);

        centerPanel.add(Box.createVerticalStrut(10));
        branchLabel = new JLabel("Branch ID:", SwingConstants.CENTER);
        branchLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        branchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        branchField = new JTextField();
        branchField.setPreferredSize(new Dimension(250, 40));
        branchField.setMaximumSize(new Dimension(250,40));
        branchField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(branchLabel);
        centerPanel.add(branchField);
        setCustomerDetailsVisible(false);

        centerPanel.add(Box.createVerticalStrut(10));
        
        login = createButton("Login");
        centerPanel.add(login);

        add(centerPanel, BorderLayout.CENTER);



        back = createButton("Back");
        
        add(back, BorderLayout.SOUTH);

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

    public JButton getLoginButton(){
        return login;
    }

    public String getUsernameInput() {
        return username.getText().trim();
    }

    public String getPasswordInput() {
        return new String(password.getPassword());
    }

    public String getConfirmPasswordInput() {
        return new String(confirmPassword.getPassword());
    }

    public void clearInputs() {
        username.setText("");
        password.setText("");
        confirmPassword.setText("");
        birthplaceField.setText("");
        addressField.setText("");
        dobField.setText("");
        if (ssnField != null) ssnField.setText("");
        if (phoneField != null) phoneField.setText("");
        if (branchField != null) branchField.setText("");
        if (emailField != null) emailField.setText("");
    }

    public void setHeading(String text) {
        title.setText(text);
    }

    public void enableConfirmPassword(boolean enabled) {
        confirmLabel.setVisible(enabled);
        confirmPassword.setVisible(enabled);
        setCustomerDetailsVisible(enabled);
        revalidate();
        repaint();
    }

    public void enableCustomerDetails(boolean enabled) {
        setCustomerDetailsVisible(enabled);
    }

    private void setCustomerDetailsVisible(boolean visible) {
        if (birthplaceLabel != null) {
            birthplaceLabel.setVisible(visible);
            birthplaceField.setVisible(visible);
            addressLabel.setVisible(visible);
            addressField.setVisible(visible);
            dobLabel.setVisible(visible);
            dobField.setVisible(visible);
            ssnLabel.setVisible(visible);
            ssnField.setVisible(visible);
            phoneLabel.setVisible(visible);
            phoneField.setVisible(visible);
            branchLabel.setVisible(visible);
            branchField.setVisible(visible);
            emailLabel.setVisible(visible);
            emailField.setVisible(visible);
        }
    }

    public String getBirthplaceInput() {
        return birthplaceField == null ? "" : birthplaceField.getText().trim();
    }

    public String getAddressInput() {
        return addressField == null ? "" : addressField.getText().trim();
    }

    public String getDobInput() {
        return dobField == null ? "" : dobField.getText().trim();
    }

    public String getSsnInput() {
        return ssnField == null ? "" : ssnField.getText().trim();
    }

    public String getPhoneInput() {
        return phoneField == null ? "" : phoneField.getText().trim();
    }

    public String getBranchInput() {
        return branchField == null ? "" : branchField.getText().trim();
    }

    public String getEmailInput() {
        return emailField == null ? "" : emailField.getText().trim();
    }
}
