package io.github.torenwallengren.tmath.category;

/**
 * A cartesian differential category: a category with a differential combinator.
 *
 * The differential combinator D transforms morphisms into their derivatives.
 * Given f: A → B, D[f]: (A × A) → (B × B) where:
 * - Input: (primal value, tangent vector)
 * - Output: (function value, derivative · tangent)
 *
 * Key axioms (the chain rule is fundamental):
 * <pre>
 * D[id] = id                           (identity)
 * D[g ∘ f] = D[g] ∘ D[f]               (chain rule / functoriality)
 * D[⟨f,g⟩] = ⟨D[f], D[g]⟩             (product rule)
 * D[π₁] = π₁, D[π₂] = π₂               (projections)
 * </pre>
 *
 * The chain rule `D[g ∘ f] = D[g] ∘ D[f]` shows that differentiation
 * is a functor, mapping morphisms to morphisms on tangent bundles.
 *
 * This interface unifies forward-mode, reverse-mode, and symbolic
 * differentiation under a common abstraction.
 *
 * @param <F> the witness type for the arrow family
 * @param <P> the witness type for the product carrier
 * @param <T> the witness type for the terminal object carrier
 * @param <R> the witness type for the scalar object carrier
 */
public interface DifferentialCategory<F, P, T, R>
        extends CartesianCategory<F, P, T>, HasScalars<F, P, R>, HasConstants<F, T, R> {

    /**
     * The differential combinator.
     *
     * Given f: A → B, returns D[f]: (A × A) → (B × B)
     * where input is (primal, tangent) and output is (value, derivative).
     *
     * For scalar functions f: R → R, this computes:
     *   D[f](x, dx) = (f(x), f'(x) · dx)
     *
     * The chain rule D[g ∘ f] = D[g] ∘ D[f] expresses that
     * differentiation respects composition (functoriality).
     *
     * @param <A> the domain type
     * @param <B> the codomain type
     * @param f the morphism to differentiate
     * @return the differentiated morphism on tangent bundles
     */
    <A, B> K2<F, P2<P, A, A>, P2<P, B, B>> D(K2<F, A, B> f);

    /**
     * Compute the derivative of a scalar function at a point.
     *
     * For f: R → R, returns f'(x).
     *
     * This is a convenience method that extracts the derivative
     * from D[f](x, 1).
     *
     * @param f the scalar morphism to differentiate
     * @param at the point to evaluate the derivative
     * @return the derivative value f'(at)
     */
    double derivativeAt(K2<F, K0<R>, K0<R>> f, double at);

    /**
     * Compute the gradient of a multivariate function.
     *
     * For f: R^n → R, returns ∇f at the given point.
     *
     * @param f the multivariate function
     * @param at the point to evaluate (length n)
     * @return the gradient vector (length n)
     */
    default double[] gradientAt(K2<F, ?, K0<R>> f, double[] at) {
        throw new UnsupportedOperationException(
            "Override in concrete implementation for gradient computation"
        );
    }
}
