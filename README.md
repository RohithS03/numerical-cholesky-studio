# 🔢 Numerical Cholesky Studio

> **High-Precision Matrix Factorization & Linear Algebra Solver Engine in Java & Web Studio**

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk)
![Linear Algebra](https://img.shields.io/badge/Math-Linear%20Algebra-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

## 📌 Overview

**Numerical Cholesky Studio** is a mathematical linear algebra engine implementing **Cholesky Decomposition** ($A = L L^T$) for symmetric positive-definite matrices. It solves systems of linear equations $A x = b$ with lower computational complexity than standard LU decomposition.

---

## ✨ Features & Solvers

- 📐 **Cholesky Decomposition Engine:** Factoring $n \times n$ symmetric positive-definite matrices.
- ⚡ **Forward & Back Substitution:** Solving systems $L y = b$ and $L^T x = y$.
- 🌐 **Interactive Web Matrix Studio:** Visual matrix entry and step-by-step triangular factor visualization.

---

## 🚀 Quick Start Guide

```bash
git clone https://github.com/RohithS03/numerical-cholesky-studio.git
cd numerical-cholesky-studio

javac -d bin src/com/math/*.java
java -cp bin com.math.CholeskyRunner
```

---

## 📜 License
Licensed under the [MIT License](LICENSE).
