package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JButton;

/**
 * Flat rounded button. Swing paints the shape; the command is separate from the label.
 */
public class RoundButton extends JButton {
    private final Color fill;

    public RoundButton(String text, Color fill) {
        super(text);
        this.fill = fill;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFocusable(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setForeground(Color.WHITE);
        setMargin(new Insets(0, 0, 0, 0));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Color color = fill;
        if (getModel().isPressed()) {
            color = fill.darker();
        } else if (getModel().isRollover()) {
            color = brighter(fill);
        }
        g.setColor(color);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g.setColor(getForeground());
        if ("\u232b".equals(getText())) {
            paintBackspace(g);
        } else {
            g.setFont(getFont());
            String label = getText();
            int textWidth = g.getFontMetrics().stringWidth(label);
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
            g.drawString(label, x, y);
        }
        g.dispose();
    }

    /**
     * Drawn by hand because the backspace character is missing from the button font.
     */
    private void paintBackspace(Graphics2D g) {
        int width = 22;
        int height = 16;
        int left = (getWidth() - width) / 2;
        int top = (getHeight() - height) / 2;
        int tip = left;
        int body = left + 7;
        int right = left + width;
        int bottom = top + height;
        int mid = top + height / 2;
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawPolygon(
                new int[] {body, right, right, body, tip},
                new int[] {top, top, bottom, bottom, mid},
                5);
        int crossLeft = body + 4;
        int crossRight = right - 5;
        int crossTop = top + 4;
        int crossBottom = bottom - 4;
        g.drawLine(crossLeft, crossTop, crossRight, crossBottom);
        g.drawLine(crossRight, crossTop, crossLeft, crossBottom);
    }

    private static Color brighter(Color color) {
        int red = Math.min(255, color.getRed() + 28);
        int green = Math.min(255, color.getGreen() + 28);
        int blue = Math.min(255, color.getBlue() + 28);
        return new Color(red, green, blue);
    }
}
