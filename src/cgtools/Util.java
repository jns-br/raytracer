package cgtools;

import static java.lang.Math.*;

public class Util {

    public static final double EPSILON = 1E-3;

    public static final boolean isZero(double a) {
        return abs(a) < EPSILON;
    }

    public static final boolean areEqual(double a, double b) {
        return isZero(a - b);
    }
}