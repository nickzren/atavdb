package org.atavdb.service;

import org.atavdb.global.Data;

/**
 *
 * @author nick
 */
public class MathManager {

    public static double devide(double a, int b) {
        if (b == 0 || a == Data.DOUBLE_NA || b == Data.INTEGER_NA) {
            return Data.DOUBLE_NA;
        } else {
            return a / (double) b;
        }
    }

    public static float devide(float a, int b) {
        if (b == 0 || a == Data.FLOAT_NA || b == Data.INTEGER_NA) {
            return Data.FLOAT_NA;
        } else {
            return a / (float) b;
        }
    }

    public static float devide(float a, float b) {
        if (b == 0 || a == Data.FLOAT_NA || b == Data.FLOAT_NA) {
            return Data.FLOAT_NA;
        } else {
            return a / b;
        }
    }

    public static float devide(int a, int b) {
        if (b == 0 || a == Data.INTEGER_NA || b == Data.INTEGER_NA) {
            return Data.FLOAT_NA;
        } else {
            return (float) a / (float) b;
        }
    }

    public static float devide(short a, short b) {
        if (b == 0 || a == Data.SHORT_NA || b == Data.SHORT_NA) {
            return Data.FLOAT_NA;
        } else {
            return (float) a / (float) b;
        }
    }

    public static float relativeDiff(float a, float b) {
        float max = MathManager.max(a, b);
        float min = MathManager.min(a, b);

        return devide(max - min, min);
    }

    public static float abs(float a, float b) {
        if (b == Data.FLOAT_NA || b == Data.INTEGER_NA
                || a == Data.FLOAT_NA || a == Data.INTEGER_NA) {
            return Data.FLOAT_NA;
        } else {
            return Math.abs(a - b);
        }
    }

    public static float max(float a, float b) {
        if (a == Data.FLOAT_NA && b != Data.FLOAT_NA) {
            return b;
        } else if (a != Data.FLOAT_NA && b == Data.FLOAT_NA) {
            return a;
        }

        return Math.max(a, b);
    }

    public static int max(int a, int b) {
        if (a == Data.INTEGER_NA && b != Data.INTEGER_NA) {
            return b;
        } else if (a != Data.INTEGER_NA && b == Data.INTEGER_NA) {
            return a;
        }

        return Math.max(a, b);
    }

    public static int min(int a, int b) {
        if (a == Data.INTEGER_NA && b != Data.INTEGER_NA) {
            return b;
        } else if (a != Data.INTEGER_NA && b == Data.INTEGER_NA) {
            return a;
        }

        return Math.min(a, b);
    }

    public static float min(float a, float b) {
        if (a == Data.FLOAT_NA && b != Data.FLOAT_NA) {
            return b;
        } else if (a != Data.INTEGER_NA && b == Data.INTEGER_NA) {
            return a;
        }

        return Math.min(a, b);
    }
}
