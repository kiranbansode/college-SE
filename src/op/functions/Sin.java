package op.functions;

import model.AngleMode;
import op.UnaryOperation;

public final class Sin extends UnaryOperation {
    public Sin() {
        super("sin");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        return Math.sin(mode.toRadians(value));
    }
}
