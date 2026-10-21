package op.functions;

import model.AngleMode;
import op.UnaryOperation;

public final class Cos extends UnaryOperation {
    public Cos() {
        super("cos");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        return Math.cos(mode.toRadians(value));
    }
}
