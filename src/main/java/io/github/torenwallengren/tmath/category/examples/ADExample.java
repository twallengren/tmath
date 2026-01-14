package io.github.torenwallengren.tmath.category.examples;

import io.github.torenwallengren.tmath.category.DifferentialLemmas;
import io.github.torenwallengren.tmath.category.K0;
import io.github.torenwallengren.tmath.category.K2;
import io.github.torenwallengren.tmath.category.ad.Dual;
import io.github.torenwallengren.tmath.category.ad.DualFn;
import io.github.torenwallengren.tmath.category.ad.ForwardModeCategory;
import io.github.torenwallengren.tmath.category.ad.FreeDifferentialCategory;
import io.github.torenwallengren.tmath.category.ad.ReverseModeCategory;
import io.github.torenwallengren.tmath.category.ad.TapeFn;
import io.github.torenwallengren.tmath.category.ad.TapeVar;
import io.github.torenwallengren.tmath.category.free.DiffTerm;

/**
 * Demonstrates automatic differentiation using category theory.
 *
 * This example shows how the categorical abstraction allows us to:
 * 1. Define functions once using categorical operations
 * 2. Automatically compute derivatives using multiple AD modes
 * 3. Use the same polymorphic code with different implementations
 *
 * Key insight: The chain rule D[g ∘ f] = D[g] ∘ D[f] shows that
 * differentiation is a functor, and this is why category theory
 * provides the right abstraction for automatic differentiation.
 */
public class ADExample {

    public static void main(String[] args) {
        System.out.println("=== Automatic Differentiation via Category Theory ===\n");

        // Get all three AD category implementations
        ForwardModeCategory forward = ForwardModeCategory.instance();
        ReverseModeCategory reverse = ReverseModeCategory.instance();
        FreeDifferentialCategory<?, ?, ?> symbolic = FreeDifferentialCategory.instance();

        // === Example 1: Square function - comparing modes ===
        System.out.println("Example 1: f(x) = x²");
        System.out.println("Expected derivative: f'(x) = 2x\n");

        // Build square using DifferentialLemmas (polymorphic!)
        var fwdSquare = DifferentialLemmas.square(forward);
        var revSquare = DifferentialLemmas.square(reverse);
        var symSquare = DifferentialLemmas.square(symbolic);

        double x = 3.0;
        System.out.printf("  Forward mode:  f'(%.1f) = %.1f%n", x, forward.derivativeAt(fwdSquare, x));
        System.out.printf("  Reverse mode:  f'(%.1f) = %.1f%n", x, reverse.derivativeAt(revSquare, x));
        System.out.printf("  Symbolic term: %s%n", DiffTerm.toInfix((DiffTerm<?, ?>) symSquare));
        System.out.printf("  Expected:      f'(%.1f) = %.1f%n", x, 2 * x);

        // === Example 2: Cube function ===
        System.out.println("\nExample 2: f(x) = x³");
        System.out.println("Expected derivative: f'(x) = 3x²\n");

        var fwdCube = DifferentialLemmas.cube(forward);
        var revCube = DifferentialLemmas.cube(reverse);

        double[] testPoints = {0.0, 1.0, 2.0, 3.0};
        for (double pt : testPoints) {
            double fwdDeriv = forward.derivativeAt(fwdCube, pt);
            double revDeriv = reverse.derivativeAt(revCube, pt);
            System.out.printf("  x=%.1f: fwd=%.1f, rev=%.1f (expected: %.1f)%n",
                    pt, fwdDeriv, revDeriv, 3 * pt * pt);
        }

        // === Example 3: Quadratic polynomial (forward mode only) ===
        // Note: Quadratic uses constants which need special handling in reverse mode
        System.out.println("\nExample 3: f(x) = 2x² + 3x + 1 (forward mode)");
        System.out.println("Expected derivative: f'(x) = 4x + 3\n");

        var fwdQuad = DifferentialLemmas.quadratic(forward, 2, 3, 1);

        for (double pt : testPoints) {
            double fwdDeriv = forward.derivativeAt(fwdQuad, pt);
            System.out.printf("  x=%.1f: fwd=%.1f (expected: %.1f)%n",
                    pt, fwdDeriv, 4 * pt + 3);
        }

        // === Example 4: Reciprocal function (forward mode only) ===
        // Note: Reciprocal uses constants (the 1 in 1/x)
        System.out.println("\nExample 4: f(x) = 1/x (forward mode)");
        System.out.println("Expected derivative: f'(x) = -1/x²\n");

        var fwdRecip = DifferentialLemmas.reciprocal(forward);

        double[] nonZeroPoints = {1.0, 2.0, 0.5};
        for (double pt : nonZeroPoints) {
            double fwdDeriv = forward.derivativeAt(fwdRecip, pt);
            System.out.printf("  x=%.1f: fwd=%.4f (expected: %.4f)%n",
                    pt, fwdDeriv, -1.0 / (pt * pt));
        }

        // === Key Insight ===
        System.out.println("\n=== Key Insight ===");
        System.out.println("The SAME DifferentialLemmas code works with ALL three modes:");
        System.out.println("  - Forward mode (dual numbers)");
        System.out.println("  - Reverse mode (tape + backprop)");
        System.out.println("  - Symbolic mode (term AST)");
        System.out.println("\nCategory theory provides the unifying abstraction:");
        System.out.println("  - D[g ∘ f] = D[g] ∘ D[f]  (chain rule = functoriality)");
        System.out.println("  - Write polymorphic code, run in any implementation");
    }
}
