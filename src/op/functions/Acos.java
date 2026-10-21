package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Acos extends UnaryOperation {
    public Acos() {
        super("acos");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        if (value < -1d || value > 1d) {
            throw new CalculatorException("Invalid inverse cosine");
        }
        return mode.fromRadians(Math.acos(value));
    }
}
