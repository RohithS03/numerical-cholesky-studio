package com.numerical.studio.engine;

import com.numerical.studio.exception.MatrixValidationException;
import com.numerical.studio.model.Matrix;
import com.numerical.studio.model.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves linear system A x = b using Cholesky factor L (A = L L^T).
 * Uses Forward Substitution (L y = b) followed by Back Substitution (L^T x = y).
 */
public class LinearSystemSolver {

    public static class LinearSolveResult {
        private final Matrix A;
        private final Vector b;
        private final Matrix L;
        private final Vector y; // Intermediate vector from L y = b
        private final Vector x; // Solution vector from L^T x = y
        private final Vector residual; // r = A x - b
        private final double residualNorm; // ||r||_2
        private final List<String> forwardSteps;
        private final List<String> backSteps;

        public LinearSolveResult(Matrix A, Vector b, Matrix L, Vector y, Vector x, List<String> forwardSteps, List<String> backSteps) {
            this.A = A;
            this.b = b;
            this.L = L;
            this.y = y;
            this.x = x;
            this.residual = A.multiply(x).subtract(b);
            this.residualNorm = this.residual.norm2();
            this.forwardSteps = forwardSteps;
            this.backSteps = backSteps;
        }

        public Matrix getA() { return A; }
        public Vector getB() { return b; }
        public Matrix getL() { return L; }
        public Vector getY() { return y; }
        public Vector getX() { return x; }
        public Vector getResidual() { return residual; }
        public double getResidualNorm() { return residualNorm; }
        public List<String> getForwardSteps() { return forwardSteps; }
        public List<String> getBackSteps() { return backSteps; }
    }

    /**
     * Solves A x = b via Cholesky decomposition.
     */
    public LinearSolveResult solve(Matrix A, Vector b) {
        if (A == null || b == null) {
            throw new MatrixValidationException("Matrix A and Vector b cannot be null.");
        }
        if (A.rows() != b.size()) {
            throw new MatrixValidationException("Dimension mismatch: Matrix A (" + A.rows() + "x" + A.cols() + ") and Vector b (" + b.size() + ").");
        }

        // Perform Cholesky Decomposition
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        CholeskyDecomposer.DecompositionResult decomp = decomposer.decompose(A);
        Matrix L = decomp.getL();

        int n = A.rows();
        Vector y = new Vector(n);
        Vector x = new Vector(n);

        List<String> forwardSteps = new ArrayList<>();
        List<String> backSteps = new ArrayList<>();

        // 1. Forward Substitution: L y = b
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int k = 0; k < i; k++) {
                sum += L.get(i, k) * y.get(k);
            }
            double l_ii = L.get(i, i);
            double yi = (b.get(i) - sum) / l_ii;
            y.set(i, yi);
            forwardSteps.add(String.format("y[%d] = (b[%d] - sum) / L[%d][%d] = (%.4f - %.4f) / %.4f = %.4f",
                    i, i, i, i, b.get(i), sum, l_ii, yi));
        }

        // 2. Back Substitution: L^T x = y
        // Note: L^T[i][k] = L[k][i]
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int k = i + 1; k < n; k++) {
                sum += L.get(k, i) * x.get(k);
            }
            double l_ii = L.get(i, i);
            double xi = (y.get(i) - sum) / l_ii;
            x.set(i, xi);
            backSteps.add(String.format("x[%d] = (y[%d] - sum) / L^T[%d][%d] = (%.4f - %.4f) / %.4f = %.4f",
                    i, i, i, i, y.get(i), sum, l_ii, xi));
        }

        return new LinearSolveResult(A, b, L, y, x, forwardSteps, backSteps);
    }
}
