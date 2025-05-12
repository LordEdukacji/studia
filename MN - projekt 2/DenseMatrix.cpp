#include "DenseMatrix.h"

#pragma once

#include <iostream>

// when calling functions which take an index of a row/column, the indices start at 0 and end at the number of rows-1/columns-1



DenseMatrix::DenseMatrix(int rows, int cols) : rows(rows), cols(cols) {
    values = new double* [rows];

    for (int i = 0; i < rows; i++) {
        values[i] = new double[cols];
    }
}

DenseMatrix::DenseMatrix(int rows, int cols, double initializationValue) : rows(rows), cols(cols) {
    values = new double* [rows];

    for (int i = 0; i < rows; i++) {
        values[i] = new double[cols];

        for (int j = 0; j < cols; j++) {
            values[i][j] = initializationValue;
        }
    }
}

DenseMatrix::~DenseMatrix() {
    for (int i = 0; i < rows; i++) {
        delete values[i];
    }
    delete values;
}

DenseMatrix* DenseMatrix::zeroMatrix(int row, int col) {
    return new DenseMatrix(row, col, 0);
}

DenseMatrix* DenseMatrix::oneMatrix(int row, int col) {
    return new DenseMatrix(row, col, 1);
}

DenseMatrix* DenseMatrix::identityMatrix(int n)
{
    DenseMatrix* id = zeroMatrix(n, n);

    id->fillDiagonal(0, 1);

    return id;
}

DenseMatrix* DenseMatrix::copyMatrix(DenseMatrix* source) {
    DenseMatrix* newMatrix = new DenseMatrix(source->rows, source->cols);

    for (int i = 0; i < newMatrix->rows; i++) {
        for (int j = 0; j < newMatrix->cols; j++) {
            newMatrix->values[i][j] = source->values[i][j];
        }
    }

    return newMatrix;
}

DenseMatrix* DenseMatrix::negatedMatrix(DenseMatrix* source)
{
    DenseMatrix* newMatrix = new DenseMatrix(source->rows, source->cols);

    for (int i = 0; i < newMatrix->rows; i++) {
        for (int j = 0; j < newMatrix->cols; j++) {
            newMatrix->values[i][j] = -source->values[i][j];
        }
    }

    return newMatrix;
}

DenseMatrix* DenseMatrix::diagonalSolve(DenseMatrix* D, DenseMatrix* B) {
    if (D->rows != B->rows) throw;

    if (!D->isDiagonal()) throw;
    if (!D->isDiagonalFull()) throw;
    if (!D->isSquare()) throw;

    DenseMatrix* X = zeroMatrix(D->cols, B->cols);

    for (int i = 0; i < X->rows; i++) {
        for (int j = 0; j < X->cols; j++) {
            X->values[i][j] = B->values[i][j] / D->values[i][i];    // safe!
        }
    }

    return X;
}

DenseMatrix* DenseMatrix::forwardSubstitutionSolve(DenseMatrix* L, DenseMatrix* B)
{
    if (L->rows != B->rows) throw;

    if (!L->isSquare()) throw;
    if (!L->isDiagonalFull()) throw;

    DenseMatrix* X = zeroMatrix(L->cols, B->cols);

    double sum;

    for (int i = 0; i < X->rows; i++) {
        for (int j = 0; j < X->cols; j++) {
            sum = 0;

            for (int k = 0; k < i; k++) {
                sum += X->values[k][j] * L->values[i][k];
            }

            X->values[i][j] = (B->values[i][j] - sum) / L->values[i][i];
        }
    }

    return X;
}

DenseMatrix* DenseMatrix::backwardSubstitutionSolve(DenseMatrix* U, DenseMatrix* B)
{
    if (U->rows != B->rows) throw;

    if (!U->isSquare()) throw;
    if (!U->isDiagonalFull()) throw;

    DenseMatrix* X = zeroMatrix(U->cols, B->cols);

    double sum;

    for (int i = X->rows - 1; i >= 0; i--) {
        for (int j = X->cols - 1; j >= 0; j--) {
            sum = 0;

            for (int k = i + 1; k < X->rows; k++) {
                sum += X->values[k][j] * U->values[i][k];
            }

            X->values[i][j] = (B->values[i][j] - sum) / U->values[i][i];
        }
    }

    return X;
}

DenseMatrix* DenseMatrix::residualVector(DenseMatrix* A, DenseMatrix* x, DenseMatrix* b) {
    DenseMatrix* res = matrixProduct(A, x);
    res->subtract(b);

    return res;
}

