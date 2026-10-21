package op;

import model.AngleMode;

/**
 * One calculator operation. The engine calls apply without knowing which
 * function it is (polymorphism).
 */
public interface Operation {
    String name();

    int arity();

    double apply(double[] args, AngleMode mode);
}
