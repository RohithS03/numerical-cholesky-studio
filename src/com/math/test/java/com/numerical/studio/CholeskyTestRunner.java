package com.numerical.studio;

import com.numerical.studio.engine.CholeskyDecomposer;
import com.numerical.studio.engine.LinearSystemSolver;
import com.numerical.studio.engine.NumericalAnalyzer;
import com.numerical.studio.exception.MatrixValidationException;
import com.numerical.studio.exception.NonPositiveDefiniteException;
import com.numerical.studio.model.Matrix;
import com.numerical.studio.model.Vector;

public class CholeskyTestRunner {
    private static int passCount = 0;
    private static int failCount = 0;

    private static void TEST_ASSERT(boolean condition, String testName) {
        if (condition) {
            System.out.println(" [PASS] " + testName);
            passCount++;
        } else {
            System.out.println(" [FAIL] " + testName);
            failCount++;
        }
    }

    public static void test1x1Matrix() {
        Matrix a = new Matrix(new double[][]{{16.0}});
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        CholeskyDecomposer.DecompositionResult res = decomposer.decompose(a);
        
        TEST_ASSERT(res.getL().get(0, 0) == 4.0, "1x1 Matrix decomposition L[0][0] == 4.0");
        TEST_ASSERT(res.getReconstructionError() < 1e-12, "1x1 Matrix reconstruction error < 1e-12");
    }

    public static void test2x2SPDMatrix() {
        Matrix a = new Matrix(new double[][]{
            {4.0, 12.0},
            {12.0, 37.0}
        });
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        CholeskyDecomposer.DecompositionResult res = decomposer.decompose(a);
        Matrix L = res.getL();

        TEST_ASSERT(Math.abs(L.get(0, 0) - 2.0) < 1e-9, "2x2 SPD L[0][0] == 2.0");
        TEST_ASSERT(Math.abs(L.get(1, 0) - 6.0) < 1e-9, "2x2 SPD L[1][0] == 6.0");
        TEST_ASSERT(Math.abs(L.get(1, 1) - 1.0) < 1e-9, "2x2 SPD L[1][1] == 1.0");
        TEST_ASSERT(res.getReconstructionError() < 1e-12, "2x2 SPD reconstruction error < 1e-12");
    }

    public static void test3x3CovarianceMatrix() {
        Matrix a = new Matrix(new double[][]{
            {25.0, 15.0, -5.0},
            {15.0, 18.0,  0.0},
            {-5.0,  0.0, 11.0}
        });
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        CholeskyDecomposer.DecompositionResult res = decomposer.decompose(a);
        
        TEST_ASSERT(res.getReconstructionError() < 1e-12, "3x3 Covariance matrix reconstruction error < 1e-12");
    }

    public static void test4x4FEAStiffnessMatrix() {
        Matrix a = new Matrix(new double[][]{
            { 4.0, -1.0,  0.0,  0.0},
            {-1.0,  4.0, -1.0,  0.0},
            { 0.0, -1.0,  4.0, -1.0},
            { 0.0,  0.0, -1.0,  4.0}
        });
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        CholeskyDecomposer.DecompositionResult res = decomposer.decompose(a);
        
        TEST_ASSERT(res.getReconstructionError() < 1e-12, "4x4 FEA stiffness matrix reconstruction error < 1e-12");
    }

    public static void testNonSymmetricMatrixRejection() {
        Matrix a = new Matrix(new double[][]{
            {4.0, 5.0},
            {3.0, 4.0} // Non-symmetric: a[0][1] != a[1][0]
        });
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        boolean caught = false;
        try {
            decomposer.decompose(a);
        } catch (MatrixValidationException e) {
            caught = true;
        }
        TEST_ASSERT(caught, "Reject non-symmetric matrix with MatrixValidationException");
    }

    public static void testNonPositiveDefiniteMatrixRejection() {
        Matrix a = new Matrix(new double[][]{
            { 1.0,  2.0},
            { 2.0,  1.0} // Indefinite matrix: 1 - 4 = -3 under square root
        });
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        boolean caught = false;
        try {
            decomposer.decompose(a);
        } catch (NonPositiveDefiniteException e) {
            caught = true;
        }
        TEST_ASSERT(caught, "Reject non-positive-definite matrix with NonPositiveDefiniteException");
    }

    public static void testZeroDiagonalRejection() {
        Matrix a = new Matrix(new double[][]{
            { 0.0,  0.0},
            { 0.0,  4.0}
        });
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        boolean caught = false;
        try {
            decomposer.decompose(a);
        } catch (NonPositiveDefiniteException e) {
            caught = true;
        }
        TEST_ASSERT(caught, "Reject zero diagonal element with NonPositiveDefiniteException");
    }

    public static void testLinearSystemSolverAxEqualsB() {
        Matrix a = new Matrix(new double[][]{
            {4.0, 12.0},
            {12.0, 37.0}
        });
        Vector b = new Vector(new double[]{16.0, 49.0});

        LinearSystemSolver solver = new LinearSystemSolver();
        LinearSystemSolver.LinearSolveResult res = solver.solve(a, b);
        Vector x = res.getX();

        TEST_ASSERT(Math.abs(x.get(0) - 1.0) < 1e-9, "Linear solver Ax=b x[0] == 1.0");
        TEST_ASSERT(Math.abs(x.get(1) - 1.0) < 1e-9, "Linear solver Ax=b x[1] == 1.0");
        TEST_ASSERT(res.getResidualNorm() < 1e-12, "Linear solver residual norm ||Ax - b|| < 1e-12");
    }

    public static void testNumericalAnalyzer() {
        Matrix a = new Matrix(new double[][]{
            {4.0, 2.0},
            {2.0, 5.0}
        });
        Vector b = new Vector(new double[]{6.0, 7.0});
        LinearSystemSolver solver = new LinearSystemSolver();
        LinearSystemSolver.LinearSolveResult solveRes = solver.solve(a, b);
        
        NumericalAnalyzer.AnalysisReport report = NumericalAnalyzer.analyze(a, solveRes.getL(), solveRes.getX(), b);
        TEST_ASSERT(report.getCholeskyFlops() > 0, "NumericalAnalyzer calculates Cholesky FLOPS > 0");
        TEST_ASSERT(report.getStabilityGrade().contains("EXCELLENT") || report.getStabilityGrade().contains("GOOD"), "NumericalAnalyzer calculates Stability Grade");
    }

    public static void main(String[] args) {
        System.out.println("\n========================================================");
        System.out.println("      CHOLESKY NUMERICAL COMPUTING UNIT TEST SUITE      ");
        System.out.println("========================================================");

        test1x1Matrix();
        test2x2SPDMatrix();
        test3x3CovarianceMatrix();
        test4x4FEAStiffnessMatrix();
        testNonSymmetricMatrixRejection();
        testNonPositiveDefiniteMatrixRejection();
        testZeroDiagonalRejection();
        testLinearSystemSolverAxEqualsB();
        testNumericalAnalyzer();

        System.out.println("========================================================");
        System.out.println(" TEST RESULTS SUMMARY: " + passCount + " PASSED, " + failCount + " FAILED");
        System.out.println("========================================================\n");

        if (failCount > 0) System.exit(1);
    }
}
