package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Ln extends UnaryOperation {
    public Ln() {
        super("ln");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        if (value <= 0d) {
            throw new CalculatorException("Invalid logarithm");
        }
        return Math.log(value);
    }
}
