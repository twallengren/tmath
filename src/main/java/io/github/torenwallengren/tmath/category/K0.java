package io.github.torenwallengren.tmath.category;

/**
 * A kind-0 type encoding: a constant object carrier for witness T.
 *
 * This represents a "picked" terminal object (or other constant object)
 * in a category. The type parameter T is a witness type that identifies
 * which particular constant object we're talking about.
 *
 * For example, K0<Unit.Mu> represents the Unit terminal object in Set.
 *
 * @param <T> the witness type for the constant object
 */
public interface K0<T> {
}
