package com.numerical.studio.engine;

import com.numerical.studio.exception.MatrixValidationException;
import com.numerical.studio.exception.NonPositiveDefiniteException;
import com.numerical.studio.model.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes Cholesky Decomposition (A = L L^T) on Symmetric Positive-Definite (SPD) matrices.
 */
public class CholeskyDecomposer {
    public static class CholeskyStep {
        private final int row;
        private final int col;
        private final boolean isDiagonal;
        private final String formula;
        private final double sum;
        private final double rawVal;
        private final double resultVal;

        public CholeskyStep(int row, int col, boolean isDiagonal, String formula, double sum, double rawVal, double resultVal) {
            this.row = row;
            this.col = col;
            this.isDiagonal = isDiagonal;
            this.formula = formula;
            this.sum = sum;
            this.rawVal = rawVal;
            this.resultVal = resultVal;
        }

        public int getRow() { return row; }
        public int getCol() { return col; }
        public boolean isDiagonal() { return isDiagonal; }
        public String getFormula() { return formula; }
        public double getSum() { return sum; }
        public double getRawVal() { return rawVal; }
        public double getResultVal() { return resultVal; }

        @Override
        public String toString() {
            if (isDiagonal) {
                return String.format("L[%d][%d] (Diagonal) = sqrt(A[%d][%d] - sum) = sqrt(%.4f - %.4f) = sqrt(%.4f) = %.4f",
                        row, col, row, col, rawVal + sum, sum, rawVal, resultVal);
            } else {
                return String.format("L[%d][%d] (Off-Diagonal) = (A[%d][%d] - sum) / L[%d][%d] = (%.4f - %.4f) / %.4f = %.4f",
                        row, col, row, col, col, col, rawVal + sum, sum, rawVal / resultVal, resultVal);
            }
        }
    }

    public static class DecompositionResult {
        private final Matrix originalA;
        private final Matrix L;
        private final Matrix LTranspose;
        private final List<CholeskyStep> steps;

        public DecompositionResult(Matrix originalA, Matrix L, List<CholeskyStep> steps) {
            this.originalA = originalA;
            this.L = L;
            this.LTranspose = L.transpose();
            this.steps = steps;
        }

        public Matrix getOriginalA() { return originalA; }
        public Matrix getL() { return L; }
        public Matrix getLTranspose() { return LTranspose; }
        public List<CholeskyStep> getSteps() { return steps; }
        
        public Matrix reconstructA() {
            return L.multiply(LTranspose);
        }

        public double getReconstructionError() {
            Matrix diff = originalA.subtract(reconstructA());
            return diff.frobeniusNorm();
        }
    }

    private final double symmetryTolerance;

    public CholeskyDecomposer() {
        this(1e-9);
    }

    public CholeskyDecomposer(double symmetryTolerance) {
        this.symmetryTolerance = symmetryTolerance;
    }

    /**
     * Decomposes matrix A into L * L^T.
     * Throws MatrixValidationException if not square or not symmetric.
     * Throws NonPositiveDefiniteException if not positive-definite.
     */
    public DecompositionResult decompose(Matrix A) {
        if (A == null) {
            throw new MatrixValidationException("Matrix cannot be null.");
        }
        if (!A.isSquare()) {
            throw new MatrixValidationException("Cholesky decomposition requires a square matrix. Given dimensions: (" + A.rows() + "x" + A.cols() + ")");
        }
        if (!A.isSymmetric(symmetryTolerance)) {
            throw new MatrixValidationException("Cholesky decomposition requires a symmetric matrix. Matrix A is non-symmetric.");
        }

        int n = A.rows();
        Matrix L = new Matrix(n, n);
        List<CholeskyStep> steps = new ArrayList<>();

        for (int j = 0; j < n; j++) {
            // Compute diagonal element L[j][j]
            double sumDiag = 0.0;
            for (int k = 0; k < j; k++) {
                double val = L.get(j, k);
                sumDiag += val * val;
            }

            double valUnderRoot = A.get(j, j) - sumDiag;
            if (valUnderRoot <= 0.0) {
                throw new NonPositiveDefiniteException(
                    "Matrix is not positive-definite: Diagonal value under square root at L[" + j + "][" + j + "] is " + String.format("%.6f", valUnderRoot) + " <= 0.",
                    j, j, valUnderRoot
                );
            }

            double ljj = Math.sqrt(valUnderRoot);
            L.set(j, j, ljj);
            steps.add(new CholeskyStep(j, j, true, "sqrt(A[" + j + "][" + j + "] - sum(L[" + j + "][k]^2))", sumDiag, valUnderRoot, ljj));

            // Compute off-diagonal elements L[i][j] for i > j
            for (int i = j + 1; i < n; i++) {
                double sumOff = 0.0;
                for (int k = 0; k < j; k++) {
                    sumOff += L.get(i, k) * L.get(j, k);
                }

                double lij = (A.get(i, j) - sumOff) / ljj;
                L.set(i, j, lij);
                steps.add(new CholeskyStep(i, j, false, "(A[" + i + "][" + j + "] - sum(L[" + i + "][k]*L[" + j + "][k])) / L[" + j + "][" + j + "]", sumOff, A.get(i, j) - sumOff, lij));
            }
        }

        return new DecompositionResult(A, L, steps);
    }
}
