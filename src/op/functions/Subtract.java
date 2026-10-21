package op.functions;

import model.AngleMode;
import op.BinaryOperation;

public final class Subtract extends BinaryOperation {
    public Subtract() {
        super("-");
    }

    @Override
    protected double evaluate(double left, double right, AngleMode mode) {
        return left - right;
    }
}
