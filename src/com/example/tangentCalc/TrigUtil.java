package com.example.tangentCalc;

/**
 * Utility class for calculating trigonometric functions and other utilities.
 * Provides methods for computing tangent, sine, cosine, and other functions.
 */
public final class TrigUtil {

    /**
     * Tolerance level for precision checks.
     */
    private static final double TOLERANCE = 1e-10;

    /**
     * Magic number for rounding precision.
     */
    private static final double MAGIC_NUMBER = 1e-15;

    /**
     * Maximum number of iterations for series approximations.
     */
    private static final int MAX_ITERATIONS = 15;

    /**
     * Value of PI (π) used in calculations.
     */
    private static final double PI = 3.141592653589793;

    /**
     * Conversion factor from degrees to radians.
     */
    private static final double DEGREE_TO_RAD_CONVERSION = PI / 180;

    /**
     * Factor used for scaling in the decimal part of a number.
     */
    private static final double DECIMAL_FACTOR = 10.0;

    /**
     * Private constructor to prevent instantiation.
     */
    private TrigUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Computes the tangent of the given angle.
     *
     * @param angle the angle in radians
     * @return the tangent of the angle, or {@code Double.NaN} if the tangent is undefined
     */
    public static double computeTangent(final double angle) {
        if (isTangentUndefined(angle)) {
            return Double.NaN;
        }

        double sinValue = computeSine(angle);
        double cosValue = computeCosine(angle);
        double tanValue = sinValue / cosValue;

        return roundValueToPrecision(tanValue, MAGIC_NUMBER);
    }

    /**
     * Checks if the tangent of the given angle is undefined.
     *
     * @param angle the angle in radians
     * @return true if the tangent is undefined (i.e., angle is an odd multiple of π/2), false otherwise
     */
    public static boolean isTangentUndefined(final double angle) {
        double halfPi = PI / 2;
        double remainder = mod(abs(angle), PI);
        return abs(remainder - halfPi) < MAGIC_NUMBER;
    }

    /**
     * Rounds the given value to the specified precision.
     *
     * @param value     the value to be rounded
     * @param precision the precision to which the value should be rounded
     * @return the rounded value
     */
    public static double roundValueToPrecision(final double value, final double precision) {
        if (value == 0) {
            return 0;
        }
        return Math.round(value / precision) * precision;
    }

    /**
     * Computes the sine of the given angle using a Taylor series approximation.
     *
     * @param angle the angle in radians
     * @return the sine of the angle
     */
    public static double computeSine(final double angle) {
        double term = angle;
        double sum = 0;
        int n = 1;

        for (int i = 1; i <= MAX_ITERATIONS; i++) {
            sum += term;
            term = -term * angle * angle / ((2 * n) * (2 * n + 1));
            n++;
        }

        return sum;
    }

    /**
     * Computes the cosine of the given angle using a Taylor series approximation.
     *
     * @param angle the angle in radians
     * @return the cosine of the angle
     */
    public static double computeCosine(final double angle) {
        double term = 1;
        double sum = 0;
        int n = 0;

        for (int i = 1; i <= MAX_ITERATIONS; i++) {
            sum += term;
            term = -term * angle * angle / ((2 * n + 1) * (2 * n + 2));
            n++;
        }

        return sum;
    }

    /**
     * Parses a string into a double value.
     *
     * @param s the string to be parsed
     * @return the parsed double value
     * @throws NumberFormatException if the string cannot be parsed as a double
     */
    public static double parseDouble(final String s) throws NumberFormatException {
        boolean negative = false;
        String stringValue = s;

        if (s.charAt(0) == '-') {
            negative = true;
            stringValue = s.substring(1);
        }

        double result = 0;
        double factor = 1;
        boolean decimal = false;
        for (char c : stringValue.toCharArray()) {
            if (c == '.') {
                if (decimal) {
                    throw new NumberFormatException("Invalid number format: multiple decimal points");
                }
                decimal = true;
            } else {
                if (c < '0' || c > '9') {
                    throw new NumberFormatException("Invalid character in number: " + c);
                }
                if (decimal) {
                    factor /= DECIMAL_FACTOR;
                    result += (c - '0') * factor;
                } else {
                    result = result * DECIMAL_FACTOR + (c - '0');
                }
            }
        }
        return negative ? -result : result;
    }

    /**
     * Computes the absolute value of the given number.
     *
     * @param value the number
     * @return the absolute value of the number
     */
    public static double abs(final double value) {
        return value < 0 ? -value : value;
    }

    /**
     * Computes the modulus of two numbers.
     *
     * @param a the dividend
     * @param b the divisor
     * @return the remainder of the division of {@code a} by {@code b}
     */
    public static double mod(final double a, final double b) {
        return a - b * (long) (a / b);
    }

    /**
     * Returns the value of PI.
     *
     * @return the value of PI
     */
    public static double pi() {
        return PI;
    }

    /**
     * Converts an angle from degrees to radians.
     *
     * @param angleInDegrees the angle in degrees
     * @return the angle in radians
     */
    public static double toRadians(final double angleInDegrees) {
        return angleInDegrees * DEGREE_TO_RAD_CONVERSION;
    }

    /**
     * Formats a result value to a string.
     *
     * @param value the result value
     * @return the formatted result string
     */
    public static String formatResult(final double value) {
        return isWholeNumber(value) ? String.format("%d", (long) value)
                : String.format("%.3f", value);
    }

    /**
     * Checks if a value is a whole number.
     *
     * @param value the value
     * @return true if the value is a whole number, false otherwise
     */
    public static boolean isWholeNumber(final double value) {
        return abs(value - Math.round(value)) < TOLERANCE;
    }

    /**
     * Formats an angle value to a string.
     *
     * @param input the angle value as a string
     * @return the formatted angle string
     */
    public static String formatAngle(final String input) {
        try {
            double value = parseDouble(input);
            return isWholeNumber(value) ? String.format("%d", (long) value)
                    : String.format("%.3f", value);
        } catch (NumberFormatException e) {
            return input;
        }
    }
}
