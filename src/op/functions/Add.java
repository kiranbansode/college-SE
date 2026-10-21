package op.functions;

import model.AngleMode;
import op.BinaryOperation;

public final class Add extends BinaryOperation {
    public Add() {
        super("+");
    }

    @Override
    protected double evaluate(double left, double right, AngleMode mode) {
        return left + right;
    }
}
