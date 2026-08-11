# Numerical Computing & Cholesky Analysis Studio

An educational numerical computing application and interactive web studio implementing **Cholesky Decomposition** ($A = L L^T$), Symmetric Positive-Definite (SPD) validation, linear system solving ($A x = b$), numerical stability metrics, and step-by-step mathematical calculation traces.

---

## Technical Architecture & File Structure

This repository provides a dual-architecture implementation:
1. **Pure Java Numerical Engine (`src/main/java/`, `src/test/java/`)**: Core linear algebra package implementing dense 2D matrices, dense vectors, Cholesky factorization, forward substitution, back substitution, error metrics, exception handling, and automated unit test suite.
2. **Interactive Academic Web Studio (`index.html`, `css/`, `js/`)**: Dark-mode web interface featuring interactive matrix grid editors ($1 \times 1$ to $8 \times 8$), visual side-by-side matrix display ($A$, $L$, $L^T$, $L L^T$), step-by-step calculation animators, linear system solver, numerical analysis suite, and real-world application context explorer.

```
d:/R O H I T H/GitHub_Repos/numerical-cholesky-studio/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── numerical/
│   │               └── studio/
│   │                   ├── model/
│   │                   │   ├── Matrix.java              # Dense 2D matrix ops, Frobenius norm, symmetry check
│   │                   │   └── Vector.java              # Dense 1D vector ops, dot product, L2 norm
│   │                   ├── engine/
│   │                   │   ├── CholeskyDecomposer.java  # Core A = L L^T algorithm & step recorder
│   │                   │   ├── LinearSystemSolver.java  # Forward/back substitution solver for Ax = b
│   │                   │   └── NumericalAnalyzer.java   # Residual, reconstruction error & FLOPS calculator
│   │                   ├── exception/
│   │                   │   ├── MatrixValidationException.java
│   │                   │   └── NonPositiveDefiniteException.java
│   │                   └── MainConsoleStudio.java       # Interactive CLI console application
│   └── test/
│       └── java/
│           └── com/
│               └── numerical/
│                   └── studio/
│                       └── CholeskyTestRunner.java     # 16 Automated Java Unit Test Cases
│
├── Makefile                     # Build & execution script
├── pom.xml                      # Maven build configuration
│
├── index.html                   # Interactive Web Studio Entry Point
├── css/
│   └── styles.css               # Dark-mode glassmorphic styling system
└── js/
    ├── app.js                   # Web UI controller & grid manager
    ├── cholesky_engine.js       # JS numerical engine & step tracer
    └── matrix_renderer.js       # Visual matrix layout renderer
```

---

## Verification Plan

### 1. Automated Java Unit Test Verification
Compile and run the 16 automated Java unit test cases using `javac` and `java`:

```powershell
# Locate JDK (or set JAVA_HOME) and compile source files
$javac = "C:\Users\rohit\.antigravity\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64\bin\javac.exe"
$java  = "C:\Users\rohit\.antigravity\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64\bin\java.exe"

if (!(Test-Path bin)) { New-Item -ItemType Directory -Path bin }
& $javac -d bin (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)

# Execute Automated Test Runner
& $java -cp bin com.numerical.studio.CholeskyTestRunner
```

Expected Test Output:
```
========================================================
      CHOLESKY NUMERICAL COMPUTING UNIT TEST SUITE      
========================================================
 [PASS] 1x1 Matrix decomposition L[0][0] == 4.0
 [PASS] 1x1 Matrix reconstruction error < 1e-12
 [PASS] 2x2 SPD L[0][0] == 2.0
 [PASS] 2x2 SPD L[1][0] == 6.0
 [PASS] 2x2 SPD L[1][1] == 1.0
 [PASS] 2x2 SPD reconstruction error < 1e-12
 [PASS] 3x3 Covariance matrix reconstruction error < 1e-12
 [PASS] 4x4 FEA stiffness matrix reconstruction error < 1e-12
 [PASS] Reject non-symmetric matrix with MatrixValidationException
 [PASS] Reject non-positive-definite matrix with NonPositiveDefiniteException
 [PASS] Reject zero diagonal element with NonPositiveDefiniteException
 [PASS] Linear solver Ax=b x[0] == 1.0
 [PASS] Linear solver Ax=b x[1] == 1.0
 [PASS] Linear solver residual norm ||Ax - b|| < 1e-12
 [PASS] NumericalAnalyzer calculates Cholesky FLOPS > 0
 [PASS] NumericalAnalyzer calculates Stability Grade
========================================================
 TEST RESULTS SUMMARY: 16 PASSED, 0 FAILED
========================================================
```

