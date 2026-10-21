package op;

import model.AngleMode;

/**
 * Shared base for functions that take two numbers, such as add or power.
 */
public abstract class BinaryOperation implements Operation {
    private final String name;

    protected BinaryOperation(String name) {
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final int arity() {
        return 2;
    }

    @Override
    public final double apply(double[] args, AngleMode mode) {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException(name + " expects two values");
        }
        return evaluate(args[0], args[1], mode);
    }

    protected abstract double evaluate(double left, double right, AngleMode mode);
}
