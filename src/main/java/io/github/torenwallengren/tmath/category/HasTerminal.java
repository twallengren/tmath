package io.github.torenwallengren.tmath.category;

/**
 * A category with a chosen terminal object.
 *
 * A terminal object 1 is characterized by the universal property:
 * for every object A, there exists a unique morphism A → 1.
 *
 * The witness type T identifies which terminal object carrier we're using
 * (e.g., Unit, Void, etc.).
 *
 * @param <F> the witness type for the arrow family
 * @param <T> the witness type for the terminal object carrier
 */
public interface HasTerminal<F, T> extends Category<F> {

    /**
     * Returns the unique morphism from A to the terminal object.
     *
     * This morphism satisfies the universal property: for any object A,
     * there is exactly one morphism A → 1, which ignores its input.
     *
     * @param <A> the source object type
     * @return the unique morphism A → 1
     */
    <A> K2<F, A, K0<T>> terminate();
}
