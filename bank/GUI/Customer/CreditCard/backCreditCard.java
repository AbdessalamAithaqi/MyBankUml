package GUI.Customer.CreditCard;

import javax.swing.*;

import java.awt.*;

public class backCreditCard extends JPanel {

    private JLabel ccvLabel;

    public backCreditCard(String ccv) {

        setOpaque(false);
        setPreferredSize(new Dimension(360, 210));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(null); // manually position elements (back of card is static)

        // Magnetic strip (black bar)
        JPanel magStrip = new JPanel();
        magStrip.setBackground(Color.BLACK);
        magStrip.setBounds(0, 25, 360, 40);

        // Signature strip (white)
        JPanel signatureStrip = new JPanel();
        signatureStrip.setBackground(new Color(255, 255, 255));
        signatureStrip.setBounds(20, 85, 260, 32);

        // CVV box
        JPanel ccvBox = new JPanel();
        ccvBox.setBackground(Color.WHITE);
        ccvBox.setBounds(290, 85, 50, 32);
        ccvBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        ccvLabel = new JLabel(ccv);
        ccvLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        ccvLabel.setForeground(Color.BLACK);
        ccvLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ccvLabel.setBounds(290, 85, 50, 32);
        ccvBox.add(ccvLabel);

        // Add to the content panel
        content.add(magStrip);
        content.add(signatureStrip);
        content.add(ccvBox);

        setLayout(new BorderLayout());
        add(content);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int arc = 26;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Blue-to-cyan gradient to match front card
        Color c1 = new Color(40, 60, 140);
        Color c2 = new Color(90, 180, 250);
        GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // White soft border
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }

    public void setCVV(String cvv) {
        ccvLabel.setText(cvv);
    }
}
