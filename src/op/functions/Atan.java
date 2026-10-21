package op.functions;

import model.AngleMode;
import op.UnaryOperation;

public final class Atan extends UnaryOperation {
    public Atan() {
        super("atan");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        return mode.fromRadians(Math.atan(value));
    }
}
