package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.DifferentialCategory;
import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.P2;

/**
 * Forward-mode automatic differentiation using dual numbers.
 *
 * In this category:
 * - Objects are types (like SetCategory)
 * - Morphisms are functions (DualFn)
 * - The scalar type is Dual (value + derivative)
 *
 * The key insight is that dual number arithmetic automatically computes
 * derivatives. When you evaluate f(x, 1) where (x, 1) is a dual number
 * representing "x with dx/dx = 1", the result (y, dy) gives both the
 * function value y = f(x) and the derivative dy = f'(x).
 *
 * The chain rule emerges automatically from composition:
 * if f produces (f(x), f'(x)) and g produces (g(y), g'(y)),
 * then g ∘ f produces (g(f(x)), g'(f(x)) · f'(x)) by dual number multiplication.
 *
 * This is efficient for functions with few outputs (computing directional
 * derivatives or when output dimension is small).
 */
public final class ForwardModeCategory
        implements DifferentialCategory<DualFn.Mu, DualPair.Mu, DualUnit.Mu, Dual.Mu> {

    private static final ForwardModeCategory INSTANCE = new ForwardModeCategory();

    private ForwardModeCategory() {
        // Singleton
    }

    /**
     * Returns the singleton instance of ForwardModeCategory.
     *
     * @return the forward-mode AD category instance
     */
    public static ForwardModeCategory instance() {
        return INSTANCE;
    }

    // Category methods

    @Override
    public <A> K2<DualFn.Mu, A, A> id() {
        return DualFn.identity();
    }

    @Override
    public <A, B, C> K2<DualFn.Mu, A, C> compose(K2<DualFn.Mu, B, C> g, K2<DualFn.Mu, A, B> f) {
        DualFn<B, C> gFn = (DualFn<B, C>) g;
        DualFn<A, B> fFn = (DualFn<A, B>) f;
        return fFn.andThen(gFn);
    }

    // Terminal object methods

    @Override
    public <A> K2<DualFn.Mu, A, K0<DualUnit.Mu>> terminate() {
        return DualFn.of(a -> DualUnit.INSTANCE, "!");
    }

    // Binary product methods

    @Override
    @SuppressWarnings("unchecked")
    public <A, B> K2<DualFn.Mu, P2<DualPair.Mu, A, B>, A> fst() {
        return DualFn.of(p -> ((DualPair<A, B>) p).fst(), "π₁");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A, B> K2<DualFn.Mu, P2<DualPair.Mu, A, B>, B> snd() {
        return DualFn.of(p -> ((DualPair<A, B>) p).snd(), "π₂");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X, A, B> K2<DualFn.Mu, X, P2<DualPair.Mu, A, B>> pair(
            K2<DualFn.Mu, X, A> f, K2<DualFn.Mu, X, B> g) {
        DualFn<X, A> ff = (DualFn<X, A>) f;
        DualFn<X, B> gg = (DualFn<X, B>) g;
        String name = (ff.name() != null && gg.name() != null)
                ? "⟨" + ff.name() + ", " + gg.name() + "⟩"
                : "⟨f, g⟩";
        return DualFn.of(x -> new DualPair<>(ff.apply(x), gg.apply(x)), name);
    }

    // Scalar arithmetic operations

    @Override
    @SuppressWarnings("unchecked")
    public K2<DualFn.Mu, P2<DualPair.Mu, K0<Dual.Mu>, K0<Dual.Mu>>, K0<Dual.Mu>> add() {
        return DualFn.of(p -> {
            DualPair<?, ?> pair = (DualPair<?, ?>) p;
            Dual a = (Dual) pair.fst();
            Dual b = (Dual) pair.snd();
            return a.add(b);
        }, "+");
    }

    @Override
    @SuppressWarnings("unchecked")
    public K2<DualFn.Mu, P2<DualPair.Mu, K0<Dual.Mu>, K0<Dual.Mu>>, K0<Dual.Mu>> mul() {
        return DualFn.of(p -> {
            DualPair<?, ?> pair = (DualPair<?, ?>) p;
            Dual a = (Dual) pair.fst();
            Dual b = (Dual) pair.snd();
            return a.mul(b);
        }, "*");
    }

    @Override
    @SuppressWarnings("unchecked")
    public K2<DualFn.Mu, K0<Dual.Mu>, K0<Dual.Mu>> neg() {
        return DualFn.of(d -> ((Dual) d).neg(), "-");
    }

    @Override
    @SuppressWarnings("unchecked")
    public K2<DualFn.Mu, P2<DualPair.Mu, K0<Dual.Mu>, K0<Dual.Mu>>, K0<Dual.Mu>> div() {
        return DualFn.of(p -> {
            DualPair<?, ?> pair = (DualPair<?, ?>) p;
            Dual a = (Dual) pair.fst();
            Dual b = (Dual) pair.snd();
            return a.div(b);
        }, "/");
    }

    // Constant operations

    @Override
    public K2<DualFn.Mu, K0<DualUnit.Mu>, K0<Dual.Mu>> zero() {
        return DualFn.of(u -> Dual.constant(0.0), "0");
    }

    @Override
    public K2<DualFn.Mu, K0<DualUnit.Mu>, K0<Dual.Mu>> one() {
        return DualFn.of(u -> Dual.constant(1.0), "1");
    }

    @Override
    public K2<DualFn.Mu, K0<DualUnit.Mu>, K0<Dual.Mu>> constant(double value) {
        return DualFn.of(u -> Dual.constant(value), String.valueOf(value));
    }

    // Differential combinator

    /**
     * The differential combinator for forward mode.
     *
     * For forward mode, D is essentially structure-preserving - the dual
     * number arithmetic already handles differentiation. This method
     * repackages types to show that differentiation maps:
     *   f: A → B  to  D[f]: (A × A) → (B × B)
     *
     * In practice for scalar functions, we use derivativeAt() which is
     * more convenient.
     */
    @Override
    public <A, B> K2<DualFn.Mu, P2<DualPair.Mu, A, A>, P2<DualPair.Mu, B, B>> D(K2<DualFn.Mu, A, B> f) {
        DualFn<A, B> fn = (DualFn<A, B>) f;
        return DualFn.of(tangentPair -> {
            // For now, return the same structure
            // Full implementation would thread tangent vectors
            @SuppressWarnings("unchecked")
            DualPair<A, A> pair = (DualPair<A, A>) tangentPair;
            B result = fn.apply(pair.fst());
            // Second component represents how tangent transforms
            B tangentResult = fn.apply(pair.snd());
            return new DualPair<>(result, tangentResult);
        }, "D[" + fn.name() + "]");
    }

    /**
     * Compute the derivative of a scalar function at a point.
     *
     * This is the primary way to use forward-mode AD:
     * 1. Create a dual number (x, 1) for the variable
     * 2. Evaluate the function to get (f(x), f'(x))
     * 3. Extract the derivative component
     */
    @Override
    public double derivativeAt(K2<DualFn.Mu, K0<Dual.Mu>, K0<Dual.Mu>> f, double at) {
        DualFn<Dual, Dual> fn = castToScalarFn(f);
        Dual input = Dual.variable(at);
        Dual output = fn.apply(input);
        return output.derivative();
    }

    /**
     * Helper to cast a scalar morphism to its concrete type.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private DualFn<Dual, Dual> castToScalarFn(K2<DualFn.Mu, K0<Dual.Mu>, K0<Dual.Mu>> f) {
        // The K0<Dual.Mu> is implemented by Dual at runtime
        return (DualFn) f;
    }

    @Override
    public String toString() {
        return "ForwardMode";
    }
}
