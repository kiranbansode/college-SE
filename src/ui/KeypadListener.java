package ui;

/**
 * A button press or key press. The keypad reports the command and does no math.
 */
public interface KeypadListener {
    void onCommand(String command);
}
