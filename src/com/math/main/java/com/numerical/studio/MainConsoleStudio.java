package com.numerical.studio;

import com.numerical.studio.engine.CholeskyDecomposer;
import com.numerical.studio.engine.LinearSystemSolver;
import com.numerical.studio.engine.NumericalAnalyzer;
import com.numerical.studio.exception.MatrixValidationException;
import com.numerical.studio.exception.NonPositiveDefiniteException;
import com.numerical.studio.model.Matrix;
import com.numerical.studio.model.Vector;

import java.util.Scanner;

public class MainConsoleStudio {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Initializing Numerical Computing & Cholesky Analysis Studio (Java Core)...");

        while (true) {
            System.out.println("\n========================================================");
            System.out.println("  NUMERICAL COMPUTING & CHOLESKY ANALYSIS STUDIO (JAVA) ");
            System.out.println("========================================================");
            System.out.println(" 1. Decompose Preset 3x3 Covariance Matrix (A = L L^T)");
            System.out.println(" 2. Decompose Preset 4x4 FEA Stiffness Matrix");
            System.out.println(" 3. Solve Linear System A x = b (Forward & Back Substitution)");
            System.out.println(" 4. Enter Custom Matrix Manually");
            System.out.println(" 5. Test Invalid Matrix Rejection (Non-Symmetric & Non-SPD)");
            System.out.println(" 6. Run Unit Test Suite (Automated 15+ Checks)");
            System.out.println(" 7. Exit Studio Application");
            System.out.println("========================================================");
            System.out.print(" Select choice (1-7): ");

            if (!scanner.hasNextInt()) break;
            int choice = scanner.nextInt();

            if (choice == 1) {
                Matrix a = new Matrix(new double[][]{
                    {25.0, 15.0, -5.0},
                    {15.0, 18.0,  0.0},
                    {-5.0,  0.0, 11.0}
                });
                runDecompositionDemo(a);
            } else if (choice == 2) {
                Matrix a = new Matrix(new double[][]{
                    { 4.0, -1.0,  0.0,  0.0},
                    {-1.0,  4.0, -1.0,  0.0},
                    { 0.0, -1.0,  4.0, -1.0},
                    { 0.0,  0.0, -1.0,  4.0}
                });
                runDecompositionDemo(a);
            } else if (choice == 3) {
                Matrix a = new Matrix(new double[][]{
                    {4.0, 12.0},
                    {12.0, 37.0}
                });
                Vector b = new Vector(new double[]{16.0, 49.0});
                runLinearSolveDemo(a, b);
            } else if (choice == 4) {
                runManualEntryDemo(scanner);
            } else if (choice == 5) {
                runInvalidRejectionDemo();
            } else if (choice == 6) {
                CholeskyTestRunner.main(new String[]{});
            } else if (choice == 7) {
                System.out.println("\nExiting Numerical Studio. Goodbye!\n");
                break;
            }
        }
    }

    private static void runDecompositionDemo(Matrix A) {
        System.out.println("\n--- ORIGINAL MATRIX A ---");
        System.out.print(A.formatMatrix());

        try {
            CholeskyDecomposer decomposer = new CholeskyDecomposer();
            CholeskyDecomposer.DecompositionResult res = decomposer.decompose(A);

            System.out.println("\n--- LOWER TRIANGULAR MATRIX L ---");
            System.out.print(res.getL().formatMatrix());

            System.out.println("\n--- TRANSPOSE MATRIX L^T ---");
            System.out.print(res.getLTranspose().formatMatrix());

            System.out.println("\n--- RECONSTRUCTED MATRIX L * L^T ---");
            System.out.print(res.reconstructA().formatMatrix());

            System.out.println("\n--- STEP-BY-STEP CALCULATION TRACE ---");
            for (CholeskyDecomposer.CholeskyStep step : res.getSteps()) {
                System.out.println("  " + step);
            }

            NumericalAnalyzer.AnalysisReport report = NumericalAnalyzer.analyze(A, res.getL(), null, null);
            System.out.print(report.formatReport());

        } catch (Exception e) {
            System.out.println(" [ERROR] " + e.getMessage());
        }
    }

    private static void runLinearSolveDemo(Matrix A, Vector b) {
        System.out.println("\n--- MATRIX A ---");
        System.out.print(A.formatMatrix());
        System.out.println("--- VECTOR b ---");
        System.out.println(b.formatVector());

        try {
            LinearSystemSolver solver = new LinearSystemSolver();
            LinearSystemSolver.LinearSolveResult res = solver.solve(A, b);

            System.out.println("\n--- FORWARD SUBSTITUTION (L y = b) ---");
            for (String step : res.getForwardSteps()) {
                System.out.println("  " + step);
            }
            System.out.println(" Intermediate Vector y = " + res.getY().formatVector());

            System.out.println("\n--- BACK SUBSTITUTION (L^T x = y) ---");
            for (String step : res.getBackSteps()) {
                System.out.println("  " + step);
            }
            System.out.println("\n SOLUTION VECTOR x = " + res.getX().formatVector());
            System.out.println(" RESIDUAL VECTOR r = " + res.getResidual().formatVector());
            System.out.printf(" RESIDUAL NORM ||Ax - b||_2 = %.6e\n", res.getResidualNorm());

        } catch (Exception e) {
            System.out.println(" [ERROR] " + e.getMessage());
        }
    }

    private static void runManualEntryDemo(Scanner scanner) {
        System.out.print("\nEnter matrix dimension n (e.g. 2, 3, 4): ");
        int n = scanner.nextInt();
        double[][] data = new double[n][n];

        System.out.println("Enter matrix values row by row:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("A[%d][%d] = ", i, j);
                data[i][j] = scanner.nextDouble();
            }
        }
        Matrix customA = new Matrix(data);
        runDecompositionDemo(customA);
    }

    private static void runInvalidRejectionDemo() {
        System.out.println("\n1. Testing Non-Symmetric Matrix Rejection...");
        try {
            Matrix nonSym = new Matrix(new double[][]{{4.0, 5.0}, {2.0, 4.0}});
            new CholeskyDecomposer().decompose(nonSym);
        } catch (MatrixValidationException e) {
            System.out.println(" [CAUGHT EXPECTED EXCEPTION]: " + e.getMessage());
        }

        System.out.println("\n2. Testing Non-Positive Definite Matrix Rejection...");
        try {
            Matrix nonSPD = new Matrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
            new CholeskyDecomposer().decompose(nonSPD);
        } catch (NonPositiveDefiniteException e) {
            System.out.println(" [CAUGHT EXPECTED EXCEPTION]: " + e.getMessage());
        }
    }
}
