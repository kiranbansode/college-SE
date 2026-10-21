package eval;

import error.CalculatorException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Formats a result so whole numbers stay short and huge numbers use powers of ten.
 */
public final class NumberFormatter {
    private NumberFormatter() {
    }

    public static String format(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new CalculatorException("Math error");
        }
        if (Math.abs(value) < 1e-12) {
            return "0";
        }
        double abs = Math.abs(value);
        if (abs >= 1e12 || abs < 1e-6) {
            return scientific(value);
        }
        BigDecimal decimal = new BigDecimal(Double.toString(value));
        decimal = decimal.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros();
        String text = decimal.toPlainString();
        if ("-0".equals(text)) {
            return "0";
        }
        return text;
    }

    private static String scientific(double value) {
        String raw = String.format(Locale.US, "%.8E", value);
        int marker = raw.indexOf('E');
        String mantissa = raw.substring(0, marker);
        String exponent = raw.substring(marker + 1);
        if (mantissa.indexOf('.') >= 0) {
            mantissa = mantissa.replaceAll("0+$", "").replaceAll("\\.$", "");
        }
        int power = Integer.parseInt(exponent);
        return mantissa + "E" + power;
    }
}
