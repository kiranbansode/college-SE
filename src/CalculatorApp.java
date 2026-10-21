import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.CalculatorFrame;

/**
 * Scientific Calculator by Kiran Bansode.
 *
 * From the project folder:
 * javac -d out -sourcepath src src/CalculatorApp.java
 * java -cp out CalculatorApp
 */
public class CalculatorApp {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // The buttons paint themselves, so a missing look and feel is harmless.
            }
            CalculatorFrame frame = new CalculatorFrame();
            frame.setVisible(true);
        });
    }
}
