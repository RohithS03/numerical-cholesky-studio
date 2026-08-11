package com.numerical.studio.exception;

/**
 * Thrown when a matrix fails structural or mathematical validation rules
 * (e.g., non-square dimensions, dimension mismatch, or non-symmetric values).
 */
public class MatrixValidationException extends RuntimeException {
    public MatrixValidationException(String message) {
        super(message);
    }
}
