#pragma once

// when calling functions which take an index of a row/column, the indices start at 0 and end at the number of rows-1/columns-1

class DenseMatrix {
private:
    int rows, cols;
    double** values;

    DenseMatrix(int rows, int cols);

    DenseMatrix(int rows, int cols, double initializationValue);

public:
    ~DenseMatrix();

    static DenseMatrix* zeroMatrix(int row, int col);
    static DenseMatrix* oneMatrix(int row, int col);

    static DenseMatrix* identityMatrix(int n);

    static DenseMatrix* copyMatrix(DenseMatrix* source);

    static DenseMatrix* negatedMatrix(DenseMatrix* source);

    // solves the equation DX=B, where D is diagonal and square
    static DenseMatrix* diagonalSolve(DenseMatrix* D, DenseMatrix* B);

    // solves the equation LX=B, where L is a sum of a diagonal and a lower matrix and is square
    static DenseMatrix* forwardSubstitutionSolve(DenseMatrix* L, DenseMatrix* B);

    // solves the equation UX=B, where U is a sum of a diagonal and an upper matrix and is square
    static DenseMatrix* backwardSubstitutionSolve(DenseMatrix* U, DenseMatrix* B);

    static DenseMatrix* residualVector(DenseMatrix* A, DenseMatrix* x, DenseMatrix* b);

    static DenseMatrix* diagonalMatrix(DenseMatrix* source);

    static DenseMatrix* lowerTriangularMatrix(DenseMatrix* source);
    static DenseMatrix* upperTriangularMatrix(DenseMatrix* source);

    static DenseMatrix* matrixProduct(DenseMatrix* m1, DenseMatrix* m2);

    int getRows();
    int getColumns();

    bool isIndexInRange(int row, int col);

    double getValue(int row, int col);
    void setValue(int row, int col, double value);

    void add(DenseMatrix* other);
    void subtract(DenseMatrix* other);

    void fillDiagonal(int diag, double value);

    void show();

    bool isVector();
    bool isDiagonal();
    bool isDiagonalFull();
    bool isSquare();

    double euclideanNorm();

    void negate();


};

