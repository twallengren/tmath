package io.github.torenwallengren.tmath.category.ad;

import java.util.ArrayList;
import java.util.List;

/**
 * A tape for recording computation in reverse-mode AD.
 *
 * The tape records a computation graph during the forward pass:
 * - Each node stores a value and references to its parents
 * - Each edge stores the local partial derivative
 *
 * During backpropagation:
 * - We traverse the tape in reverse order
 * - Gradients are accumulated using the chain rule
 *
 * This is more efficient than forward mode for functions with
 * many inputs and few outputs (like loss functions in ML).
 */
public final class Tape {

    private final List<TapeNode> nodes = new ArrayList<>();

    /**
     * Create a new variable on the tape.
     *
     * @param value the initial value
     * @return a TapeVar representing this variable
     */
    public TapeVar variable(double value) {
        int index = nodes.size();
        nodes.add(new TapeNode(value, new ArrayList<>()));
        return new TapeVar(index, this);
    }

    /**
     * Create a constant (no gradient flows through it).
     */
    public TapeVar constant(double value) {
        return variable(value);
    }

    /**
     * Record a unary operation.
     *
     * @param input the input variable
     * @param resultValue the computed result
     * @param localGradient d(result)/d(input)
     * @return a new TapeVar for the result
     */
    TapeVar recordUnary(TapeVar input, double resultValue, double localGradient) {
        int index = nodes.size();
        List<TapeEdge> parents = new ArrayList<>();
        parents.add(new TapeEdge(input.index(), localGradient));
        nodes.add(new TapeNode(resultValue, parents));
        return new TapeVar(index, this);
    }

    /**
     * Record a binary operation.
     *
     * @param left the left input
     * @param right the right input
     * @param resultValue the computed result
     * @param leftGradient d(result)/d(left)
     * @param rightGradient d(result)/d(right)
     * @return a new TapeVar for the result
     */
    TapeVar recordBinary(TapeVar left, TapeVar right, double resultValue,
                         double leftGradient, double rightGradient) {
        int index = nodes.size();
        List<TapeEdge> parents = new ArrayList<>();
        parents.add(new TapeEdge(left.index(), leftGradient));
        parents.add(new TapeEdge(right.index(), rightGradient));
        nodes.add(new TapeNode(resultValue, parents));
        return new TapeVar(index, this);
    }

    /**
     * Get the value of a node.
     */
    double getValue(int index) {
        return nodes.get(index).value();
    }

    /**
     * Backpropagate from an output to compute all gradients.
     *
     * @param outputIndex the index of the output variable
     * @return array of gradients for all variables on the tape
     */
    public double[] backpropagate(int outputIndex) {
        double[] adjoints = new double[nodes.size()];

        // Seed the output gradient
        adjoints[outputIndex] = 1.0;

        // Reverse traversal
        for (int i = nodes.size() - 1; i >= 0; i--) {
            TapeNode node = nodes.get(i);
            double adjoint = adjoints[i];

            // Propagate gradient to parents
            for (TapeEdge edge : node.parents()) {
                adjoints[edge.parentIndex()] += adjoint * edge.localGradient();
            }
        }

        return adjoints;
    }

    /**
     * Get the number of nodes on the tape.
     */
    public int size() {
        return nodes.size();
    }

    /**
     * A node in the computation tape.
     */
    record TapeNode(double value, List<TapeEdge> parents) {}

    /**
     * An edge representing a parent relationship with local gradient.
     */
    record TapeEdge(int parentIndex, double localGradient) {}
}
