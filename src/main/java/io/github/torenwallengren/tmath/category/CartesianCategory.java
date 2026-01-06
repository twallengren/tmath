package io.github.torenwallengren.tmath.category;

/**
 * A cartesian category: a category with finite products.
 *
 * A cartesian category has:
 * - A terminal object (nullary product)
 * - Binary products
 *
 * These structures satisfy coherence laws relating products and terminal objects.
 * Cartesian categories are fundamental in categorical logic and computer science,
 * as they correspond to simply-typed lambda calculus with products.
 *
 * @param <F> the witness type for the arrow family
 * @param <P> the witness type for the product carrier
 * @param <T> the witness type for the terminal object carrier
 */
public interface CartesianCategory<F, P, T>
        extends HasTerminal<F, T>, HasBinaryProducts<F, P> {
}
