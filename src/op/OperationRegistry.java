package op;

import error.CalculatorException;
import java.util.HashMap;
import java.util.Map;
import model.AngleMode;
import op.functions.Abs;
import op.functions.Acos;
import op.functions.Add;
import op.functions.Asin;
import op.functions.Atan;
import op.functions.Cos;
import op.functions.Divide;
import op.functions.Factorial;
import op.functions.Ln;
import op.functions.Log10;
import op.functions.Multiply;
import op.functions.Percent;
import op.functions.Power;
import op.functions.Reciprocal;
import op.functions.Sin;
import op.functions.Sqrt;
import op.functions.Square;
import op.functions.Subtract;
import op.functions.Tan;

/**
 * Looks up an operation by the name that appears in the expression.
 */
public final class OperationRegistry {
    private final Map<String, Operation> byName = new HashMap<String, Operation>();

    public OperationRegistry() {
        register(new Add());
        register(new Subtract());
        register(new Multiply());
        register(new Divide());
        register(new Power());
        register(new Sin());
        register(new Cos());
        register(new Tan());
        register(new Asin());
        register(new Acos());
        register(new Atan());
        register(new Log10());
        register(new Ln());
        register(new Sqrt());
        register(new Square());
        register(new Reciprocal());
        register(new Factorial());
        register(new Abs());
        register(new Percent());
    }

    public double unary(String name, double value, AngleMode mode) {
        return require(name, 1).apply(new double[] { value }, mode);
    }

    public double binary(String name, double left, double right, AngleMode mode) {
        return require(name, 2).apply(new double[] { left, right }, mode);
    }

    private void register(Operation operation) {
        byName.put(operation.name(), operation);
    }

    private Operation require(String name, int arity) {
        Operation operation = byName.get(name);
        if (operation == null || operation.arity() != arity) {
            throw new CalculatorException("Unknown function '" + name + "'");
        }
        return operation;
    }
}
