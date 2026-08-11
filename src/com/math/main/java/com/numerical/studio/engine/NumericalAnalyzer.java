package com.numerical.studio.engine;

import com.numerical.studio.model.Matrix;
import com.numerical.studio.model.Vector;

/**
 * Calculates numerical error metrics, FLOPS complexity, and stability metrics for Cholesky Decomposition.
 */
public class NumericalAnalyzer {

    public static class AnalysisReport {
        private final int matrixDimension;
        private final long choleskyFlops;
        private final long luFlops;
        private final double reconstructionError;
        private final double residualNorm;
        private final String stabilityGrade;

        public AnalysisReport(int dimension, long choleskyFlops, long luFlops, double reconstructionError, double residualNorm) {
            this.matrixDimension = dimension;
            this.choleskyFlops = choleskyFlops;
            this.luFlops = luFlops;
            this.reconstructionError = reconstructionError;
            this.residualNorm = residualNorm;
            if (reconstructionError < 1e-12 && residualNorm < 1e-12) {
                this.stabilityGrade = "EXCELLENT (Machine Precision ~ 1e-16)";
            } else if (reconstructionError < 1e-8 && residualNorm < 1e-8) {
                this.stabilityGrade = "GOOD (High Numerical Accuracy)";
            } else {
                this.stabilityGrade = "ACCEPTABLE (Potential Ill-Conditioning)";
            }
        }

        public int getMatrixDimension() { return matrixDimension; }
        public long getCholeskyFlops() { return choleskyFlops; }
        public long getLuFlops() { return luFlops; }
        public double getReconstructionError() { return reconstructionError; }
        public double getResidualNorm() { return residualNorm; }
        public String getStabilityGrade() { return stabilityGrade; }

        public String formatReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n========================================================\n");
            sb.append("         CHOLESKY NUMERICAL ANALYSIS REPORT            \n");
            sb.append("========================================================\n");
            sb.append(String.format(" Matrix Dimension (n x n)   : %d x %d\n", matrixDimension, matrixDimension));
            sb.append(String.format(" Cholesky FLOPS Complexity  : %d FLOPS (~ (1/3) n^3)\n", choleskyFlops));
            sb.append(String.format(" LU Decomposition Complexity: %d FLOPS (~ (2/3) n^3)\n", luFlops));
            sb.append(String.format(" Efficiency Advantage       : 2x Faster than LU Decomposition\n"));
            sb.append(String.format(" Reconstruction Error ||A-LL^T||_F: %.6e\n", reconstructionError));
            sb.append(String.format(" Residual Error norm ||Ax-b||_2   : %.6e\n", residualNorm));
            sb.append(String.format(" Numerical Stability Grade  : %s\n", stabilityGrade));
            sb.append("========================================================\n\n");
            return sb.toString();
        }
    }

    public static AnalysisReport analyze(Matrix A, Matrix L, Vector x, Vector b) {
        int n = A.rows();
        
        // FLOPS for Cholesky: n^3/3 + n^2/2 + n/6 additions/multiplications
        long choleskyFlops = (long) (Math.pow(n, 3) / 3.0 + Math.pow(n, 2) / 2.0 + n / 6.0);
        long luFlops = (long) (2.0 * Math.pow(n, 3) / 3.0);

        Matrix diff = A.subtract(L.multiply(L.transpose()));
        double recErr = diff.frobeniusNorm();

        double resNorm = 0.0;
        if (x != null && b != null) {
            Vector r = A.multiply(x).subtract(b);
            resNorm = r.norm2();
        }

        return new AnalysisReport(n, choleskyFlops, luFlops, recErr, resNorm);
    }
}
