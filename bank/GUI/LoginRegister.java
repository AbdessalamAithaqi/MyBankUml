package bank.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import bank.GUI.Buttons.*;

public class LoginRegister extends JPanel {

    private JButton customerLogin;
    private JButton customerRegister;
    private JButton tellerLogin;
    private JButton admin;

    public LoginRegister(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        //setBackground(Color.GRAY);
        
        // TITLE
        JLabel title = new JLabel("My Bank Uml", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));

        add(title, BorderLayout.NORTH);

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                resizeTitleFont(title);
            }
        });

        //Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        //centerPanel.setBackground(Color.BLUE);
        
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(createCustomerSection());
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(createTellerSection());
        
        add(centerPanel, BorderLayout.CENTER);

        //Admin
        admin = new adminButton();
        add(admin, BorderLayout.SOUTH);

    }

/*
 * INITIALISATION METHODS:
 * The following method create the different panels that make up the loginRegister panel
 */

    private JPanel createCustomerSection(){ // Creates the panel with customer login and register
        JPanel section = new JPanel();
        //section.setBackground(Color.GREEN);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Customer", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                resizeSubTitleFont(title);
            }
        });

        customerLogin = createButton("Login");
        customerRegister = createButton("Register");

        section.add(title);
        section.add(Box.createVerticalStrut(15));
        section.add(customerLogin);
        section.add(Box.createVerticalStrut(10));
        section.add(customerRegister);

        return section;
    }

    private JPanel createTellerSection(){ // created the panels with the teller login
        JPanel section = new JPanel();
        //section.setBackground(Color.RED);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Teller", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                resizeSubTitleFont(title);
            }
        });

        tellerLogin = createButton("Login");

        section.add(title);
        section.add(Box.createVerticalStrut(15));
        section.add(tellerLogin);

        return section;
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


/*
 * RESIZING METHODS:
 * The following method are called when a resize of the window 
 */
    private void resizeTitleFont(JLabel title){ // resizes the title with relation to window width
        
        int width = getWidth();

        int newSize = Math.max(20, width/20);

        title.setFont(new Font("SansSerif", Font.BOLD, newSize));

        revalidate();
    }

    private void resizeSubTitleFont(JLabel title){ // resize the subtitles with realtion ot window width
        int width = getWidth();

        int newSize = Math.max(15, width/25);
        newSize = Math.min(newSize, 60);

        title.setFont(new Font("SansSerif", Font.BOLD, newSize));

        revalidate();
    }
    
/*
 * GETTERS
 */

 public JButton getCustomerLogin(){
    return customerLogin;
 }

 public JButton getCustomerRegister(){
    return customerRegister;
 }

 public JButton getTellerLogin(){
    return tellerLogin;
 }

 public JButton getAdminButton(){
    return admin;
 }
}
