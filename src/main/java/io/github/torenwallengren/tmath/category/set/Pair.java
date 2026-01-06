package io.github.torenwallengren.tmath.category.set;

import io.github.torenwallengren.tmath.category.P2;

/**
 * A binary product in the category Set: an ordered pair of values.
 *
 * Pair<A, B> represents the cartesian product A × B. It has two
 * projection functions (fst and snd) and satisfies the universal
 * property of products.
 *
 * This serves as the chosen product carrier for SetCategory.
 *
 * @param <A> the type of the first component
 * @param <B> the type of the second component
 */
public record Pair<A, B>(A fst, B snd) implements P2<Pair.Mu, A, B> {

    /**
     * Witness type for the Pair product carrier.
     * Used as the P parameter in HasBinaryProducts<F, P>.
     */
    public static final class Mu {}

    /**
     * Creates a new pair with the given components.
     *
     * @param <A> the type of the first component
     * @param <B> the type of the second component
     * @param fst the first component
     * @param snd the second component
     * @return a new pair (fst, snd)
     */
    public static <A, B> Pair<A, B> of(A fst, B snd) {
        return new Pair<>(fst, snd);
    }

    @Override
    public String toString() {
        return "(" + fst + ", " + snd + ")";
    }
}
