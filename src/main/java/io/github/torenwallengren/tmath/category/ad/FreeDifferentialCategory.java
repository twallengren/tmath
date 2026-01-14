package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.DifferentialCategory;
import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.P2;
import io.github.torenwallengren.tmath.category.free.DiffTerm;

/**
 * The free differential category: symbolic automatic differentiation.
 *
 * In this category:
 * - Objects are type parameters (symbolic)
 * - Morphisms are DiffTerm syntax trees
 * - Composition builds Comp nodes
 * - The D combinator performs symbolic differentiation
 *
 * This allows:
 * - Inspecting the structure of derivatives before evaluation
 * - Simplification and optimization passes
 * - Symbolic manipulation of mathematical expressions
 *
 * The witness types P, T, R are placeholders - they don't correspond
 * to actual runtime types since everything is symbolic.
 *
 * @param <P> placeholder witness for products
 * @param <T> placeholder witness for terminal
 * @param <R> placeholder witness for scalars
 */
public final class FreeDifferentialCategory<P, T, R>
        implements DifferentialCategory<DiffTerm.Mu, P, T, R> {

    private static final FreeDifferentialCategory<?, ?, ?> INSTANCE = new FreeDifferentialCategory<>();

    private FreeDifferentialCategory() {
        // Singleton
    }

    /**
     * Returns the singleton instance.
     */
    @SuppressWarnings("unchecked")
    public static <P, T, R> FreeDifferentialCategory<P, T, R> instance() {
        return (FreeDifferentialCategory<P, T, R>) INSTANCE;
    }

    // Category methods

    @Override
    public <A> K2<DiffTerm.Mu, A, A> id() {
        return new DiffTerm.Id<>();
    }

    @Override
    public <A, B, C> K2<DiffTerm.Mu, A, C> compose(K2<DiffTerm.Mu, B, C> g, K2<DiffTerm.Mu, A, B> f) {
        DiffTerm<B, C> gTerm = (DiffTerm<B, C>) g;
        DiffTerm<A, B> fTerm = (DiffTerm<A, B>) f;

        // Simplify: id ∘ f = f, g ∘ id = g
        if (gTerm instanceof DiffTerm.Id<?>) {
            @SuppressWarnings("unchecked")
            K2<DiffTerm.Mu, A, C> result = (K2<DiffTerm.Mu, A, C>) f;
            return result;
        }
        if (fTerm instanceof DiffTerm.Id<?>) {
            @SuppressWarnings("unchecked")
            K2<DiffTerm.Mu, A, C> result = (K2<DiffTerm.Mu, A, C>) g;
            return result;
        }

        return new DiffTerm.Comp<>(gTerm, fTerm);
    }

    // Terminal object methods

    @Override
    public <A> K2<DiffTerm.Mu, A, K0<T>> terminate() {
        return new DiffTerm.Terminate<>();
    }

    // Binary product methods

    @Override
    public <A, B> K2<DiffTerm.Mu, P2<P, A, B>, A> fst() {
        return new DiffTerm.Fst<>();
    }

    @Override
    public <A, B> K2<DiffTerm.Mu, P2<P, A, B>, B> snd() {
        return new DiffTerm.Snd<>();
    }

    @Override
    public <X, A, B> K2<DiffTerm.Mu, X, P2<P, A, B>> pair(K2<DiffTerm.Mu, X, A> f, K2<DiffTerm.Mu, X, B> g) {
        DiffTerm<X, A> fTerm = (DiffTerm<X, A>) f;
        DiffTerm<X, B> gTerm = (DiffTerm<X, B>) g;
        return new DiffTerm.Pair<>(fTerm, gTerm);
    }

    // Scalar operations

    @Override
    public K2<DiffTerm.Mu, P2<P, K0<R>, K0<R>>, K0<R>> add() {
        return new DiffTerm.Add<>();
    }

    @Override
    public K2<DiffTerm.Mu, P2<P, K0<R>, K0<R>>, K0<R>> mul() {
        return new DiffTerm.Mul<>();
    }

    @Override
    public K2<DiffTerm.Mu, K0<R>, K0<R>> neg() {
        return new DiffTerm.Neg<>();
    }

    @Override
    public K2<DiffTerm.Mu, P2<P, K0<R>, K0<R>>, K0<R>> div() {
        return new DiffTerm.Div<>();
    }

    // Constants

    @Override
    public K2<DiffTerm.Mu, K0<T>, K0<R>> zero() {
        return new DiffTerm.Const<>(0.0);
    }

    @Override
    public K2<DiffTerm.Mu, K0<T>, K0<R>> one() {
        return new DiffTerm.Const<>(1.0);
    }

    @Override
    public K2<DiffTerm.Mu, K0<T>, K0<R>> constant(double value) {
        return new DiffTerm.Const<>(value);
    }

    // Differential combinator (symbolic differentiation)

    /**
     * Symbolic differentiation.
     *
     * For now, returns a placeholder showing that D was applied.
     * A full implementation would recursively apply differentiation rules:
     * - D[id] = id (on tangent bundle)
     * - D[g ∘ f] = D[g] ∘ D[f] (chain rule)
     * - D[+] = + on tangent components (linearity of addition)
     * - D[*] applies product rule
     * - etc.
     */
    @Override
    public <A, B> K2<DiffTerm.Mu, P2<P, A, A>, P2<P, B, B>> D(K2<DiffTerm.Mu, A, B> f) {
        // For now, return a structural placeholder
        // Full symbolic differentiation would transform the AST
        DiffTerm<A, B> term = (DiffTerm<A, B>) f;

        // Create identity on tangent bundle as a simple D
        @SuppressWarnings("unchecked")
        K2<DiffTerm.Mu, P2<P, A, A>, P2<P, B, B>> result =
            (K2<DiffTerm.Mu, P2<P, A, A>, P2<P, B, B>>) (Object) new DiffTerm.Id<>();

        return result;
    }

    /**
     * For symbolic mode, derivativeAt doesn't compute numerically.
     * Instead, use the symbolic structure directly.
     */
    @Override
    public double derivativeAt(K2<DiffTerm.Mu, K0<R>, K0<R>> f, double at) {
        throw new UnsupportedOperationException(
            "Symbolic differentiation produces terms, not values. " +
            "Use DiffTerm.toInfix() to see the symbolic derivative, " +
            "or interpret into ForwardModeCategory for numeric evaluation."
        );
    }

    /**
     * Get the symbolic term representation.
     */
    @SuppressWarnings("unchecked")
    public <A, B> DiffTerm<A, B> getTerm(K2<DiffTerm.Mu, A, B> f) {
        return (DiffTerm<A, B>) f;
    }

    @Override
    public String toString() {
        return "FreeDifferential";
    }
}
