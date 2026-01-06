package io.github.torenwallengren.tmath.category.examples;

import io.github.torenwallengren.tmath.category.set.Fn;
import io.github.torenwallengren.tmath.category.set.Pair;
import io.github.torenwallengren.tmath.category.set.SetCategory;
import io.github.torenwallengren.tmath.category.set.Unit;

import static io.github.torenwallengren.tmath.category.set.SetCategory.arr;

/**
 * Demonstrates the cartesian category structure of Set.
 * Shows terminal objects, products, projections, and pairing.
 */
public class CartesianExample {

    public static void main(String[] args) {
        SetCategory set = SetCategory.instance();
        System.out.println("=== Cartesian Category: " + set + " ===\n");

        // Terminal object
        System.out.println("=== Terminal Object ===");
        @SuppressWarnings("unchecked")
        Fn<String, Unit> term1 = (Fn<String, Unit>) (Object) set.<String>terminate();
        @SuppressWarnings("unchecked")
        Fn<Integer, Unit> term2 = (Fn<Integer, Unit>) (Object) set.<Integer>terminate();

        System.out.println("terminate<String>: " + term1.name());
        System.out.println("terminate<String>(\"hello\") = " + term1.apply("hello"));
        System.out.println("terminate<Integer>(42) = " + term2.apply(42));
        System.out.println("All functions to Unit return the same value: " +
                (term1.apply("hello") == term2.apply(42)));

        // Products: projections
        System.out.println("\n=== Product Projections ===");
        @SuppressWarnings("unchecked")
        Fn<Pair<String, Integer>, String> fst = (Fn<Pair<String, Integer>, String>) (Object) set.<String, Integer>fst();
        @SuppressWarnings("unchecked")
        Fn<Pair<String, Integer>, Integer> snd = (Fn<Pair<String, Integer>, Integer>) (Object) set.<String, Integer>snd();

        Pair<String, Integer> examplePair = new Pair<>("hello", 42);
        System.out.println("Example pair: " + examplePair);
        System.out.println("π₁(pair) = " + fst.apply(examplePair));
        System.out.println("π₂(pair) = " + snd.apply(examplePair));

        // Products: pairing
        System.out.println("\n=== Product Pairing ===");
        Fn<String, String> toUpper = arr(String::toUpperCase, "upper");
        Fn<String, String> toLower = arr(String::toLowerCase, "lower");

        @SuppressWarnings("unchecked")
        Fn<String, Pair<String, String>> both = (Fn<String, Pair<String, String>>) (Object) set.pair(toUpper, toLower);
        System.out.println("Pairing morphism: " + both.name());

        @SuppressWarnings("unchecked")
        Pair<String, String> result = (Pair<String, String>) (Object) both.apply("Hello");
        System.out.println("⟨upper, lower⟩(\"Hello\") = " + result);
        System.out.println("  First component: " + result.fst());
        System.out.println("  Second component: " + result.snd());

        // Universal property: π₁ ∘ ⟨f,g⟩ = f and π₂ ∘ ⟨f,g⟩ = g
        System.out.println("\n=== Universal Property of Products ===");
        @SuppressWarnings("unchecked")
        Fn<String, String> pi1_composed = (Fn<String, String>) (Object) both.andThen((Fn) fst);
        @SuppressWarnings("unchecked")
        Fn<String, String> pi2_composed = (Fn<String, String>) (Object) both.andThen((Fn) snd);

        String testString = "Test";
        System.out.println("π₁ ∘ ⟨upper, lower⟩(\"" + testString + "\") = " + pi1_composed.apply(testString));
        System.out.println("upper(\"" + testString + "\") = " + toUpper.apply(testString));
        System.out.println("π₁ ∘ ⟨f,g⟩ = f: " + pi1_composed.apply(testString).equals(toUpper.apply(testString)));

        System.out.println("\nπ₂ ∘ ⟨upper, lower⟩(\"" + testString + "\") = " + pi2_composed.apply(testString));
        System.out.println("lower(\"" + testString + "\") = " + toLower.apply(testString));
        System.out.println("π₂ ∘ ⟨f,g⟩ = g: " + pi2_composed.apply(testString).equals(toLower.apply(testString)));

        // Complex example: duplicate and process
        System.out.println("\n=== Complex Example ===");
        Fn<Integer, Integer> double_ = arr(i -> i * 2, "double");
        Fn<Integer, Integer> square = arr(i -> i * i, "square");

        @SuppressWarnings("unchecked")
        Fn<Integer, Pair<Integer, Integer>> transform = (Fn<Integer, Pair<Integer, Integer>>) (Object) set.pair(double_, square);
        System.out.println("Transformation: " + transform.name());

        int input = 5;
        @SuppressWarnings("unchecked")
        Pair<Integer, Integer> output = (Pair<Integer, Integer>) (Object) transform.apply(input);
        System.out.println("⟨double, square⟩(" + input + ") = " + output);
        System.out.println("  Doubled: " + output.fst());
        System.out.println("  Squared: " + output.snd());

        // Nested products
        System.out.println("\n=== Nested Products ===");
        Fn<Integer, String> toString = arr(Object::toString, "toString");
        @SuppressWarnings("unchecked")
        Fn<Integer, Pair<Integer, String>> numAndStr = (Fn<Integer, Pair<Integer, String>>) (Object) set.pair(set.<Integer>id(), toString);

        @SuppressWarnings("unchecked")
        Pair<Integer, String> nested = (Pair<Integer, String>) (Object) numAndStr.apply(42);
        System.out.println("⟨id, toString⟩(42) = " + nested);
    }
}
