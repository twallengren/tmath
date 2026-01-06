package io.github.torenwallengren.tmath.category;

/**
 * A kind-2 type encoding: an arrow from A to B in arrow family F.
 *
 * This is a "higher-kinded type" emulation in Java. The type parameter F
 * is a witness type representing the family of arrows (morphisms) in a category.
 *
 * For example, if F = Fn.Mu, then K2<Fn.Mu, A, B> represents a function from A to B.
 *
 * @param <F> the witness type for the arrow family (category)
 * @param <A> the domain (source) type
 * @param <B> the codomain (target) type
 */
public interface K2<F, A, B> {
}
