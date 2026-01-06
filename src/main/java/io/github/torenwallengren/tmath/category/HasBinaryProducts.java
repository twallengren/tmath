package io.github.torenwallengren.tmath.category;

/**
 * A category with chosen binary products.
 *
 * A binary product A × B is characterized by the universal property:
 * - Two projection morphisms: π₁: A×B → A and π₂: A×B → B
 * - For any object X with morphisms f: X → A and g: X → B,
 *   there exists a unique morphism ⟨f,g⟩: X → A×B such that:
 *   π₁ ∘ ⟨f,g⟩ = f and π₂ ∘ ⟨f,g⟩ = g
 *
 * The witness type P identifies which product carrier we're using
 * (e.g., Pair, Tuple2, etc.).
 *
 * @param <F> the witness type for the arrow family
 * @param <P> the witness type for the product carrier
 */
public interface HasBinaryProducts<F, P> extends Category<F> {

    /**
     * Returns the first projection morphism π₁: A×B → A
     *
     * @param <A> the first component type
     * @param <B> the second component type
     * @return the first projection π₁
     */
    <A, B> K2<F, P2<P, A, B>, A> fst();

    /**
     * Returns the second projection morphism π₂: A×B → B
     *
     * @param <A> the first component type
     * @param <B> the second component type
     * @return the second projection π₂
     */
    <A, B> K2<F, P2<P, A, B>, B> snd();

    /**
     * Given morphisms f: X → A and g: X → B, returns the unique
     * pairing morphism ⟨f,g⟩: X → A×B satisfying:
     * - π₁ ∘ ⟨f,g⟩ = f
     * - π₂ ∘ ⟨f,g⟩ = g
     *
     * @param <X> the source object type
     * @param <A> the first target component type
     * @param <B> the second target component type
     * @param f the morphism to the first component
     * @param g the morphism to the second component
     * @return the pairing morphism ⟨f,g⟩
     */
    <X, A, B> K2<F, X, P2<P, A, B>> pair(K2<F, X, A> f, K2<F, X, B> g);
}
