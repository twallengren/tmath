package io.github.torenwallengren.tmath.category.examples;

import io.github.torenwallengren.tmath.category.CartesianLemmas;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.free.FreeCartesian;
import io.github.torenwallengren.tmath.category.free.Term;
import io.github.torenwallengren.tmath.category.set.Fn;
import io.github.torenwallengren.tmath.category.set.Pair;
import io.github.torenwallengren.tmath.category.set.SetCategory;
import io.github.torenwallengren.tmath.category.set.Unit;

import static io.github.torenwallengren.tmath.category.set.SetCategory.arr;

/**
 * Demonstrates the power of categorical abstraction:
 * - Write constructions once (CartesianLemmas)
 * - Use them symbolically (Term/FreeCartesian) for inspection
 * - Use them concretely (Fn/SetCategory) for execution
 *
 * The SAME code works in both symbolic and concrete categories!
 */
public class SymbolicVsConcreteExample {

    public static void main(String[] args) {
        System.out.println("=== Symbolic vs Concrete Categories ===\n");

        // Get category instances
        FreeCartesian<Pair.Mu, Unit.Mu> free = FreeCartesian.instance();
        SetCategory set = SetCategory.instance();

        // === DIAGONAL ===
        System.out.println("=== Diagonal: Δ_A = ⟨id, id⟩ : A → A×A ===\n");

        // Symbolic: get the term structure
        @SuppressWarnings("unchecked")
        Term<String, ?> diagTerm = (Term<String, ?>) (Object) CartesianLemmas.<Term.Mu, Pair.Mu, Unit.Mu, String>diagonal(free);
        System.out.println("Symbolic (Term):");
        System.out.println("  Δ = " + diagTerm);
        System.out.println("  Type: Term<A, A×A>");
        System.out.println("  Can inspect, normalize, transform...\n");

        // Concrete: get the executable function
        @SuppressWarnings("unchecked")
        Fn<String, Pair<String, String>> diagFn =
                (Fn<String, Pair<String, String>>) (Object) CartesianLemmas.<Fn.Mu, Pair.Mu, Unit.Mu, String>diagonal(set);
        System.out.println("Concrete (Fn):");
        System.out.println("  Δ: String → (String, String)");
        @SuppressWarnings("unchecked")
        Pair<String, String> diagResult = (Pair<String, String>) (Object) diagFn.apply("hello");
        System.out.println("  Δ(\"hello\") = " + diagResult);
        System.out.println("  Can execute, compute results...\n");

        // === SWAP ===
        System.out.println("=== Swap: σ = ⟨π₂, π₁⟩ : A×B → B×A ===\n");

        // Symbolic
        Term<?, ?> swapTerm = (Term<?, ?>) CartesianLemmas.<Term.Mu, Pair.Mu, Unit.Mu, String, Integer>swap(free);
        System.out.println("Symbolic (Term):");
        System.out.println("  σ = " + swapTerm);
        System.out.println();

        // Concrete
        @SuppressWarnings("unchecked")
        Fn<Pair<String, Integer>, Pair<Integer, String>> swapFn =
                (Fn<Pair<String, Integer>, Pair<Integer, String>>) (Object)
                        CartesianLemmas.<Fn.Mu, Pair.Mu, Unit.Mu, String, Integer>swap(set);
        System.out.println("Concrete (Fn):");
        Pair<String, Integer> input = new Pair<>("foo", 42);
        @SuppressWarnings("unchecked")
        Pair<Integer, String> swapResult = (Pair<Integer, String>) (Object) swapFn.apply(input);
        System.out.println("  σ" + input + " = " + swapResult);
        System.out.println();

        // === PRODUCT OF MORPHISMS ===
        System.out.println("=== Product: f×g = ⟨f∘π₁, g∘π₂⟩ : A×B → C×D ===\n");

        // Define base morphisms
        Fn<String, Integer> length = arr(String::length, "length");
        Fn<Integer, Integer> square = arr(i -> i * i, "square");

        // Symbolic: show the structure (using id as placeholders)
        Term<String, String> lenTerm = new Term.Id<>();  // placeholder for visualization
        Term<Integer, Integer> sqTerm = new Term.Id<>();  // placeholder for visualization
        @SuppressWarnings("unchecked")
        Term<?, ?> productTerm = (Term<?, ?>) (Object) CartesianLemmas.product(free, lenTerm, sqTerm);
        System.out.println("Symbolic (Term):");
        System.out.println("  f×g = " + productTerm);
        System.out.println();

        // Concrete: actually compute
        @SuppressWarnings("unchecked")
        Fn<Pair<String, Integer>, Pair<Integer, Integer>> productFn =
                (Fn<Pair<String, Integer>, Pair<Integer, Integer>>) (Object)
                        CartesianLemmas.<Fn.Mu, Pair.Mu, Unit.Mu, String, Integer, Integer, Integer>product(
                                set, length, square);
        System.out.println("Concrete (Fn):");
        Pair<String, Integer> productInput = new Pair<>("hello", 5);
        @SuppressWarnings("unchecked")
        Pair<Integer, Integer> productResult = (Pair<Integer, Integer>) (Object) productFn.apply(productInput);
        System.out.println("  (length×square)" + productInput + " = " + productResult);
        System.out.println("  length(\"hello\") = " + productResult.fst());
        System.out.println("  square(5) = " + productResult.snd());
        System.out.println();

        // === ASSOCIATIVITY ===
        System.out.println("=== Associators: α and α⁻¹ ===\n");

        // Symbolic
        Term<?, ?> assocLeftTerm = (Term<?, ?>) CartesianLemmas.
                <Term.Mu, Pair.Mu, Unit.Mu, String, Integer, Boolean>assocLeft(free);
        Term<?, ?> assocRightTerm = (Term<?, ?>) CartesianLemmas.
                <Term.Mu, Pair.Mu, Unit.Mu, String, Integer, Boolean>assocRight(free);

        System.out.println("Symbolic (Term):");
        System.out.println("  α : (A×B)×C → A×(B×C)");
        System.out.println("    = " + assocLeftTerm);
        System.out.println();
        System.out.println("  α⁻¹ : A×(B×C) → (A×B)×C");
        System.out.println("    = " + assocRightTerm);
        System.out.println();

        // === KEY INSIGHT ===
        System.out.println("=== Key Insight ===");
        System.out.println("The SAME abstract construction (CartesianLemmas) generates:");
        System.out.println("  1. Symbolic Terms (syntax) - for reasoning, rewriting, proving");
        System.out.println("  2. Concrete Functions (semantics) - for computation, examples");
        System.out.println();
        System.out.println("This is the power of categorical abstraction:");
        System.out.println("  Write once, use everywhere!");
    }
}
