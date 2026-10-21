package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 * Button grid. Each button sends a command string to the listener.
 */
public class KeypadPanel extends JPanel {
    private static final Color NUMBER = new Color(0x2C2C2E);
    private static final Color FUNCTION = new Color(0x505052);
    private static final Color OPERATOR = new Color(0xFF9F0A);
    private static final Color UTILITY = new Color(0x636366);

    private static final String[][] KEYS = {
        {"sin", "cos", "tan", "(", ")"},
        {"asin", "acos", "atan", "^", "sqrt"},
        {"log", "ln", "sqr", "!", "inv"},
        {"pi", "e", "abs", "%", "+/-"},
        {"7", "8", "9", "/", "="},
        {"4", "5", "6", "*", "bksp"},
        {"1", "2", "3", "-", "AC"},
        {"0", "00", ".", "+", "Ans"}
    };

    public KeypadPanel(KeypadListener listener) {
        setOpaque(false);
        setLayout(new GridLayout(KEYS.length, KEYS[0].length, 8, 8));
        for (int row = 0; row < KEYS.length; row++) {
            for (int col = 0; col < KEYS[row].length; col++) {
                String command = KEYS[row][col];
                String label = labelFor(command);
                RoundButton button = new RoundButton(label, colorFor(command));
                button.setFont(new Font("Segoe UI", Font.BOLD, fontSize(label)));
                button.setPreferredSize(new Dimension(72, 52));
                button.setToolTipText(tipFor(command));
                button.addActionListener(event -> listener.onCommand(command));
                add(button);
            }
        }
    }

    private static String labelFor(String command) {
        if ("sqrt".equals(command)) {
            return "\u221a";
        }
        if ("sqr".equals(command)) {
            return "x\u00b2";
        }
        if ("!".equals(command)) {
            return "n!";
        }
        if ("inv".equals(command)) {
            return "1/x";
        }
        if ("pi".equals(command)) {
            return "\u03c0";
        }
        if ("+/-".equals(command)) {
            return "\u00b1";
        }
        if ("bksp".equals(command)) {
            return "\u232b";
        }
        if ("/".equals(command)) {
            return "\u00f7";
        }
        if ("*".equals(command)) {
            return "\u00d7";
        }
        if ("-".equals(command)) {
            return "\u2212";
        }
        if ("^".equals(command)) {
            return "x^y";
        }
        if ("abs".equals(command)) {
            return "|x|";
        }
        return command;
    }

    private static Color colorFor(String command) {
        if (isNumber(command)) {
            return NUMBER;
        }
        if ("+".equals(command) || "-".equals(command) || "*".equals(command)
                || "/".equals(command) || "=".equals(command)) {
            return OPERATOR;
        }
        if ("AC".equals(command) || "bksp".equals(command)) {
            return UTILITY;
        }
        return FUNCTION;
    }

    private static boolean isNumber(String command) {
        if (".".equals(command) || "00".equals(command)) {
            return true;
        }
        return command.length() == 1 && Character.isDigit(command.charAt(0));
    }

    private static int fontSize(String label) {
        if (label.length() >= 4) {
            return 15;
        }
        if (label.length() == 3) {
            return 16;
        }
        return 18;
    }

    private static String tipFor(String command) {
        if ("sin".equals(command)) {
            return "Sine";
        }
        if ("cos".equals(command)) {
            return "Cosine";
        }
        if ("tan".equals(command)) {
            return "Tangent";
        }
        if ("asin".equals(command)) {
            return "Inverse sine";
        }
        if ("acos".equals(command)) {
            return "Inverse cosine";
        }
        if ("atan".equals(command)) {
            return "Inverse tangent";
        }
        if ("log".equals(command)) {
            return "Log base 10";
        }
        if ("ln".equals(command)) {
            return "Natural log";
        }
        if ("sqrt".equals(command)) {
            return "Square root";
        }
        if ("sqr".equals(command)) {
            return "Square";
        }
        if ("!".equals(command)) {
            return "Factorial";
        }
        if ("inv".equals(command)) {
            return "Reciprocal";
        }
        if ("pi".equals(command)) {
            return "Pi";
        }
        if ("e".equals(command)) {
            return "Euler's number";
        }
        if ("abs".equals(command)) {
            return "Absolute value";
        }
        if ("%".equals(command)) {
            return "Divide this value by 100";
        }
        if ("+/-".equals(command)) {
            return "Change sign";
        }
        if ("bksp".equals(command)) {
            return "Backspace";
        }
        if ("AC".equals(command)) {
            return "All clear";
        }
        if ("Ans".equals(command)) {
            return "Insert the last answer";
        }
        if ("^".equals(command)) {
            return "Power";
        }
        return null;
    }
}
