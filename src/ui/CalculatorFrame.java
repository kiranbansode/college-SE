package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import model.CalculatorEngine;

/**
 * Window. Buttons and keys are forwarded to the engine, then the display is refreshed.
 */
public class CalculatorFrame extends JFrame implements KeypadListener {
    private final CalculatorEngine engine = new CalculatorEngine();
    private final DisplayPanel display = new DisplayPanel(this);
    private final KeypadPanel keypad = new KeypadPanel(this);

    public CalculatorFrame() {
        super("Scientific Calculator by Kiran Bansode");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(new Color(0x1C1C1E));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.add(display, BorderLayout.NORTH);
        root.add(keypad, BorderLayout.CENTER);
        setContentPane(root);
        bindKeys();
        refresh();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    @Override
    public void onCommand(String command) {
        handle(command);
        refresh();
    }

    private void refresh() {
        display.setState(
                engine.getExpressionText(),
                engine.getResultText(),
                engine.isError(),
                engine.getAngleMode(),
                engine.hasMemory(),
                engine.getMemoryText());
    }

    private void handle(String command) {
        if (command.length() == 1 && Character.isDigit(command.charAt(0))) {
            engine.appendDigit(command.charAt(0));
            return;
        }
        if ("00".equals(command)) {
            engine.appendDigits("00");
            return;
        }
        if (".".equals(command)) {
            engine.appendDot();
            return;
        }
        if ("+".equals(command) || "-".equals(command) || "*".equals(command)
                || "/".equals(command) || "^".equals(command)) {
            engine.appendOperator(command.charAt(0));
            return;
        }
        if ("sin".equals(command) || "cos".equals(command) || "tan".equals(command)
                || "asin".equals(command) || "acos".equals(command) || "atan".equals(command)
                || "log".equals(command) || "ln".equals(command) || "sqrt".equals(command)
                || "abs".equals(command)) {
            engine.appendFunction(command);
            return;
        }
        if ("sqr".equals(command) || "inv".equals(command)) {
            engine.wrapLastValue(command);
            return;
        }
        if ("!".equals(command)) {
            engine.appendFactorial();
            return;
        }
        if ("%".equals(command)) {
            engine.appendPercent();
            return;
        }
        if ("+/-".equals(command)) {
            engine.toggleSign();
            return;
        }
        if ("pi".equals(command)) {
            engine.appendConstant("pi");
            return;
        }
        if ("e".equals(command)) {
            engine.appendConstant("e");
            return;
        }
        if ("(".equals(command) || ")".equals(command)) {
            engine.appendParen(command.charAt(0));
            return;
        }
        if ("bksp".equals(command)) {
            engine.backspace();
            return;
        }
        if ("AC".equals(command)) {
            engine.clear();
            return;
        }
        if ("=".equals(command)) {
            engine.equals();
            return;
        }
        if ("Ans".equals(command)) {
            engine.appendAnswer();
            return;
        }
        if ("angle".equals(command)) {
            engine.toggleAngleMode();
            return;
        }
        if ("mc".equals(command)) {
            engine.memoryClear();
            return;
        }
        if ("mr".equals(command)) {
            engine.memoryRecall();
            return;
        }
        if ("m+".equals(command)) {
            engine.memoryAdd();
            return;
        }
        if ("m-".equals(command)) {
            engine.memorySubtract();
        }
    }

    private void bindKeys() {
        bind(KeyEvent.VK_ENTER, "=");
        bind(KeyEvent.VK_BACK_SPACE, "bksp");
        bind(KeyEvent.VK_ESCAPE, "AC");
        bind(KeyEvent.VK_ADD, "+");
        bind(KeyEvent.VK_SUBTRACT, "-");
        bind(KeyEvent.VK_MULTIPLY, "*");
        bind(KeyEvent.VK_DIVIDE, "/");
        bind(KeyEvent.VK_DECIMAL, ".");
        for (int digit = 0; digit <= 9; digit++) {
            bind(KeyEvent.VK_0 + digit, Integer.toString(digit));
            bind(KeyEvent.VK_NUMPAD0 + digit, Integer.toString(digit));
        }
        bindChar('+', "+");
        bindChar('-', "-");
        bindChar('*', "*");
        bindChar('/', "/");
        bindChar('.', ".");
        bindChar('^', "^");
        bindChar('%', "%");
        bindChar('!', "!");
        bindChar('(', "(");
        bindChar(')', ")");
        bindChar('=', "=");
    }

    private void bind(int keyCode, String command) {
        String actionName = "key-" + keyCode;
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyCode, 0), actionName);
        getRootPane().getActionMap().put(actionName, new CommandAction(command));
    }

    private void bindChar(char character, String command) {
        String actionName = "char-" + (int) character;
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(character), actionName);
        getRootPane().getActionMap().put(actionName, new CommandAction(command));
    }

    private final class CommandAction extends AbstractAction {
        private final String command;

        private CommandAction(String command) {
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            onCommand(command);
        }
    }
}
