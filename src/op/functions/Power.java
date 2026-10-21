package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.BinaryOperation;

public final class Power extends BinaryOperation {
    public Power() {
        super("^");
    }

    @Override
    protected double evaluate(double left, double right, AngleMode mode) {
        double result = Math.pow(left, right);
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new CalculatorException("Invalid power");
        }
        return result;
    }
}
