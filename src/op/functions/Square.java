package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Square extends UnaryOperation {
    public Square() {
        super("sqr");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        double result = value * value;
        if (Double.isInfinite(result)) {
            throw new CalculatorException("Math error");
        }
        return result;
    }
}
