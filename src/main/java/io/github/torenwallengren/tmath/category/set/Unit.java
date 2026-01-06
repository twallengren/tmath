package io.github.torenwallengren.tmath.category.set;

import io.github.torenwallengren.tmath.category.K0;

/**
 * The terminal object in the category Set.
 *
 * Unit is a singleton type with exactly one value (INSTANCE). In Set,
 * this makes it a terminal object because there is exactly one function
 * from any type A to Unit (the constant function returning INSTANCE).
 *
 * This serves as the chosen terminal object carrier for SetCategory.
 */
public record Unit() implements K0<Unit.Mu> {

    /**
     * Witness type for the Unit terminal object carrier.
     * Used as the T parameter in HasTerminal<F, T>.
     */
    public static final class Mu {}

    /**
     * The unique instance of Unit.
     * All functions to Unit return this value.
     */
    public static final Unit INSTANCE = new Unit();
}
