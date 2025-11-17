package GUI;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.*;

public class CardPanel extends JPanel{

    LoginRegister lr = new LoginRegister();

    public CardPanel() {
        this.setBackground(Color.gray);
        this.setLayout(new CardLayout());   

        this.add(lr);
    }
}
