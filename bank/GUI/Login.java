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
    }

    public void setHeading(String text) {
        title.setText(text);
    }

    public void enableConfirmPassword(boolean enabled) {
        confirmLabel.setVisible(enabled);
        confirmPassword.setVisible(enabled);
        revalidate();
        repaint();
    }
}
