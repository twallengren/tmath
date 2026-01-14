package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.P2;

/**
 * A binary product for the forward-mode AD category.
 *
 * This is analogous to Pair but used within the dual number category
 * structure. It holds two components which may themselves be Dual
 * numbers or other types.
 *
 * @param <A> the type of the first component
 * @param <B> the type of the second component
 */
public record DualPair<A, B>(A fst, B snd) implements P2<DualPair.Mu, A, B> {

    /**
     * Witness type for the DualPair product carrier.
     * Used as the P parameter in HasBinaryProducts<F, P>.
     */
    public static final class Mu {}

    /**
     * Creates a new dual pair with the given components.
     *
     * @param <A> the type of the first component
     * @param <B> the type of the second component
     * @param fst the first component
     * @param snd the second component
     * @return a new pair (fst, snd)
     */
    public static <A, B> DualPair<A, B> of(A fst, B snd) {
        return new DualPair<>(fst, snd);
    }

    @Override
    public String toString() {
        return "(" + fst + ", " + snd + ")";
    }
}
