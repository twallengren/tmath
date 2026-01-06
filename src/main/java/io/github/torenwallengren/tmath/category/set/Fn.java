package io.github.torenwallengren.tmath.category.set;

import io.github.torenwallengren.tmath.category.K2;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A morphism in the category Set: a function from A to B with optional metadata.
 *
 * This class wraps java.util.function.Function and implements K2 to fit into
 * the category framework. It can carry additional metadata such as:
 * - name/description
 * - derivative information (for automatic differentiation)
 * - composition history
 * - mathematical properties
 *
 * @param <A> the domain (source) type
 * @param <B> the codomain (target) type
 */
public final class Fn<A, B> implements K2<Fn.Mu, A, B> {

    /**
     * Witness type for the "function arrow" family.
     * This is used as the F parameter in Category<F>.
     */
    public static final class Mu {}

    private final Function<A, B> function;
    private final String name;
    private final Optional<Object> metadata;  // Extensible metadata slot

    /**
     * Creates a function morphism from a Java function.
     *
     * @param function the underlying function
     */
    public Fn(Function<A, B> function) {
        this(function, null, Optional.empty());
    }

    /**
     * Creates a named function morphism.
     *
     * @param function the underlying function
     * @param name a descriptive name for this morphism
     */
    public Fn(Function<A, B> function, String name) {
        this(function, name, Optional.empty());
    }

    /**
     * Creates a function morphism with full metadata.
     *
     * @param function the underlying function
     * @param name a descriptive name for this morphism
     * @param metadata optional metadata (derivative info, etc.)
     */
    public Fn(Function<A, B> function, String name, Optional<Object> metadata) {
        this.function = Objects.requireNonNull(function, "function cannot be null");
        this.name = name;
        this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");
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
     * Returns the metadata associated with this function.
     *
     * @return optional metadata
     */
    public Optional<Object> metadata() {
        return metadata;
    }

    /**
     * Creates a new Fn with updated metadata.
     *
     * @param newMetadata the new metadata
     * @return a new Fn with the same function but different metadata
     */
    public Fn<A, B> withMetadata(Object newMetadata) {
        return new Fn<>(function, name, Optional.of(newMetadata));
    }

    /**
     * Creates a new Fn with an updated name.
     *
     * @param newName the new name
     * @return a new Fn with the same function but different name
     */
    public Fn<A, B> withName(String newName) {
        return new Fn<>(function, newName, metadata);
    }

    /**
     * Creates the identity function for type A.
     *
     * @param <A> the type
     * @return the identity function id: A → A
     */
    public static <A> Fn<A, A> identity() {
        return new Fn<>(Function.identity(), "id");
    }

    /**
     * Composes this function with another: (g ∘ f)(x) = g(f(x))
     *
     * @param <C> the output type of g
     * @param after the function to apply after this one (g: B → C)
     * @return the composed function (g ∘ f: A → C)
     */
    public <C> Fn<A, C> andThen(Fn<B, C> after) {
        String composedName = (name != null && after.name != null)
                ? after.name + " ∘ " + name
                : null;
        return new Fn<>(
                input -> after.apply(this.apply(input)),
                composedName,
                Optional.empty()  // Metadata composition could be more sophisticated
        );
    }

    /**
     * Creates a simple function morphism from a lambda.
     *
     * @param <A> the domain type
     * @param <B> the codomain type
     * @param fn the function
     * @return a morphism wrapping the function
     */
    public static <A, B> Fn<A, B> of(Function<A, B> fn) {
        return new Fn<>(fn);
    }

    /**
     * Creates a named function morphism from a lambda.
     *
     * @param <A> the domain type
     * @param <B> the codomain type
     * @param fn the function
     * @param name the name
     * @return a named morphism wrapping the function
     */
    public static <A, B> Fn<A, B> of(Function<A, B> fn, String name) {
        return new Fn<>(fn, name);
    }

    @Override
    public String toString() {
        return name != null ? name : "Fn";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Fn<?, ?> other)) return false;
        // Function equality is reference equality (can't compare lambdas structurally)
        return function.equals(other.function);
    }

    @Override
    public int hashCode() {
        return function.hashCode();
    }
}
