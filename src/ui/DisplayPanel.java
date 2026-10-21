package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.AngleMode;

/**
 * Expression line, result line, angle mode, and memory keys.
 */
public class DisplayPanel extends JPanel {
    private final JLabel expressionLabel = new JLabel(" ");
    private final JLabel resultLabel = new JLabel("0");
    private final JLabel memoryLabel = new JLabel(" ");
    private final RoundButton angleButton;
    private final Font resultFont = new Font("Segoe UI", Font.BOLD, 40);
    private final Font resultSmallFont = new Font("Segoe UI", Font.BOLD, 22);

    public DisplayPanel(KeypadListener listener) {
        setOpaque(false);
        setLayout(new BorderLayout(0, 6));
        setBorder(new EmptyBorder(16, 18, 12, 18));
        setPreferredSize(new Dimension(420, 176));

        expressionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        expressionLabel.setForeground(new Color(0xA1A1A6));
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        memoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        memoryLabel.setForeground(new Color(0xFF9F0A));
        memoryLabel.setPreferredSize(new Dimension(28, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(memoryLabel, BorderLayout.WEST);
        top.add(expressionLabel, BorderLayout.CENTER);

        resultLabel.setFont(resultFont);
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel keys = new JPanel(new GridLayout(1, 5, 8, 0));
        keys.setOpaque(false);
        angleButton = utilityButton("DEG", "angle", listener);
        keys.add(angleButton);
        keys.add(utilityButton("MC", "mc", listener));
        keys.add(utilityButton("MR", "mr", listener));
        keys.add(utilityButton("M+", "m+", listener));
        keys.add(utilityButton("M-", "m-", listener));

        add(top, BorderLayout.NORTH);
        add(resultLabel, BorderLayout.CENTER);
        add(keys, BorderLayout.SOUTH);
    }

    public void setState(String expression, String result, boolean error, AngleMode mode,
            boolean memory, String memoryText) {
        expressionLabel.setText(expression.length() == 0 ? " " : expression);
        resultLabel.setText(result);
        if (error || result.length() > 14) {
            resultLabel.setFont(resultSmallFont);
        } else {
            resultLabel.setFont(resultFont);
        }
        resultLabel.setForeground(error ? new Color(0xFF6B6B) : Color.WHITE);
        angleButton.setText(mode.label());
        if (memory) {
            memoryLabel.setText("M");
            memoryLabel.setToolTipText(memoryText);
        } else {
            memoryLabel.setText(" ");
            memoryLabel.setToolTipText(null);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
        g.dispose();
    }

    private static RoundButton utilityButton(String label, String command, KeypadListener listener) {
        RoundButton button = new RoundButton(label, new Color(0x3A3A3C));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.addActionListener(event -> listener.onCommand(command));
        return button;
    }
}
