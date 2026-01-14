package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.DifferentialCategory;
import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.P2;

/**
 * Reverse-mode automatic differentiation using tapes and backpropagation.
 *
 * More efficient than forward mode for functions R^n → R (many inputs,
 * one output) because a single backward pass computes all partial derivatives.
 *
 * The category-theoretic view: reverse mode uses continuation-passing style,
 * where the derivative "pulls back" cotangent vectors through the computation.
 */
public final class ReverseModeCategory
        implements DifferentialCategory<TapeFn.Mu, TapePair.Mu, TapeUnit.Mu, TapeVar.Mu> {

    private static final ReverseModeCategory INSTANCE = new ReverseModeCategory();

    private ReverseModeCategory() {
        // Singleton
    }

    public static ReverseModeCategory instance() {
        return INSTANCE;
    }

    // Category methods

    @Override
    public <A> K2<TapeFn.Mu, A, A> id() {
        return TapeFn.identity();
    }

    @Override
    public <A, B, C> K2<TapeFn.Mu, A, C> compose(K2<TapeFn.Mu, B, C> g, K2<TapeFn.Mu, A, B> f) {
        TapeFn<B, C> gFn = (TapeFn<B, C>) g;
        TapeFn<A, B> fFn = (TapeFn<A, B>) f;
        return fFn.andThen(gFn);
    }

    // Terminal object methods

    @Override
    public <A> K2<TapeFn.Mu, A, K0<TapeUnit.Mu>> terminate() {
        return TapeFn.of(a -> TapeUnit.INSTANCE, "!");
    }

    // Binary product methods

    @Override
    @SuppressWarnings("unchecked")
    public <A, B> K2<TapeFn.Mu, P2<TapePair.Mu, A, B>, A> fst() {
        return TapeFn.of(p -> ((TapePair<A, B>) p).fst(), "π₁");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A, B> K2<TapeFn.Mu, P2<TapePair.Mu, A, B>, B> snd() {
        return TapeFn.of(p -> ((TapePair<A, B>) p).snd(), "π₂");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X, A, B> K2<TapeFn.Mu, X, P2<TapePair.Mu, A, B>> pair(
            K2<TapeFn.Mu, X, A> f, K2<TapeFn.Mu, X, B> g) {
        TapeFn<X, A> ff = (TapeFn<X, A>) f;
        TapeFn<X, B> gg = (TapeFn<X, B>) g;
        String name = (ff.name() != null && gg.name() != null)
                ? "⟨" + ff.name() + ", " + gg.name() + "⟩"
                : "⟨f, g⟩";
        return TapeFn.of(x -> new TapePair<>(ff.apply(x), gg.apply(x)), name);
    }

    // Scalar operations

    @Override
    @SuppressWarnings("unchecked")
    public K2<TapeFn.Mu, P2<TapePair.Mu, K0<TapeVar.Mu>, K0<TapeVar.Mu>>, K0<TapeVar.Mu>> add() {
        return TapeFn.of(p -> {
            TapePair<?, ?> pair = (TapePair<?, ?>) p;
            TapeVar a = (TapeVar) pair.fst();
            TapeVar b = (TapeVar) pair.snd();
            return a.add(b);
        }, "+");
    }

    @Override
    @SuppressWarnings("unchecked")
    public K2<TapeFn.Mu, P2<TapePair.Mu, K0<TapeVar.Mu>, K0<TapeVar.Mu>>, K0<TapeVar.Mu>> mul() {
        return TapeFn.of(p -> {
            TapePair<?, ?> pair = (TapePair<?, ?>) p;
            TapeVar a = (TapeVar) pair.fst();
            TapeVar b = (TapeVar) pair.snd();
            return a.mul(b);
        }, "*");
    }

    @Override
    @SuppressWarnings("unchecked")
    public K2<TapeFn.Mu, K0<TapeVar.Mu>, K0<TapeVar.Mu>> neg() {
        return TapeFn.of(v -> ((TapeVar) v).neg(), "-");
    }

    @Override
    @SuppressWarnings("unchecked")
    public K2<TapeFn.Mu, P2<TapePair.Mu, K0<TapeVar.Mu>, K0<TapeVar.Mu>>, K0<TapeVar.Mu>> div() {
        return TapeFn.of(p -> {
            TapePair<?, ?> pair = (TapePair<?, ?>) p;
            TapeVar a = (TapeVar) pair.fst();
            TapeVar b = (TapeVar) pair.snd();
            return a.div(b);
        }, "/");
    }

    // Constants - these need a tape, so we use a factory pattern

    @Override
    public K2<TapeFn.Mu, K0<TapeUnit.Mu>, K0<TapeVar.Mu>> zero() {
        // This returns a morphism that creates a zero constant on whatever tape is active
        // For now, throw - constants need special handling in reverse mode
        throw new UnsupportedOperationException(
            "Constants in reverse mode require a tape context. " +
            "Use derivativeAt() which manages the tape automatically."
        );
    }

    @Override
    public K2<TapeFn.Mu, K0<TapeUnit.Mu>, K0<TapeVar.Mu>> one() {
        throw new UnsupportedOperationException(
            "Constants in reverse mode require a tape context. " +
            "Use derivativeAt() which manages the tape automatically."
        );
    }

    @Override
    public K2<TapeFn.Mu, K0<TapeUnit.Mu>, K0<TapeVar.Mu>> constant(double value) {
        throw new UnsupportedOperationException(
            "Constants in reverse mode require a tape context. " +
            "Use derivativeAt() which manages the tape automatically."
        );
    }

    // Differential combinator

    @Override
    public <A, B> K2<TapeFn.Mu, P2<TapePair.Mu, A, A>, P2<TapePair.Mu, B, B>> D(K2<TapeFn.Mu, A, B> f) {
        // For reverse mode, D is more complex as it involves tape construction
        throw new UnsupportedOperationException(
            "D combinator in reverse mode is implicit in the tape. " +
            "Use derivativeAt() or gradientAt() for actual differentiation."
        );
    }

    /**
     * Compute the derivative of a scalar function at a point.
     *
     * This is the primary interface for reverse-mode AD:
     * 1. Create a tape
     * 2. Create input variable on tape
     * 3. Evaluate function (builds computation graph)
     * 4. Backpropagate to compute gradient
     * 5. Return gradient for input variable
     */
    @Override
    public double derivativeAt(K2<TapeFn.Mu, K0<TapeVar.Mu>, K0<TapeVar.Mu>> f, double at) {
        Tape tape = new Tape();
        TapeVar input = tape.variable(at);

        @SuppressWarnings({"unchecked", "rawtypes"})
        TapeFn<TapeVar, TapeVar> fn = (TapeFn) f;
        TapeVar output = fn.apply(input);

        double[] gradients = tape.backpropagate(output.index());
        return gradients[input.index()];
    }

    /**
     * Compute gradients for a multivariate function.
     *
     * Efficient for functions f: R^n → R because a single backward
     * pass computes all n partial derivatives.
     */
    @Override
    public double[] gradientAt(K2<TapeFn.Mu, ?, K0<TapeVar.Mu>> f, double[] at) {
        Tape tape = new Tape();
        TapeVar[] inputs = new TapeVar[at.length];
        for (int i = 0; i < at.length; i++) {
            inputs[i] = tape.variable(at[i]);
        }

        // Evaluate the function with the inputs
        // This requires the function to work with TapeVar arrays
        // For now, throw - full multivariate support needs more infrastructure
        throw new UnsupportedOperationException(
            "Full multivariate gradient support not yet implemented. " +
            "Use derivativeAt for single-variable functions."
        );
    }

    @Override
    public String toString() {
        return "ReverseMode";
    }
}
