package op;

import model.AngleMode;

/**
 * Shared base for functions that take one number, such as sin or square root.
 */
public abstract class UnaryOperation implements Operation {
    private final String name;

    protected UnaryOperation(String name) {
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final int arity() {
        return 1;
    }

    @Override
    public final double apply(double[] args, AngleMode mode) {
        if (args == null || args.length != 1) {
            throw new IllegalArgumentException(name + " expects one value");
        }
        return evaluate(args[0], mode);
    }

    protected abstract double evaluate(double value, AngleMode mode);
}
