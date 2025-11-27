package bank.GUI.Admin;

import javax.swing.*;
import java.awt.*;

import bank.GUI.Buttons.defaultButton;

public class AdminHome extends JPanel {
    private final defaultButton logout;

    public AdminHome(String username) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Signed in as " + username, SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(title);
        center.add(Box.createVerticalStrut(10));
        center.add(subtitle);

        logout = new defaultButton("Logout");
        JPanel bottom = new JPanel();
        bottom.add(logout);

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public defaultButton getLogoutButton() {
        return logout;
    }
}
