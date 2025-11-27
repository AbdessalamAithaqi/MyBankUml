package bank.GUI.Customer.CreditCard;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class frontCreditCard extends JPanel{
    private JLabel bankLabel;
    private JLabel cardNumberLabel;
    private JLabel nameLabel;
    private JLabel expLabel;

    public frontCreditCard(String bankName,
                           String cardNumber,
                           String cardholderName,
                           String expiryDate) {

        setOpaque(false);
        setPreferredSize(new Dimension(360, 210));

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        // a bit more internal padding so nothing hugs the corners
        content.setBorder(new EmptyBorder(18, 26, 22, 26));

        // ===== TOP: Bank name / logo =====
        bankLabel = new JLabel(bankName.toUpperCase());
        bankLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        bankLabel.setForeground(Color.WHITE);
        content.add(bankLabel, BorderLayout.NORTH);

        // ===== CENTER: Card number =====
        cardNumberLabel = new JLabel(cardNumber, SwingConstants.LEFT);
        cardNumberLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        cardNumberLabel.setForeground(Color.WHITE);
        cardNumberLabel.setBorder(new EmptyBorder(18, 0, 18, 0));
        content.add(cardNumberLabel, BorderLayout.CENTER);

        // ===== BOTTOM: Name + Expiry (more realistic placement) =====
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;  // stick to bottom-left-ish
        gbc.insets = new Insets(0, 0, 0, 0);

        // Cardholder name (bottom-left, but not in the absolute corner)
        nameLabel = new JLabel(cardholderName.toUpperCase());
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        bottomPanel.add(nameLabel, gbc);

        // Expiry "block" (VALID THRU + date), slightly inset from the right
        JPanel expBlock = new JPanel();
        expBlock.setOpaque(false);
        expBlock.setLayout(new BoxLayout(expBlock, BoxLayout.Y_AXIS));

        JLabel validThru = new JLabel("VALID THRU");
        validThru.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validThru.setForeground(new Color(230, 230, 230));

        expLabel = new JLabel(expiryDate);
        expLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        expLabel.setForeground(Color.WHITE);

        validThru.setAlignmentX(Component.LEFT_ALIGNMENT);
        expLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        expBlock.add(validThru);
        expBlock.add(expLabel);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 40, 0, 5); // 40px gap from name, 5px from right
        bottomPanel.add(expBlock, gbc);

        content.add(bottomPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw rounded card background with gradient
        Graphics2D g2 = (Graphics2D) g.create();
        int arc = 26;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient background (you can tweak these colors)
        Color c1 = new Color(40, 60, 140);
        Color c2 = new Color(90, 180, 250);
        GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Optional: subtle border
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

        g2.dispose();

        super.paintComponent(g);
    }

    // Optional setters if you want to update text later
    public void setCardNumber(String cardNumber) {
        cardNumberLabel.setText(cardNumber);
    }

    public void setCardholderName(String name) {
        nameLabel.setText(name.toUpperCase());
    }

    public void setExpiryDate(String expiry) {
        expLabel.setText("EXP " + expiry);
    }

    public void setBankName(String bankName) {
        bankLabel.setText(bankName.toUpperCase());
    }
}
