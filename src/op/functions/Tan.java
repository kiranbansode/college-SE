package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Tan extends UnaryOperation {
    public Tan() {
        super("tan");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        double radians = mode.toRadians(value);
        if (Math.abs(Math.cos(radians)) < 1e-10) {
            throw new CalculatorException("Undefined");
        }
        return Math.sin(radians) / Math.cos(radians);
    }
}
