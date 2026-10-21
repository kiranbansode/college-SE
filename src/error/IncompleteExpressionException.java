package error;

/**
 * The expression is still being typed, so it cannot be evaluated yet.
 */
public class IncompleteExpressionException extends CalculatorException {
    public IncompleteExpressionException(String message) {
        super(message);
    }
}
