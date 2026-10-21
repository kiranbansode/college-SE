package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Sqrt extends UnaryOperation {
    public Sqrt() {
        super("sqrt");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        if (value < 0d) {
            throw new CalculatorException("Invalid square root");
        }
        return Math.sqrt(value);
    }
}
