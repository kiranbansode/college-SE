package op.functions;

import error.CalculatorException;
import model.AngleMode;
import op.UnaryOperation;

public final class Factorial extends UnaryOperation {
    public Factorial() {
        super("fact");
    }

    @Override
    protected double evaluate(double value, AngleMode mode) {
        if (value < 0d || Math.abs(value - Math.rint(value)) > 1e-9 || value > 170d) {
            throw new CalculatorException("Invalid factorial");
        }
        int count = (int) Math.rint(value);
        double product = 1d;
        for (int i = 2; i <= count; i++) {
            product *= i;
        }
        return product;
    }
}
