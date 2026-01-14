package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.K0;

/**
 * A dual number: a value paired with its derivative component.
 *
 * Dual numbers form the basis of forward-mode automatic differentiation.
 * The key insight is that arithmetic on dual numbers automatically computes
 * derivatives via the standard differentiation rules:
 *
 *   (a, a') + (b, b') = (a+b, a'+b')           [sum rule]
 *   (a, a') * (b, b') = (a*b, a'*b + a*b')     [product rule]
 *   (a, a') / (b, b') = (a/b, (a'*b - a*b')/b²) [quotient rule]
 *
 * To differentiate f(x), create a dual number (x, 1) representing x
 * with derivative 1 (since dx/dx = 1), evaluate f on this dual number,
 * and extract the derivative component of the result.
 *
 * @param value the primal (function) value
 * @param derivative the derivative (tangent) value
 */
public record Dual(double value, double derivative) implements K0<Dual.Mu> {

    /**
     * Witness type for the Dual scalar object carrier.
     * Used as the R parameter in HasScalars<F, P, R>.
     */
    public static final class Mu {}

    /**
     * Create a dual number representing a variable x.
     *
     * The derivative is 1 because dx/dx = 1.
     *
     * @param x the variable value
     * @return a dual number (x, 1)
     */
    public static Dual variable(double x) {
        return new Dual(x, 1.0);
    }

    /**
     * Create a dual number representing a constant c.
     *
     * The derivative is 0 because dc/dx = 0 for any constant.
     *
     * @param c the constant value
     * @return a dual number (c, 0)
     */
    public static Dual constant(double c) {
        return new Dual(c, 0.0);
    }

    /**
     * Add two dual numbers.
     *
     * (a, a') + (b, b') = (a+b, a'+b')
     *
     * @param other the dual number to add
     * @return the sum
     */
    public Dual add(Dual other) {
        return new Dual(
            value + other.value,
            derivative + other.derivative
        );
    }

    /**
     * Multiply two dual numbers.
     *
     * (a, a') * (b, b') = (a*b, a'*b + a*b')
     *
     * This is the product rule: (fg)' = f'g + fg'
     *
     * @param other the dual number to multiply
     * @return the product
     */
    public Dual mul(Dual other) {
        return new Dual(
            value * other.value,
            derivative * other.value + value * other.derivative
        );
    }

    /**
     * Negate a dual number.
     *
     * -(a, a') = (-a, -a')
     *
     * @return the negation
     */
    public Dual neg() {
        return new Dual(-value, -derivative);
    }

    /**
     * Subtract a dual number.
     *
     * (a, a') - (b, b') = (a-b, a'-b')
     *
     * @param other the dual number to subtract
     * @return the difference
     */
    public Dual sub(Dual other) {
        return add(other.neg());
    }

    /**
     * Divide two dual numbers.
     *
     * (a, a') / (b, b') = (a/b, (a'*b - a*b') / b²)
     *
     * This is the quotient rule: (f/g)' = (f'g - fg') / g²
     *
     * @param other the dual number to divide by
     * @return the quotient
     */
    public Dual div(Dual other) {
        double g2 = other.value * other.value;
        return new Dual(
            value / other.value,
            (derivative * other.value - value * other.derivative) / g2
        );
    }

    @Override
    public String toString() {
        return "(" + value + ", " + derivative + ")";
    }
}