### 2. Main Java Console Studio Execution
Launch the interactive CLI menu:

```powershell
& $java -cp bin com.numerical.studio.MainConsoleStudio
```

### 3. Interactive Web Studio Application Verification
1. Open [`index.html`](file:///d:/R%20O%20H%20I%20T%20H/GitHub_Repos/numerical-cholesky-studio/index.html) in any modern web browser.
2. Navigate through the sidebar tabs:
   - **Dashboard**: View matrix metrics and quick presets.
   - **Matrix Workspace**: Edit cells in real-time ($1 \times 1$ to $8 \times 8$) and inspect SPD validation badges.
   - **Cholesky Factorizer**: Inspect side-by-side matrices $A$, $L$, $L^T$, and reconstructed $L L^T$.
   - **Linear Solver ($A x = b$)**: Inspect forward substitution ($L y = b$), back substitution ($L^T x = y$), solution vector $x$, and residual norm $\|Ax - b\|_2 < 10^{-12}$.
   - **Step-by-Step Trace**: Review formula substitutions for every cell $L_{i,j}$.
   - **Application Contexts**: Review academic application contexts.
   - **Numerical Analysis**: Review FLOPS comparison cards ($(1/3)n^3$ FLOPS).
   - **Testing Suite**: Verify 16/16 test pass badges.

---

## Mathematical Formulation & Algorithm

For a Symmetric Positive-Definite (SPD) matrix $A \in \mathbb{R}^{n \times n}$, Cholesky decomposition factors $A$ into a lower triangular matrix $L$ such that:

$$A = L L^T$$

### Diagonal Elements ($j = 0, \dots, n-1$)
$$L_{j,j} = \sqrt{A_{j,j} - \sum_{k=0}^{j-1} L_{j,k}^2}$$

### Off-Diagonal Elements ($i = j+1, \dots, n-1$)
$$L_{i,j} = \frac{A_{i,j} - \sum_{k=0}^{j-1} L_{i,k} L_{j,k}}{L_{j,j}}$$

### Linear System Solving ($A x = b$)
1. **Forward Substitution** ($L y = b$):
   $$y_i = \frac{b_i - \sum_{k=0}^{i-1} L_{i,k} y_k}{L_{i,i}}$$
2. **Back Substitution** ($L^T x = y$):
   $$x_i = \frac{y_i - \sum_{k=i+1}^{n-1} L_{k,i} x_k}{L_{i,i}}$$

---

## Computational Complexity & Numerical Stability

- **FLOPS Complexity**:
  $$\text{FLOPS} = \frac{1}{3} n^3 + \frac{1}{2} n^2 + \frac{1}{6} n \quad \text{operations} + n \text{ square roots}$$
- **Efficiency**: Requires roughly **half** the floating-point operations of standard LU decomposition ($\frac{2}{3} n^3$ FLOPS).
- **Numerical Stability**: Inherently stable for SPD matrices; pivoting is not required.

---

## Real-World Application Contexts

| Domain | Application Context | Implemented Functionality |
| :--- | :--- | :--- |
| **Finite Element Analysis (FEA)** | Solving global structural stiffness equations $K u = F$. | **Directly Implemented**: Solves SPD linear system $Ax = b$ via Cholesky factor. |
| **Portfolio Optimization** | Generating correlated asset return vectors for Monte Carlo simulation ($X = L Z$). | **Implemented Matrix Factorization**: Computes Cholesky factor $L$ of covariance matrix $\Sigma$. |
| **Machine Learning** | Gaussian Process regression marginal likelihood evaluation. | **Implemented Linear Algebra Engine**: Computes $L$ and triangular solves. |
| **Signal Processing** | Adaptive Kalman filtering and noise covariance updates. | **Implemented Linear Algebra Engine**: Matrix factorization & residual checks. |

---

## Limitations & Future Extensions

1. **Memory Format**: Dense 2D arrays are used; future extensions can introduce sparse matrix formats (e.g. Compressed Sparse Row / Column) for large-scale $1000 \times 1000+$ sparse systems.
2. **Parallelization**: Future revisions can introduce multithreaded block Cholesky algorithms using Java Fork/Join or Parallel Streams for large matrices.
