package io.github.torenwallengren.tmath.category;

/**
 * A category with a distinguished scalar object and arithmetic operations.
 *
 * The scalar object R supports ring operations modeled as morphisms:
 * - add: R × R → R (addition)
 * - mul: R × R → R (multiplication)
 * - neg: R → R (negation)
 *
 * This is the foundation for numeric computation in categorical terms.
 * The arithmetic operations are morphisms, not external functions,
 * allowing them to be composed, differentiated, and transformed.
 *
 * @param <F> the witness type for the arrow family
 * @param <P> the witness type for the product carrier
 * @param <R> the witness type for the scalar object carrier
 */
public interface HasScalars<F, P, R> extends HasBinaryProducts<F, P> {

    /**
     * Addition morphism: add : R × R → R
     *
     * @return the addition morphism
     */
    K2<F, P2<P, K0<R>, K0<R>>, K0<R>> add();

    /**
     * Multiplication morphism: mul : R × R → R
     *
     * @return the multiplication morphism
     */
    K2<F, P2<P, K0<R>, K0<R>>, K0<R>> mul();

    /**
     * Negation morphism: neg : R → R
     *
     * @return the negation morphism
     */
    K2<F, K0<R>, K0<R>> neg();

    /**
     * Division morphism: div : R × R → R
     *
     * Note: This is a partial operation (undefined when divisor is zero).
     *
     * @return the division morphism
     */
    K2<F, P2<P, K0<R>, K0<R>>, K0<R>> div();

    /**
     * Subtraction morphism: sub = add ∘ (id × neg)
     *
     * Derived from add and neg.
     *
     * @return the subtraction morphism
     */
    default K2<F, P2<P, K0<R>, K0<R>>, K0<R>> sub() {
        // sub(a, b) = add(a, neg(b)) = add ∘ ⟨π₁, neg ∘ π₂⟩
        K2<F, P2<P, K0<R>, K0<R>>, K0<R>> pi1 = fst();
        K2<F, P2<P, K0<R>, K0<R>>, K0<R>> pi2 = snd();
        K2<F, P2<P, K0<R>, K0<R>>, K0<R>> negPi2 = compose(neg(), pi2);
        K2<F, P2<P, K0<R>, K0<R>>, P2<P, K0<R>, K0<R>>> paired = pair(pi1, negPi2);
        return compose(add(), paired);
    }
}
