package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.P2;

/**
 * A binary product for the reverse-mode AD category.
 */
public record TapePair<A, B>(A fst, B snd) implements P2<TapePair.Mu, A, B> {

    /**
     * Witness type for the TapePair product carrier.
     */
    public static final class Mu {}

    public static <A, B> TapePair<A, B> of(A fst, B snd) {
        return new TapePair<>(fst, snd);
    }

    @Override
    public String toString() {
        return "(" + fst + ", " + snd + ")";
    }
}
