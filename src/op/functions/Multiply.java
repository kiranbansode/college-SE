package op.functions;

import model.AngleMode;
import op.BinaryOperation;

public final class Multiply extends BinaryOperation {
    public Multiply() {
        super("*");
    }

    @Override
    protected double evaluate(double left, double right, AngleMode mode) {
        return left * right;
    }
}
