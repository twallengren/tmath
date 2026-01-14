package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.K0;

/**
 * The terminal object for the reverse-mode AD category.
 */
public record TapeUnit() implements K0<TapeUnit.Mu> {

    /**
     * Witness type for the TapeUnit terminal object.
     */
    public static final class Mu {}

    public static final TapeUnit INSTANCE = new TapeUnit();
}
