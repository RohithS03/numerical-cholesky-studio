package com.numerical.studio.model;

import com.numerical.studio.exception.MatrixValidationException;
import java.util.Arrays;

/**
 * Represents a 1D dense vector for linear system solving.
 */
public class Vector {
    private final int size;
    private final double[] data;

    public Vector(int size) {
        if (size <= 0) {
            throw new MatrixValidationException("Vector size must be a positive integer.");
        }
        this.size = size;
        this.data = new double[size];
    }

    public Vector(double[] data) {
        if (data == null || data.length == 0) {
            throw new MatrixValidationException("Vector data cannot be empty or null.");
        }
        this.size = data.length;
        this.data = new double[size];
        System.arraycopy(data, 0, this.data, 0, size);
    }

    public int size() { return size; }

    public double get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Vector index out of bounds: " + index);
        }
        return data[index];
    }

    public void set(int index, double value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Vector index out of bounds: " + index);
        }
        data[index] = value;
    }

    public double dot(Vector other) {
        if (this.size != other.size) {
            throw new MatrixValidationException("Vector size mismatch for dot product.");
        }
        double sum = 0.0;
        for (int i = 0; i < size; i++) {
            sum += this.data[i] * other.data[i];
        }
        return sum;
    }

    public double norm2() {
        return Math.sqrt(dot(this));
    }

    public Vector subtract(Vector other) {
        if (this.size != other.size) {
            throw new MatrixValidationException("Vector size mismatch for subtraction.");
        }
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = this.data[i] - other.data[i];
        }
        return new Vector(result);
    }

    public double[] toArray() {
        double[] copy = new double[size];
        System.arraycopy(data, 0, copy, 0, size);
        return copy;
    }

    public String formatVector() {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%10.4f ", data[i]));
        }
        sb.append("]^T");
        return sb.toString();
    }

    @Override
    public String toString() {
        return formatVector();
    }
}
