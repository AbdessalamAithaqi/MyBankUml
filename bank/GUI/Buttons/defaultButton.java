package bank.GUI.Buttons;

import javax.swing.*;
import java.awt.*;


public class defaultButton extends JButton{
    public defaultButton(String text){
        super(text);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setFont(new Font("SansSerif", Font.PLAIN, 18));
        setPreferredSize(new Dimension(250, 60));
        setMaximumSize(new Dimension(250, 60));
        setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
}
