package io.github.torenwallengren.tmath.category;

/**
 * Represents a mathematical category using higher-kinded type emulation.
 *
 * A category consists of:
 * - A collection of objects (represented by Java types A, B, C, ...)
 * - A family of morphisms between objects (represented by K2<F, A, B>)
 * - An identity morphism for each object
 * - A composition operation for morphisms
 *
 * The composition must satisfy the category laws:
 * - Associativity: h ∘ (g ∘ f) = (h ∘ g) ∘ f
 * - Identity: id_B ∘ f = f and g ∘ id_A = g for f: A → B, g: B → C
 *
 * @param <F> the witness type for the morphism family in this category
 */
public interface Category<F> {

    /**
     * Returns the identity morphism for type A.
     * The identity morphism id_A: A → A satisfies:
     * - id_A ∘ f = f for any f: X → A
     * - g ∘ id_A = g for any g: A → Y
     *
     * @param <A> the object type
     * @return the identity morphism id: A → A
     */
    <A> K2<F, A, A> id();

    /**
     * Composes two morphisms in this category.
     * Given f: A → B and g: B → C, returns g ∘ f: A → C
     *
     * Composition is applied in diagrammatic order (right to left):
     * compose(g, f) means "first apply f, then apply g"
     *
     * @param <A> the source type
     * @param <B> the intermediate type
     * @param <C> the target type
     * @param g the second morphism (g: B → C)
     * @param f the first morphism (f: A → B)
     * @return the composed morphism (g ∘ f: A → C)
     */
    <A, B, C> K2<F, A, C> compose(K2<F, B, C> g, K2<F, A, B> f);
}
