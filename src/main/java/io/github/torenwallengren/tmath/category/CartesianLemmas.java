package io.github.torenwallengren.tmath.category;

/**
 * Generic constructions and lemmas for cartesian categories.
 *
 * These constructions are written once against the abstract
 * CartesianCategory interface and work for ANY cartesian category
 * (symbolic Term, concrete Fn, matrices, etc.).
 *
 * This demonstrates the power of categorical abstraction:
 * prove/construct once, use everywhere.
 */
public final class CartesianLemmas {

    private CartesianLemmas() {
        // Utility class
    }

    /**
     * The diagonal morphism Δ_A : A → A×A
     * Defined as ⟨id_A, id_A⟩
     *
     * This duplicates an object into a pair with itself.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <A> the object type
     * @param C the cartesian category
     * @return the diagonal morphism Δ : A → A×A
     */
    public static <F, P, T, A> K2<F, A, P2<P, A, A>> diagonal(CartesianCategory<F, P, T> C) {
        return C.pair(C.id(), C.id());
    }

    /**
     * The swap morphism σ : A×B → B×A
     * Defined as ⟨π₂, π₁⟩
     *
     * This swaps the components of a product.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <A> the first component type
     * @param <B> the second component type
     * @param C the cartesian category
     * @return the swap morphism σ : A×B → B×A
     */
    public static <F, P, T, A, B> K2<F, P2<P, A, B>, P2<P, B, A>> swap(CartesianCategory<F, P, T> C) {
        return C.pair(C.snd(), C.fst());
    }

    /**
     * Left associator morphism α : (A×B)×C → A×(B×C)
     * Defined as ⟨π₁∘π₁, ⟨π₂∘π₁, π₂⟩⟩
     *
     * This reassociates nested products to the right.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <A> the first component type
     * @param <B> the second component type
     * @param <C> the third component type
     * @param cat the cartesian category
     * @return the left associator α : (A×B)×C → A×(B×C)
     */
    public static <F, P, T, A, B, C> K2<F, P2<P, P2<P, A, B>, C>, P2<P, A, P2<P, B, C>>> assocLeft(
            CartesianCategory<F, P, T> cat) {
        // π₁ : (A×B)×C → A×B
        K2<F, P2<P, P2<P, A, B>, C>, P2<P, A, B>> pi1 = cat.fst();
        // π₂ : (A×B)×C → C
        K2<F, P2<P, P2<P, A, B>, C>, C> pi2outer = cat.snd();

        // π₁∘π₁ : (A×B)×C → A
        K2<F, P2<P, A, B>, A> pi1inner = cat.fst();
        K2<F, P2<P, P2<P, A, B>, C>, A> fstComp = cat.compose(pi1inner, pi1);

        // π₂∘π₁ : (A×B)×C → B
        K2<F, P2<P, A, B>, B> pi2inner = cat.snd();
        K2<F, P2<P, P2<P, A, B>, C>, B> sndComp = cat.compose(pi2inner, pi1);

        // ⟨π₂∘π₁, π₂⟩ : (A×B)×C → B×C
        K2<F, P2<P, P2<P, A, B>, C>, P2<P, B, C>> innerPair = cat.pair(sndComp, pi2outer);

        // ⟨π₁∘π₁, ⟨π₂∘π₁, π₂⟩⟩ : (A×B)×C → A×(B×C)
        return cat.pair(fstComp, innerPair);
    }

    /**
     * Right associator morphism α⁻¹ : A×(B×C) → (A×B)×C
     * Defined as ⟨⟨π₁, π₁∘π₂⟩, π₂∘π₂⟩
     *
     * This reassociates nested products to the left.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <A> the first component type
     * @param <B> the second component type
     * @param <C> the third component type
     * @param cat the cartesian category
     * @return the right associator α⁻¹ : A×(B×C) → (A×B)×C
     */
    public static <F, P, T, A, B, C> K2<F, P2<P, A, P2<P, B, C>>, P2<P, P2<P, A, B>, C>> assocRight(
            CartesianCategory<F, P, T> cat) {
        // π₁ : A×(B×C) → A
        K2<F, P2<P, A, P2<P, B, C>>, A> pi1outer = cat.fst();
        // π₂ : A×(B×C) → B×C
        K2<F, P2<P, A, P2<P, B, C>>, P2<P, B, C>> pi2 = cat.snd();

        // π₁∘π₂ : A×(B×C) → B
        K2<F, P2<P, B, C>, B> pi1inner = cat.fst();
        K2<F, P2<P, A, P2<P, B, C>>, B> fstComp = cat.compose(pi1inner, pi2);

        // π₂∘π₂ : A×(B×C) → C
        K2<F, P2<P, B, C>, C> pi2inner = cat.snd();
        K2<F, P2<P, A, P2<P, B, C>>, C> sndComp = cat.compose(pi2inner, pi2);

        // ⟨π₁, π₁∘π₂⟩ : A×(B×C) → A×B
        K2<F, P2<P, A, P2<P, B, C>>, P2<P, A, B>> innerPair = cat.pair(pi1outer, fstComp);

        // ⟨⟨π₁, π₁∘π₂⟩, π₂∘π₂⟩ : A×(B×C) → (A×B)×C
        return cat.pair(innerPair, sndComp);
    }

    /**
     * Product of two morphisms: f×g : A×B → C×D
     * Defined as ⟨f∘π₁, g∘π₂⟩
     *
     * Applies f to the first component and g to the second.
     *
     * @param <F> the arrow family witness
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @param <A> the first source component type
     * @param <B> the second source component type
     * @param <C> the first target component type
     * @param <D> the second target component type
     * @param cat the cartesian category
     * @param f the morphism A → C
     * @param g the morphism B → D
     * @return the product morphism f×g : A×B → C×D
     */
    public static <F, P, T, A, B, C, D> K2<F, P2<P, A, B>, P2<P, C, D>> product(
            CartesianCategory<F, P, T> cat,
            K2<F, A, C> f,
            K2<F, B, D> g) {
        K2<F, P2<P, A, B>, A> pi1 = cat.fst();
        K2<F, P2<P, A, B>, B> pi2 = cat.snd();

        // f∘π₁ : A×B → C
        K2<F, P2<P, A, B>, C> fComp = cat.compose(f, pi1);

        // g∘π₂ : A×B → D
        K2<F, P2<P, A, B>, D> gComp = cat.compose(g, pi2);

        // ⟨f∘π₁, g∘π₂⟩ : A×B → C×D
        return cat.pair(fComp, gComp);
    }
}
