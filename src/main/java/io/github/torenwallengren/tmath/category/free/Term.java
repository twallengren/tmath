package io.github.torenwallengren.tmath.category.free;

import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.P2;

/**
 * A symbolic representation of morphisms in a cartesian category.
 *
 * Term is an AST (Abstract Syntax Tree) that represents morphisms as
 * syntax trees rather than executable functions. This allows for:
 * - Symbolic inspection and manipulation
 * - Normalization and rewriting
 * - Interpretation into different concrete categories
 *
 * This is the "free cartesian category" - morphisms are just trees
 * with no evaluation semantics.
 *
 * @param <A> the source type
 * @param <B> the target type
 */
public sealed interface Term<A, B> extends K2<Term.Mu, A, B>
        permits Term.Id, Term.Comp, Term.Fst, Term.Snd, Term.Pair, Term.Terminate {

    /**
     * Witness type for the Term arrow family.
     * Used as the F parameter in Category<F>.
     */
    final class Mu {}

    /**
     * Identity morphism: id_A : A → A
     *
     * @param <A> the object type
     */
    record Id<A>() implements Term<A, A> {
        @Override
        public String toString() {
            return "id";
        }
    }

    /**
     * Composition of morphisms: g ∘ f : A → C
     *
     * @param <A> the source type
     * @param <B> the intermediate type
     * @param <C> the target type
     * @param g the second morphism (B → C)
     * @param f the first morphism (A → B)
     */
    record Comp<A, B, C>(Term<B, C> g, Term<A, B> f) implements Term<A, C> {
        @Override
        public String toString() {
            return "(" + g + " ∘ " + f + ")";
        }
    }

    /**
     * Unique morphism to terminal object: ! : A → 1
     *
     * @param <A> the source type
     * @param <T> the terminal object witness
     */
    record Terminate<A, T>() implements Term<A, K0<T>> {
        @Override
        public String toString() {
            return "!";
        }
    }

    /**
     * First projection from product: π₁ : A×B → A
     *
     * @param <A> the first component type
     * @param <B> the second component type
     * @param <P> the product witness
     */
    record Fst<A, B, P>() implements Term<P2<P, A, B>, A> {
        @Override
        public String toString() {
            return "π₁";
        }
    }

    /**
     * Second projection from product: π₂ : A×B → B
     *
     * @param <A> the first component type
     * @param <B> the second component type
     * @param <P> the product witness
     */
    record Snd<A, B, P>() implements Term<P2<P, A, B>, B> {
        @Override
        public String toString() {
            return "π₂";
        }
    }

    /**
     * Pairing morphism: ⟨f,g⟩ : X → A×B
     *
     * @param <X> the source type
     * @param <A> the first target component type
     * @param <B> the second target component type
     * @param <P> the product witness
     * @param f the morphism to the first component (X → A)
     * @param g the morphism to the second component (X → B)
     */
    record Pair<X, A, B, P>(Term<X, A> f, Term<X, B> g) implements Term<X, P2<P, A, B>> {
        @Override
        public String toString() {
            return "⟨" + f + ", " + g + "⟩";
        }
    }
}
