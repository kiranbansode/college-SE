package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.BinaryOperation;

public final class Divide extends BinaryOperation {
    public Divide() {
        super("/");
    }

    @Override
    protected double evaluate(double left, double right, AngleMode mode) {
        if (right == 0d) {
            throw new CalculatorException("Cannot divide by zero");
        }
        return left / right;
    }
}
