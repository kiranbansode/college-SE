package eval;

/**
 * Turns the stored expression into the symbols shown on the display.
 */
public final class ExpressionFormatter {
    private ExpressionFormatter() {
    }

    public static String pretty(String expression) {
        if (expression == null || expression.length() == 0) {
            return "";
        }
        String text = expression.replace("sqrt(", "\u221a(");
        text = text.replace("pi", "\u03c0");
        text = text.replace("*", "\u00d7");
        text = text.replace("/", "\u00f7");
        text = text.replace("E", "\u00d710^");
        return text;
    }
}
