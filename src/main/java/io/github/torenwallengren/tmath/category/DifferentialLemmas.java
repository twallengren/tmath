package io.github.torenwallengren.tmath.category;

/**
 * Generic constructions and lemmas for differential categories.
 *
 * These constructions are written once against the abstract
 * DifferentialCategory interface and work for ANY differential category
 * (forward-mode, reverse-mode, symbolic, etc.).
 *
 * This demonstrates the power of categorical abstraction:
 * define once, use in any implementation.
 */
public final class DifferentialLemmas {

    private DifferentialLemmas() {
        // Utility class
    }

    /**
     * The square function: square(x) = x * x
     *
     * Built categorically as: mul ∘ diagonal
     * where diagonal(x) = (x, x)
     *
     * The derivative is 2x, which emerges from the product rule:
     * d/dx[x * x] = 1*x + x*1 = 2x
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <R> the scalar witness
     * @param cat the differential category
     * @return the square morphism: R → R
     */
    public static <F, P, T, R> K2<F, K0<R>, K0<R>> square(DifferentialCategory<F, P, T, R> cat) {
        // diagonal: R → R × R
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> diag = CartesianLemmas.diagonal(cat);
        // mul: R × R → R
        K2<F, P2<P, K0<R>, K0<R>>, K0<R>> mul = cat.mul();
        // square = mul ∘ diagonal
        return cat.compose(mul, diag);
    }

    /**
     * The cube function: cube(x) = x * x * x
     *
     * Built as: mul ∘ (id × square) ∘ diagonal
     *
     * The derivative is 3x², emerging from chain/product rules.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <R> the scalar witness
     * @param cat the differential category
     * @return the cube morphism: R → R
     */
    public static <F, P, T, R> K2<F, K0<R>, K0<R>> cube(DifferentialCategory<F, P, T, R> cat) {
        // x * x²
        K2<F, K0<R>, K0<R>> sq = square(cat);
        K2<F, K0<R>, K0<R>> id = cat.id();
        // diagonal: R → R × R
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> diag = CartesianLemmas.diagonal(cat);
        // id × square: R × R → R × R
        K2<F, P2<P, K0<R>, K0<R>>, P2<P, K0<R>, K0<R>>> idTimesSq =
            CartesianLemmas.product(cat, id, sq);
        // mul: R × R → R
        K2<F, P2<P, K0<R>, K0<R>>, K0<R>> mul = cat.mul();
        // cube = mul ∘ (id × square) ∘ diagonal
        return cat.compose(mul, cat.compose(idTimesSq, diag));
    }

    /**
     * Addition with a constant: f(x) = x + c
     *
     * The derivative is 1 (constant rule: d/dx[c] = 0).
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <R> the scalar witness
     * @param cat the differential category
     * @param c the constant to add
     * @return the morphism x ↦ x + c
     */
    public static <F, P, T, R> K2<F, K0<R>, K0<R>> addConstant(
            DifferentialCategory<F, P, T, R> cat, double c) {
        // id: R → R
        K2<F, K0<R>, K0<R>> id = cat.id();
        // constant: R → R (factors through terminal)
        K2<F, K0<R>, K0<R>> constC = cat.constantFrom(c);
        // pair: R → R × R
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> paired = cat.pair(id, constC);
        // add ∘ pair
        return cat.compose(cat.add(), paired);
    }

    /**
     * Multiplication by a constant: f(x) = c * x
     *
     * The derivative is c (constant multiple rule).
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <R> the scalar witness
     * @param cat the differential category
     * @param c the constant multiplier
     * @return the morphism x ↦ c * x
     */
    public static <F, P, T, R> K2<F, K0<R>, K0<R>> mulConstant(
            DifferentialCategory<F, P, T, R> cat, double c) {
        K2<F, K0<R>, K0<R>> constC = cat.constantFrom(c);
        K2<F, K0<R>, K0<R>> id = cat.id();
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> paired = cat.pair(constC, id);
        return cat.compose(cat.mul(), paired);
    }

    /**
     * A polynomial: f(x) = ax² + bx + c
     *
     * Demonstrates combining square, multiplication, and addition.
     *
     * The derivative is 2ax + b.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <R> the scalar witness
     * @param cat the differential category
     * @param a coefficient of x²
     * @param b coefficient of x
     * @param c constant term
     * @return the quadratic polynomial morphism
     */
    public static <F, P, T, R> K2<F, K0<R>, K0<R>> quadratic(
            DifferentialCategory<F, P, T, R> cat, double a, double b, double c) {
        // ax²
        K2<F, K0<R>, K0<R>> sq = square(cat);
        K2<F, K0<R>, K0<R>> aSq = cat.compose(mulConstant(cat, a), sq);

        // bx
        K2<F, K0<R>, K0<R>> bx = mulConstant(cat, b);

        // ax² + bx
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> pair1 = cat.pair(aSq, bx);
        K2<F, K0<R>, K0<R>> axSqPlusBx = cat.compose(cat.add(), pair1);

        // (ax² + bx) + c
        K2<F, K0<R>, K0<R>> constC = cat.constantFrom(c);
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> pair2 = cat.pair(axSqPlusBx, constC);
        return cat.compose(cat.add(), pair2);
    }

    /**
     * The reciprocal function: f(x) = 1/x
     *
     * The derivative is -1/x².
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <R> the scalar witness
     * @param cat the differential category
     * @return the reciprocal morphism: R → R
     */
    public static <F, P, T, R> K2<F, K0<R>, K0<R>> reciprocal(DifferentialCategory<F, P, T, R> cat) {
        // 1/x = div(1, x)
        K2<F, K0<R>, K0<R>> one = cat.constantFrom(1.0);
        K2<F, K0<R>, K0<R>> id = cat.id();
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> paired = cat.pair(one, id);
        return cat.compose(cat.div(), paired);
    }

    /**
     * Difference of squares: f(x) = (x + a)(x - a) = x² - a²
     *
     * Built categorically using the factored form.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <R> the scalar witness
     * @param cat the differential category
     * @param a the constant
     * @return the morphism x ↦ (x+a)(x-a)
     */
    public static <F, P, T, R> K2<F, K0<R>, K0<R>> differenceOfSquares(
            DifferentialCategory<F, P, T, R> cat, double a) {
        // x + a
        K2<F, K0<R>, K0<R>> xPlusA = addConstant(cat, a);
        // x - a = x + (-a)
        K2<F, K0<R>, K0<R>> xMinusA = addConstant(cat, -a);
        // (x+a, x-a)
        K2<F, K0<R>, P2<P, K0<R>, K0<R>>> paired = cat.pair(xPlusA, xMinusA);
        // (x+a)(x-a)
        return cat.compose(cat.mul(), paired);
    }
}
