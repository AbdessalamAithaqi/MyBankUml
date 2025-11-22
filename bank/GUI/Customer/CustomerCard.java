package GUI.Customer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;


import GUI.Buttons.defaultButton;
import GUI.Customer.CreditCard.backCreditCard;
import GUI.Customer.CreditCard.frontCreditCard;

public class CustomerCard extends JPanel{
    
    private defaultButton back;
    private defaultButton makeTransaction;

    public CustomerCard(){

        back = new defaultButton("Back");
        makeTransaction = new defaultButton("Make Transaction");

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JLabel title = new JLabel("Your Card");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerPanel = makeCenterPanel();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1,2));
        bottomPanel.add(back);
        bottomPanel.add(makeTransaction);

        add(title, BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER );
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //NOT DONE
    private JPanel makeCenterPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
    panel.setOpaque(false); // if you want the parent background to show

    frontCreditCard front = new frontCreditCard("MyBankUML", "1111 2222 3333 4444", "Ulysse", "01/28");
    backCreditCard back  = new backCreditCard("123");

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridy = 0;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;

    // Left card
    gbc.gridx = 0;
    gbc.weightx = 0.5;
    gbc.insets = new Insets(0, 0, 0, 20); // right gap
    panel.add(front, gbc);

    // Right card
    gbc.gridx = 1;
    gbc.weightx = 0.5;
    gbc.insets = new Insets(0, 20, 0, 0); // left gap (optional)
    panel.add(back, gbc);

    // === Make them scale with window size while keeping aspect ratio ===
    addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            resizeCards(panel, front, back);
        }
    });

    return panel;
    }

    private void resizeCards(JPanel centerPanel, frontCreditCard front, backCreditCard back) {
    // Base card size (must match your card's original design)
    int baseW = 360;
    int baseH = 210;
    int gap   = 40; // total horizontal gap between cards

    int availW = centerPanel.getWidth();
    int availH = centerPanel.getHeight();

    if (availW <= 0 || availH <= 0) return;

    // We have two cards side by side: total base width = 2 * baseW + gap
    double scaleX = (double) availW / (2 * baseW + gap);
    double scaleY = (double) availH / baseH;

    double scale = Math.min(scaleX, scaleY); // keep aspect ratio

    // Avoid absurdly tiny or negative sizes
    if (scale <= 0) scale = 0.1;

    int cardW = (int) (baseW * scale);
    int cardH = (int) (baseH * scale);

    Dimension cardSize = new Dimension(cardW, cardH);

    front.setPreferredSize(cardSize);
    back.setPreferredSize(cardSize);

    // Let layout managers recalc based on new preferred sizes
    centerPanel.revalidate();
    centerPanel.repaint();
    }



    public defaultButton getBackButton(){
        return back;
    }

    public defaultButton getMakeTransactionButton(){
        return makeTransaction;
    }

}
