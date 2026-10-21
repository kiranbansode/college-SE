package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Asin extends UnaryOperation {
    public Asin() {
        super("asin");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        if (value < -1d || value > 1d) {
            throw new CalculatorException("Invalid inverse sine");
        }
        return mode.fromRadians(Math.asin(value));
    }
}
