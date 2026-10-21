package model;

/**
 * Trig functions read this to decide whether the angle is degrees or radians.
 */
public enum AngleMode {
    DEGREES,
    RADIANS;

    public double toRadians(double angle) {
        if (this == DEGREES) {
            return Math.toRadians(angle);
        }
        return angle;
    }

    public double fromRadians(double radians) {
        if (this == DEGREES) {
            return Math.toDegrees(radians);
        }
        return radians;
    }

    public AngleMode other() {
        if (this == DEGREES) {
            return RADIANS;
        }
        return DEGREES;
    }

    public String label() {
        if (this == DEGREES) {
            return "DEG";
        }
        return "RAD";
    }
}
