package io.github.torenwallengren.tmath.category.ad;

import io.github.torenwallengren.tmath.category.K2;

import java.util.Objects;
import java.util.function.Function;

/**
 * A morphism in the reverse-mode AD category.
 */
public final class TapeFn<A, B> implements K2<TapeFn.Mu, A, B> {

    /**
     * Witness type for the TapeFn arrow family.
     */
    public static final class Mu {}

    private final Function<A, B> function;
    private final String name;

    public TapeFn(Function<A, B> function) {
        this(function, null);
    }

    public TapeFn(Function<A, B> function, String name) {
        this.function = Objects.requireNonNull(function);
        this.name = name;
    }

    public B apply(A input) {
        return function.apply(input);
    }

    public String name() {
        return name;
    }

    public static <A> TapeFn<A, A> identity() {
        return new TapeFn<>(Function.identity(), "id");
    }

    public <C> TapeFn<A, C> andThen(TapeFn<B, C> after) {
        String composedName = (name != null && after.name != null)
                ? after.name + " ∘ " + name
                : null;
        return new TapeFn<>(input -> after.apply(this.apply(input)), composedName);
    }

    public static <A, B> TapeFn<A, B> of(Function<A, B> fn) {
        return new TapeFn<>(fn);
    }

    public static <A, B> TapeFn<A, B> of(Function<A, B> fn, String name) {
        return new TapeFn<>(fn, name);
    }

    @Override
    public String toString() {
        return name != null ? name : "TapeFn";
    }
}
