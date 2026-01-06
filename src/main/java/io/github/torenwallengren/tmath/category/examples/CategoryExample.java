package io.github.torenwallengren.tmath.category.examples;

import io.github.torenwallengren.tmath.category.set.Fn;
import io.github.torenwallengren.tmath.category.set.SetCategory;

import static io.github.torenwallengren.tmath.category.set.SetCategory.arr;

/**
 * Example usage of the category theory interfaces.
 * Demonstrates the K2 witness pattern, morphisms, composition, and the Set category.
 */
public class CategoryExample {

    public static void main(String[] args) {
        // Get the Set category instance
        SetCategory set = SetCategory.instance();
        System.out.println("=== Category: " + set + " ===\n");

        // Create some morphisms in Set using the arr helper
        Fn<Integer, String> intToString = arr(i -> "Number: " + i, "intToString");
        Fn<String, Integer> stringLength = arr(String::length, "length");
        Fn<Integer, Integer> doubleIt = arr(i -> i * 2, "double");

        // Test basic morphism application
        System.out.println("=== Basic Morphisms ===");
        System.out.println("intToString(42) = " + intToString.apply(42));
        System.out.println("stringLength(\"hello\") = " + stringLength.apply("hello"));
        System.out.println("doubleIt(21) = " + doubleIt.apply(21));

        // Test composition using the category's compose method
        System.out.println("\n=== Composition (via Category) ===");
        Fn<Integer, Integer> composed1 = (Fn<Integer, Integer>) set.compose(stringLength, intToString);
        System.out.println("(length ∘ intToString)(42) = " + composed1.apply(42));
        System.out.println("Composition name: " + composed1.name());

        // Test composition using Fn's andThen method
        System.out.println("\n=== Composition (via Fn.andThen) ===");
        Fn<Integer, Integer> composed2 = intToString.andThen(stringLength);
        System.out.println("(length ∘ intToString)(42) = " + composed2.apply(42));
        System.out.println("Composition name: " + composed2.name());

        // Test identity morphism
        System.out.println("\n=== Identity ===");
        Fn<String, String> id = Fn.identity();
        String testString = "test";
        System.out.println("identity(\"" + testString + "\") = " + id.apply(testString));

        // Test identity laws: id ∘ f = f
        System.out.println("\n=== Identity Laws ===");
        Fn<Integer, Integer> justF = arr(i -> i + 1, "f");
        Fn<Integer, Integer> idThenF = (Fn<Integer, Integer>) set.compose(justF, set.id());
        System.out.println("f(10) = " + justF.apply(10));
        System.out.println("(f ∘ id)(10) = " + idThenF.apply(10));
        System.out.println("Identity law holds: " + (justF.apply(10).equals(idThenF.apply(10))));

        // Test associativity: h ∘ (g ∘ f) = (h ∘ g) ∘ f
        System.out.println("\n=== Associativity ===");
        Fn<Integer, Integer> f = arr(i -> i + 1, "f");
        Fn<Integer, Integer> g = arr(i -> i * 2, "g");
        Fn<Integer, Integer> h = arr(i -> i * i, "h");

        int testValue = 5;

        // Left: h ∘ (g ∘ f)
        Fn<Integer, Integer> gComposeF = (Fn<Integer, Integer>) set.compose(g, f);
        Fn<Integer, Integer> left = (Fn<Integer, Integer>) set.compose(h, gComposeF);

        // Right: (h ∘ g) ∘ f
        Fn<Integer, Integer> hComposeG = (Fn<Integer, Integer>) set.compose(h, g);
        Fn<Integer, Integer> right = (Fn<Integer, Integer>) set.compose(hComposeG, f);

        System.out.println("h ∘ (g ∘ f)(" + testValue + ") = " + left.apply(testValue));
        System.out.println("(h ∘ g) ∘ f(" + testValue + ") = " + right.apply(testValue));
        System.out.println("Associativity holds: " +
            (left.apply(testValue).equals(right.apply(testValue))));

        // Demonstrate metadata
        System.out.println("\n=== Metadata ===");
        Fn<Integer, Integer> triple = arr(i -> i * 3, "triple");
        Fn<Integer, Integer> withMeta = triple.withMetadata("derivative: 3");
        System.out.println("Function: " + withMeta.name());
        System.out.println("Metadata: " + withMeta.metadata().orElse("none"));
        System.out.println("Apply: " + withMeta.apply(7));
    }
}
