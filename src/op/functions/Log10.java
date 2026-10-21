package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Log10 extends UnaryOperation {
    public Log10() {
        super("log");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        if (value <= 0d) {
            throw new CalculatorException("Invalid logarithm");
        }
        return Math.log10(value);
    }
}
