package io.github.torenwallengren.tmath.category;

/**
 * A category with constant morphisms (global elements) for scalars.
 *
 * Constants are modeled as morphisms from the terminal object:
 * a constant c : 1 → R can be composed with the unique morphism ! : A → 1
 * to get a "constant function" c ∘ ! : A → R.
 *
 * This provides zero, one, and arbitrary constants.
 *
 * @param <F> the witness type for the arrow family
 * @param <T> the witness type for the terminal object carrier
 * @param <R> the witness type for the scalar object carrier
 */
public interface HasConstants<F, T, R> extends HasTerminal<F, T> {

    /**
     * Zero constant: 0 : 1 → R
     *
     * The additive identity.
     *
     * @return the zero constant morphism
     */
    K2<F, K0<T>, K0<R>> zero();

    /**
     * One constant: 1 : 1 → R
     *
     * The multiplicative identity.
     *
     * @return the one constant morphism
     */
    K2<F, K0<T>, K0<R>> one();

    /**
     * Lift a double value to a constant morphism: c : 1 → R
     *
     * @param value the scalar value
     * @return a constant morphism representing that value
     */
    K2<F, K0<T>, K0<R>> constant(double value);

    /**
     * Create a constant morphism from any domain: A → R
     *
     * This factors through the terminal object: const_A(c) = c ∘ !
     *
     * @param <A> the domain type
     * @param value the scalar value
     * @return a constant morphism A → R
     */
    default <A> K2<F, A, K0<R>> constantFrom(double value) {
        K2<F, A, K0<T>> term = terminate();
        K2<F, K0<T>, K0<R>> c = constant(value);
        return compose(c, term);
    }
}
