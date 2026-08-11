package com.numerical.studio.model;

import com.numerical.studio.exception.MatrixValidationException;
import java.util.Arrays;

/**
 * Represents a 2D dense matrix and encapsulates linear algebra operations.
 */
public class Matrix {
    private final int rows;
    private final int cols;
    private final double[][] data;

    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new MatrixValidationException("Matrix dimensions must be positive integers.");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            throw new MatrixValidationException("Matrix data cannot be empty or null.");
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i].length != cols) {
                throw new MatrixValidationException("All rows in a matrix must have equal column lengths.");
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    public int rows() { return rows; }
    public int cols() { return cols; }

    public double get(int r, int c) {
        checkBounds(r, c);
        return data[r][c];
    }

    public void set(int r, int c, double value) {
        checkBounds(r, c);
        data[r][c] = value;
    }

    private void checkBounds(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            throw new IndexOutOfBoundsException("Matrix index out of bounds: (" + r + ", " + c + ")");
        }
    }

    public boolean isSquare() {
        return rows == cols;
    }

    public boolean isSymmetric(double tolerance) {
        if (!isSquare()) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < cols; j++) {
                if (Math.abs(data[i][j] - data[j][i]) > tolerance) {
                    return false;
                }
            }
        }
        return true;
    }

    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(j, i, data[i][j]);
            }
        }
        return result;
    }

    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows) {
            throw new MatrixValidationException("Matrix dimension mismatch for multiplication: (" + rows + "x" + cols + ") * (" + other.rows + "x" + other.cols + ")");
        }
        Matrix result = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int k = 0; k < this.cols; k++) {
                for (int j = 0; j < other.cols; j++) {
                    result.data[i][j] += this.data[i][k] * other.data[k][j];
                }
            }
        }
        return result;
    }

    public Vector multiply(Vector vector) {
        if (this.cols != vector.size()) {
            throw new MatrixValidationException("Matrix-vector dimension mismatch: (" + rows + "x" + cols + ") * (" + vector.size() + ")");
        }
        double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            double sum = 0.0;
            for (int j = 0; j < cols; j++) {
                sum += data[i][j] * vector.get(j);
            }
            result[i] = sum;
        }
        return new Vector(result);
    }

    public Matrix subtract(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new MatrixValidationException("Matrix dimension mismatch for subtraction.");
        }
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = this.data[i][j] - other.data[i][j];
            }
        }
        return result;
    }

    public double frobeniusNorm() {
        double sumSquare = 0.0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sumSquare += data[i][j] * data[i][j];
            }
        }
        return Math.sqrt(sumSquare);
    }

    public double[][] toArray() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    public String formatMatrix() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%10.4f ", data[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return formatMatrix();
    }
}
