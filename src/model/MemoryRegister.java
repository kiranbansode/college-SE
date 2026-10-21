package model;

/**
 * One memory cell for MC, MR, M+ and M-.
 */
public final class MemoryRegister {
    private double value;

    public void clear() {
        value = 0;
    }

    public void add(double amount) {
        value += amount;
    }

    public void subtract(double amount) {
        value -= amount;
    }

    public double recall() {
        return value;
    }

    public boolean hasValue() {
        return value != 0d;
    }
}
