package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.K0;

/**
 * A variable on the reverse-mode tape.
 *
 * TapeVar wraps an index into a Tape. Operations on TapeVars
 * automatically record themselves on the tape for later backpropagation.
 *
 * Unlike Dual (forward mode), TapeVar doesn't carry a derivative directly.
 * Instead, derivatives are computed during the backward pass.
 */
public record TapeVar(int index, Tape tape) implements K0<TapeVar.Mu> {

    /**
     * Witness type for TapeVar as a scalar object.
     */
    public static final class Mu {}

    /**
     * Get the value of this variable.
     */
    public double value() {
        return tape.getValue(index);
    }

    /**
     * Add two tape variables.
     * d(a+b)/da = 1, d(a+b)/db = 1
     */
    public TapeVar add(TapeVar other) {
        double result = value() + other.value();
        return tape.recordBinary(this, other, result, 1.0, 1.0);
    }

    /**
     * Multiply two tape variables.
     * d(a*b)/da = b, d(a*b)/db = a
     */
    public TapeVar mul(TapeVar other) {
        double result = value() * other.value();
        return tape.recordBinary(this, other, result, other.value(), value());
    }

    /**
     * Negate a tape variable.
     * d(-a)/da = -1
     */
    public TapeVar neg() {
        double result = -value();
        return tape.recordUnary(this, result, -1.0);
    }

    /**
     * Subtract a tape variable.
     * d(a-b)/da = 1, d(a-b)/db = -1
     */
    public TapeVar sub(TapeVar other) {
        double result = value() - other.value();
        return tape.recordBinary(this, other, result, 1.0, -1.0);
    }

    /**
     * Divide two tape variables.
     * d(a/b)/da = 1/b, d(a/b)/db = -a/b²
     */
    public TapeVar div(TapeVar other) {
        double a = value();
        double b = other.value();
        double result = a / b;
        double gradA = 1.0 / b;
        double gradB = -a / (b * b);
        return tape.recordBinary(this, other, result, gradA, gradB);
    }

    @Override
    public String toString() {
        return "TapeVar[" + index + "]=" + value();
    }
}
