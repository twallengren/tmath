package io.github.torenwallengren.tmath.category.free;

import io.github.torenwallengren.tmath.category.CartesianCategory;
import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.P2;

/**
 * The free cartesian category: morphisms are symbolic terms (AST nodes).
 *
 * This category has no evaluation semantics - morphisms are purely syntactic.
 * It represents the "syntax" of cartesian category theory.
 *
 * The free cartesian category is universal: any cartesian category can be
 * obtained by interpreting these terms (via a functor).
 *
 * @param <P> the product witness (e.g., Pair.Mu for Set)
 * @param <T> the terminal witness (e.g., Unit.Mu for Set)
 */
public final class FreeCartesian<P, T> implements CartesianCategory<Term.Mu, P, T> {

    private static final FreeCartesian<?, ?> INSTANCE = new FreeCartesian<>();

    private FreeCartesian() {
        // Singleton
    }

    /**
     * Returns the singleton instance of the free cartesian category.
     *
     * @param <P> the product witness
     * @param <T> the terminal witness
     * @return the free cartesian category instance
     */
    @SuppressWarnings("unchecked")
    public static <P, T> FreeCartesian<P, T> instance() {
        return (FreeCartesian<P, T>) INSTANCE;
    }

    // Category operations

    @Override
    public <A> Term<A, A> id() {
        return new Term.Id<>();
    }

    @Override
    public <A, B, C> Term<A, C> compose(K2<Term.Mu, B, C> g, K2<Term.Mu, A, B> f) {
        return new Term.Comp<>((Term<B, C>) g, (Term<A, B>) f);
    }

    // Terminal object operations

    @Override
    public <A> Term<A, K0<T>> terminate() {
        return new Term.Terminate<>();
    }

    // Binary product operations

    @Override
    public <A, B> Term<P2<P, A, B>, A> fst() {
        return new Term.Fst<>();
    }

    @Override
    public <A, B> Term<P2<P, A, B>, B> snd() {
        return new Term.Snd<>();
    }

    @Override
    public <X, A, B> Term<X, P2<P, A, B>> pair(K2<Term.Mu, X, A> f, K2<Term.Mu, X, B> g) {
        return new Term.Pair<>((Term<X, A>) f, (Term<X, B>) g);
    }

    @Override
    public String toString() {
        return "Free";
    }
}
