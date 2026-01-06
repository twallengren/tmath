package io.github.torenwallengren.tmath.category.set;

import io.github.torenwallengren.tmath.category.CartesianCategory;
import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.P2;

/**
 * The category Set, where:
 * - Objects are Java types (A, B, C, ...)
 * - Morphisms are functions between types (Fn<A, B>)
 * - Composition is function composition
 * - Identity is the identity function
 * - Terminal object is Unit
 * - Products are Pairs
 *
 * This is one of the most fundamental categories in mathematics.
 * In Set, every Java type is an object, and every total function between
 * types is a morphism.
 *
 * Set is a cartesian category, meaning it has:
 * - A terminal object (Unit - the singleton type)
 * - Binary products (Pair - ordered pairs)
 *
 * This implementation uses the witness type pattern with:
 * - Fn.Mu as the witness for the arrow family
 * - Pair.Mu as the witness for products
 * - Unit.Mu as the witness for the terminal object
 */
public final class SetCategory implements CartesianCategory<Fn.Mu, Pair.Mu, Unit.Mu> {

    private static final SetCategory INSTANCE = new SetCategory();

    private SetCategory() {
        // Singleton
    }

    /**
     * Returns the singleton instance of SetCategory.
     *
     * @return the Set category instance
     */
    public static SetCategory instance() {
        return INSTANCE;
    }

    /**
     * Returns the identity morphism for type A.
     * The identity function satisfies: id(x) = x for all x.
     *
     * @param <A> the object type
     * @return the identity function id: A → A
     */
    @Override
    public <A> K2<Fn.Mu, A, A> id() {
        return Fn.identity();
    }

    /**
     * Composes two functions in Set.
     * Given f: A → B and g: B → C, returns (g ∘ f): A → C
     * where (g ∘ f)(x) = g(f(x))
     *
     * @param <A> the source type
     * @param <B> the intermediate type
     * @param <C> the target type
     * @param g the second function (B → C)
     * @param f the first function (A → B)
     * @return the composed function (g ∘ f): A → C
     */
    @Override
    public <A, B, C> K2<Fn.Mu, A, C> compose(K2<Fn.Mu, B, C> g, K2<Fn.Mu, A, B> f) {
        // Safe casts because K2<Fn.Mu, X, Y> can only be constructed as Fn<X, Y>
        Fn<B, C> gFn = (Fn<B, C>) g;
        Fn<A, B> fFn = (Fn<A, B>) f;
        return fFn.andThen(gFn);
    }

    /**
     * Creates a morphism in Set from a Java function.
     *
     * @param <A> the source type
     * @param <B> the target type
     * @param fn the function
     * @return a morphism in Set
     */
    public static <A, B> Fn<A, B> arr(java.util.function.Function<A, B> fn) {
        return Fn.of(fn);
    }

    /**
     * Creates a named morphism in Set from a Java function.
     *
     * @param <A> the source type
     * @param <B> the target type
     * @param fn the function
     * @param name the name of the morphism
     * @return a named morphism in Set
     */
    public static <A, B> Fn<A, B> arr(java.util.function.Function<A, B> fn, String name) {
        return Fn.of(fn, name);
    }

    // Terminal object methods

    /**
     * Returns the unique morphism from A to the terminal object (Unit).
     * This function ignores its input and always returns Unit.INSTANCE.
     *
     * @param <A> the source type
     * @return the unique morphism A → Unit
     */
    @Override
    public <A> K2<Fn.Mu, A, K0<Unit.Mu>> terminate() {
        return Fn.of(a -> Unit.INSTANCE, "!");
    }

    // Binary product methods

    /**
     * Returns the first projection π₁: A×B → A
     *
     * @param <A> the first component type
     * @param <B> the second component type
     * @return the first projection
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A, B> K2<Fn.Mu, P2<Pair.Mu, A, B>, A> fst() {
        return Fn.of(p -> ((Pair<A, B>) p).fst(), "π₁");
    }

    /**
     * Returns the second projection π₂: A×B → B
     *
     * @param <A> the first component type
     * @param <B> the second component type
     * @return the second projection
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A, B> K2<Fn.Mu, P2<Pair.Mu, A, B>, B> snd() {
        return Fn.of(p -> ((Pair<A, B>) p).snd(), "π₂");
    }

    /**
     * Given morphisms f: X → A and g: X → B, returns the pairing ⟨f,g⟩: X → A×B
     * such that π₁ ∘ ⟨f,g⟩ = f and π₂ ∘ ⟨f,g⟩ = g
     *
     * @param <X> the source type
     * @param <A> the first target component type
     * @param <B> the second target component type
     * @param f the morphism to the first component
     * @param g the morphism to the second component
     * @return the pairing morphism
     */
    @Override
    @SuppressWarnings("unchecked")
    public <X, A, B> K2<Fn.Mu, X, P2<Pair.Mu, A, B>> pair(K2<Fn.Mu, X, A> f, K2<Fn.Mu, X, B> g) {
        Fn<X, A> ff = (Fn<X, A>) f;
        Fn<X, B> gg = (Fn<X, B>) g;
        String name = (ff.name() != null && gg.name() != null)
                ? "⟨" + ff.name() + ", " + gg.name() + "⟩"
                : "⟨f, g⟩";
        return Fn.of(x -> new Pair<>(ff.apply(x), gg.apply(x)), name);
    }

    @Override
    public String toString() {
        return "Set";
    }
}

