package op.functions;

import model.AngleMode;
import op.UnaryOperation;

public final class Abs extends UnaryOperation {
    public Abs() {
        super("abs");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        return Math.abs(value);
    }
}
