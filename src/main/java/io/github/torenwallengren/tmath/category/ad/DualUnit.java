package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.K0;

/**
 * The terminal object for the forward-mode AD category.
 *
 * This is analogous to Unit - a singleton type with exactly one value.
 * It serves as the terminal object carrier for ForwardModeCategory.
 */
public record DualUnit() implements K0<DualUnit.Mu> {

    /**
     * Witness type for the DualUnit terminal object carrier.
     * Used as the T parameter in HasTerminal<F, T>.
     */
    public static final class Mu {}

    /**
     * The unique instance of DualUnit.
     * All functions to DualUnit return this value.
     */
    public static final DualUnit INSTANCE = new DualUnit();
}
