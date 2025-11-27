package bank.GUI.Buttons;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Cursor;

public class adminButton extends JButton{
    public adminButton(){
        super("<html><u>admin</u></html>");
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(new Font("SansSerif", Font.PLAIN, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(SwingConstants.CENTER);
    }
    
}
