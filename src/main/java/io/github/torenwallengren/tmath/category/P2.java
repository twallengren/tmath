package io.github.torenwallengren.tmath.category;

/**
 * A binary product carrier witness: a product of A and B using witness P.
 *
 * This represents a "picked" product construction in a category. The type
 * parameter P is a witness type that identifies which particular product
 * encoding we're using (e.g., Pair, tuple, etc.).
 *
 * For example, P2<Pair.Mu, String, Integer> represents a product of String
 * and Integer implemented as a Pair.
 *
 * @param <P> the witness type for the product encoding
 * @param <A> the first component type
 * @param <B> the second component type
 */
public interface P2<P, A, B> {
}
