package op.functions;

import model.AngleMode;
import op.UnaryOperation;

public final class Percent extends UnaryOperation {
    public Percent() {
        super("percent");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        return value / 100d;
    }
}
