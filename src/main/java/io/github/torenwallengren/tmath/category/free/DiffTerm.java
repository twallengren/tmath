package io.github.torenwallengren.tmath.category.free;

import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.P2;

/**
 * A symbolic representation of differentiable morphisms.
 *
 * DiffTerm extends the basic Term concept with arithmetic operations,
 * allowing symbolic differentiation. Each term can be:
 * - Inspected to understand the expression structure
 * - Differentiated symbolically using standard rules
 * - Interpreted into concrete categories
 *
 * This is the "free differential category" - morphisms are syntax trees
 * that can be manipulated, simplified, and transformed.
 *
 * @param <A> the source type
 * @param <B> the target type
 */
public sealed interface DiffTerm<A, B> extends K2<DiffTerm.Mu, A, B>
        permits DiffTerm.Id, DiffTerm.Comp, DiffTerm.Fst, DiffTerm.Snd,
                DiffTerm.Pair, DiffTerm.Terminate,
                DiffTerm.Add, DiffTerm.Mul, DiffTerm.Neg, DiffTerm.Div,
                DiffTerm.Const, DiffTerm.Var {

    /**
     * Witness type for the DiffTerm arrow family.
     */
    final class Mu {}

    // === Cartesian structure (from Term) ===

    /**
     * Identity morphism: id_A : A → A
     */
    record Id<A>() implements DiffTerm<A, A> {
        @Override
        public String toString() {
            return "id";
        }
    }

    /**
     * Composition of morphisms: g ∘ f : A → C
     */
    record Comp<A, B, C>(DiffTerm<B, C> g, DiffTerm<A, B> f) implements DiffTerm<A, C> {
        @Override
        public String toString() {
            return "(" + g + " ∘ " + f + ")";
        }
    }

    /**
     * Unique morphism to terminal object: ! : A → 1
     */
    record Terminate<A, T>() implements DiffTerm<A, K0<T>> {
        @Override
        public String toString() {
            return "!";
        }
    }

    /**
     * First projection: π₁ : A×B → A
     */
    record Fst<A, B, P>() implements DiffTerm<P2<P, A, B>, A> {
        @Override
        public String toString() {
            return "π₁";
        }
    }

    /**
     * Second projection: π₂ : A×B → B
     */
    record Snd<A, B, P>() implements DiffTerm<P2<P, A, B>, B> {
        @Override
        public String toString() {
            return "π₂";
        }
    }

    /**
     * Pairing morphism: ⟨f,g⟩ : X → A×B
     */
    record Pair<X, A, B, P>(DiffTerm<X, A> f, DiffTerm<X, B> g) implements DiffTerm<X, P2<P, A, B>> {
        @Override
        public String toString() {
            return "⟨" + f + ", " + g + "⟩";
        }
    }

    // === Arithmetic operations ===

    /**
     * Addition: + : R×R → R
     */
    record Add<P, R>() implements DiffTerm<P2<P, K0<R>, K0<R>>, K0<R>> {
        @Override
        public String toString() {
            return "+";
        }
    }

    /**
     * Multiplication: * : R×R → R
     */
    record Mul<P, R>() implements DiffTerm<P2<P, K0<R>, K0<R>>, K0<R>> {
        @Override
        public String toString() {
            return "*";
        }
    }

    /**
     * Negation: - : R → R
     */
    record Neg<R>() implements DiffTerm<K0<R>, K0<R>> {
        @Override
        public String toString() {
            return "neg";
        }
    }

    /**
     * Division: / : R×R → R
     */
    record Div<P, R>() implements DiffTerm<P2<P, K0<R>, K0<R>>, K0<R>> {
        @Override
        public String toString() {
            return "/";
        }
    }

    // === Constants and variables ===

    /**
     * Constant: c : 1 → R
     * A constant value in the scalar type.
     */
    record Const<T, R>(double value) implements DiffTerm<K0<T>, K0<R>> {
        @Override
        public String toString() {
            if (value == (long) value) {
                return String.valueOf((long) value);
            }
            return String.valueOf(value);
        }
    }

    /**
     * Variable: var : R → R
     * Represents a named variable (identity on scalars with a name).
     */
    record Var<R>(String name) implements DiffTerm<K0<R>, K0<R>> {
        @Override
        public String toString() {
            return name;
        }
    }

    // === Pretty printing helpers ===

    /**
     * Convert a DiffTerm to a more readable infix expression.
     */
    static String toInfix(DiffTerm<?, ?> term) {
        return switch (term) {
            case Id<?> id -> "x";
            case Var<?> v -> v.name();
            case Const<?, ?> c -> c.toString();
            case Neg<?> n -> "neg";
            case Add<?, ?> a -> "+";
            case Mul<?, ?> m -> "*";
            case Div<?, ?> d -> "/";
            case Fst<?, ?, ?> f -> "π₁";
            case Snd<?, ?, ?> s -> "π₂";
            case Terminate<?, ?> t -> "!";
            case Pair<?, ?, ?, ?> p -> "⟨" + toInfix(p.f()) + ", " + toInfix(p.g()) + "⟩";
            case Comp<?, ?, ?> c -> toInfixComp(c);
        };
    }

    private static String toInfixComp(Comp<?, ?, ?> comp) {
        var g = comp.g();
        var f = comp.f();

        // Special case: binary operation applied to a pair
        if (g instanceof Add<?, ?> && f instanceof Pair<?, ?, ?, ?> p) {
            return "(" + toInfix(p.f()) + " + " + toInfix(p.g()) + ")";
        }
        if (g instanceof Mul<?, ?> && f instanceof Pair<?, ?, ?, ?> p) {
            return "(" + toInfix(p.f()) + " * " + toInfix(p.g()) + ")";
        }
        if (g instanceof Div<?, ?> && f instanceof Pair<?, ?, ?, ?> p) {
            return "(" + toInfix(p.f()) + " / " + toInfix(p.g()) + ")";
        }
        if (g instanceof Neg<?>) {
            return "-" + toInfix(f);
        }

        // General composition
        return toInfix(g) + "(" + toInfix(f) + ")";
    }
}