DenseMatrix* DenseMatrix::diagonalMatrix(DenseMatrix* source) {
    DenseMatrix* diag = DenseMatrix::zeroMatrix(source->rows, source->cols);

    for (int i = 0; i < diag->rows && i < diag->cols; i++) {
        diag->values[i][i] = source->values[i][i];
    }

    return diag;
}

DenseMatrix* DenseMatrix::lowerTriangularMatrix(DenseMatrix* source)
{
    DenseMatrix* newMatrix = zeroMatrix(source->rows, source->cols);

    for (int i = 0; i < newMatrix->rows; i++) {
        for (int j = 0; j < newMatrix->cols; j++) {
            if (i > j) newMatrix->values[i][j] = source->values[i][j];
        }
    }

    return newMatrix;
}

DenseMatrix* DenseMatrix::upperTriangularMatrix(DenseMatrix* source)
{
    DenseMatrix* newMatrix = zeroMatrix(source->rows, source->cols);

    for (int i = 0; i < newMatrix->rows; i++) {
        for (int j = 0; j < newMatrix->cols; j++) {
            if (j > i) newMatrix->values[i][j] = source->values[i][j];
        }
    }

    return newMatrix;
}

DenseMatrix* DenseMatrix::matrixProduct(DenseMatrix* m1, DenseMatrix* m2) {
    if (m1->cols != m2->rows) throw;

    DenseMatrix* result = zeroMatrix(m1->rows, m2->cols);

    for (int i = 0; i < m1->rows; i++) {
        for (int j = 0; j < m2->cols; j++) {
            for (int k = 0; k < m1->cols; k++) {
                result->values[i][j] += m1->values[i][k] * m2->values[k][j];
            }
        }
    }

    return result;
}

int DenseMatrix::getRows() {
    return rows;
}

int DenseMatrix::getColumns() {
    return cols;
}

bool DenseMatrix::isIndexInRange(int row, int col) {
    if (row < 0 || col < 0) return false;
    if (row >= rows || col >= cols) return false;
    return true;
}

double DenseMatrix::getValue(int row, int col) {
    if (!this->isIndexInRange(row, col)) throw;

    return values[row][col];
}

void DenseMatrix::setValue(int row, int col, double value) {
    if (!this->isIndexInRange(row, col)) throw;

    values[row][col] = value;
}

void DenseMatrix::add(DenseMatrix* other) {
    if (this->rows != other->rows || this->cols != other->cols) throw;

    for (int i = 0; i < this->rows; i++) {
        for (int j = 0; j < this->cols; j++) {
            this->values[i][j] += other->values[i][j];
        }
    }
}

void DenseMatrix::subtract(DenseMatrix* other) {
    if (this->rows != other->rows || this->cols != other->cols) throw;

    for (int i = 0; i < this->rows; i++) {
        for (int j = 0; j < this->cols; j++) {
            this->values[i][j] -= other->values[i][j];
        }
    }
}

void DenseMatrix::fillDiagonal(int diag, double value) {
    if (diag >= this->cols) throw;

    if (diag <= -this->rows) throw;

    int row, col;

    if (diag >= 0) {
        row = 0;
        col = diag;
    }
    else {
        row = -diag;
        col = 0;
    }

    while (row < rows && col < cols) {
        values[row][col] = value;
        row++;
        col++;
    }
}

void DenseMatrix::show() {
    for (int i = 0; i < this->rows; i++) {
        for (int j = 0; j < this->cols; j++) {
            std::cout << values[i][j] << "\t";
        }
        std::cout << std::endl;
    }
}

bool DenseMatrix::isVector() {
    return cols == 1;
}

bool DenseMatrix::isDiagonal() {
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (i == j) continue;

            if (values[i][j] != 0) return false;
        }
    }

    return true;
}

bool DenseMatrix::isDiagonalFull()
{
    for (int i = 0; i < rows && i < cols; i++) {
        if (values[i][i] == 0) return false;
    }

    return true;
}

bool DenseMatrix::isSquare() {
    return rows == cols;
}

double DenseMatrix::euclideanNorm() {
    if (!this->isVector()) throw;

    double sum = 0;

    for (int i = 0; i < rows; i++) {
        sum += pow(this->values[i][0], 2);
    }

    return sqrt(sum);
}

void DenseMatrix::negate()
{
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            values[i][j] = -values[i][j];
        }
    }
}


