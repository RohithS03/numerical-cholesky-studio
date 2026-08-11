package com.numerical.studio.exception;

/**
 * Thrown when a matrix is not Symmetric Positive-Definite (SPD),
 * causing Cholesky decomposition to fail due to zero/negative values inside the square root.
 */
public class NonPositiveDefiniteException extends RuntimeException {
    private final int failureRow;
    private final int failureCol;
    private final double valueUnderRoot;

    public NonPositiveDefiniteException(String message, int failureRow, int failureCol, double valueUnderRoot) {
        super(message);
        this.failureRow = failureRow;
        this.failureCol = failureCol;
        this.valueUnderRoot = valueUnderRoot;
    }

    public int getFailureRow() { return failureRow; }
    public int getFailureCol() { return failureCol; }
    public double getValueUnderRoot() { return valueUnderRoot; }
}
