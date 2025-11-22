import java.awt.Dimension;

import javax.swing.JFrame;
import GUI.CardPanel;

public class Main {
    public static void main(String[] args) {
        JFrame jf = new JFrame();

        CardPanel cardPanel = new CardPanel();

        jf.setTitle("MyBankUml");
        jf.setResizable(true);
        jf.setMinimumSize(new Dimension(900, 600));
        jf.setSize(1000, 600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jf.setContentPane(cardPanel);
        jf.setVisible(true);
    

    }
}
