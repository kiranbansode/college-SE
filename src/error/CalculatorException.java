package error;

/**
 * A problem the user can see on the display, such as divide by zero.
 */
public class CalculatorException extends RuntimeException {
    public CalculatorException(String message) {
        super(message);
    }
}
