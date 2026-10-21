package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Reciprocal extends UnaryOperation {
    public Reciprocal() {
        super("inv");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        if (value == 0d) {
            throw new CalculatorException("Cannot divide by zero");
        }
        return 1d / value;
    }
}
