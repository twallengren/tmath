package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.K2;

import java.util.Objects;
import java.util.function.Function;

/**
 * A morphism in the forward-mode AD category: a function with automatic differentiation.
 *
 * DualFn wraps a function and provides composition. When functions operate
 * on Dual numbers, differentiation happens automatically via the arithmetic
 * of dual numbers.
 *
 * @param <A> the domain (source) type
 * @param <B> the codomain (target) type
 */
public final class DualFn<A, B> implements K2<DualFn.Mu, A, B> {

    /**
     * Witness type for the DualFn arrow family.
     * Used as the F parameter in Category<F>.
     */
    public static final class Mu {}

    private final Function<A, B> function;
    private final String name;

    /**
     * Creates a DualFn morphism from a Java function.
     *
     * @param function the underlying function
     */
    public DualFn(Function<A, B> function) {
        this(function, null);
    }

    /**
     * Creates a named DualFn morphism.
     *
     * @param function the underlying function
     * @param name a descriptive name for this morphism
     */
    public DualFn(Function<A, B> function, String name) {
        this.function = Objects.requireNonNull(function, "function cannot be null");
        this.name = name;
    }

    /**
     * Applies this function to the given input.
     *
     * @param input the input value
     * @return the output value
     */
    public B apply(A input) {
        return function.apply(input);
    }

    /**
     * Returns the name of this function, if any.
     *
     * @return the function name, or null if unnamed
     */
    public String name() {
        return name;
    }

    /**
     * Creates the identity function for type A.
     *
     * @param <A> the type
     * @return the identity function id: A → A
     */
    public static <A> DualFn<A, A> identity() {
        return new DualFn<>(Function.identity(), "id");
    }

    /**
     * Composes this function with another: (g ∘ f)(x) = g(f(x))
     *
     * @param <C> the output type of g
     * @param after the function to apply after this one (g: B → C)
     * @return the composed function (g ∘ f: A → C)
     */
    public <C> DualFn<A, C> andThen(DualFn<B, C> after) {
        String composedName = (name != null && after.name != null)
                ? after.name + " ∘ " + name
                : null;
        return new DualFn<>(
                input -> after.apply(this.apply(input)),
                composedName
        );
    }

    /**
     * Creates a DualFn morphism from a lambda.
     *
     * @param <A> the domain type
     * @param <B> the codomain type
     * @param fn the function
     * @return a morphism wrapping the function
     */
    public static <A, B> DualFn<A, B> of(Function<A, B> fn) {
        return new DualFn<>(fn);
    }

    /**
     * Creates a named DualFn morphism from a lambda.
     *
     * @param <A> the domain type
     * @param <B> the codomain type
     * @param fn the function
     * @param name the name
     * @return a named morphism wrapping the function
     */
    public static <A, B> DualFn<A, B> of(Function<A, B> fn, String name) {
        return new DualFn<>(fn, name);
    }

    @Override
    public String toString() {
        return name != null ? name : "DualFn";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DualFn<?, ?> other)) return false;
        return function.equals(other.function);
    }

    @Override
    public int hashCode() {
        return function.hashCode();
    }
}
