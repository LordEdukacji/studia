#include "tasks.h"

#include <cmath>
#include <chrono>
#include <iostream>
#include <fstream>

IterationResults::IterationResults(DenseMatrix* x, int iterations, std::vector<double>& errNorms) : x(x), iterations(iterations), errNorms(errNorms) {}

IterationResults::~IterationResults() {
    delete x;
}

IterationResults* JacobiMethod(DenseMatrix* A, DenseMatrix* b, double errLimit = 10e-9, int maxIterations = 1000) {
    DenseMatrix* D = DenseMatrix::diagonalMatrix(A);
    DenseMatrix* L = DenseMatrix::lowerTriangularMatrix(A);
    DenseMatrix* U = DenseMatrix::upperTriangularMatrix(A);

    DenseMatrix* minusD = DenseMatrix::negatedMatrix(D);
    DenseMatrix* LplusU = DenseMatrix::copyMatrix(L);
    LplusU->add(U);

    DenseMatrix* M = DenseMatrix::diagonalSolve(minusD, LplusU);
    DenseMatrix* bm = DenseMatrix::diagonalSolve(D, b);

    delete minusD;
    delete LplusU;

    int iterations = 0;
    double errNorm = 0;
    std::vector<double> errNorms;

    DenseMatrix* x = DenseMatrix::oneMatrix(A->getColumns(), 1);

    while (true) {
        iterations++;

        x = DenseMatrix::matrixProduct(M, x);

        x->add(bm);

        DenseMatrix* res = DenseMatrix::residualVector(A, x, b);
        errNorm = res->euclideanNorm();
        delete res;

        errNorms.push_back(errNorm);

        if (errNorm < errLimit) break;

        if (iterations >= maxIterations) break;
    }

    delete M;
    delete bm;

    delete D;
    delete L;
    delete U;

    return new IterationResults(x, iterations, errNorms);
}

IterationResults* GaussSeidelMethod(DenseMatrix* A, DenseMatrix* b, double errLimit = 10e-9, int maxIterations = 1000) {
    DenseMatrix* D = DenseMatrix::diagonalMatrix(A);
    DenseMatrix* L = DenseMatrix::lowerTriangularMatrix(A);
    DenseMatrix* U = DenseMatrix::upperTriangularMatrix(A);

    DenseMatrix* DplusL = DenseMatrix::copyMatrix(D);
    DplusL->add(L);

    DenseMatrix* bm = DenseMatrix::forwardSubstitutionSolve(DplusL, b);
    DplusL->negate();
    DenseMatrix* M = DenseMatrix::forwardSubstitutionSolve(DplusL, U);

    delete DplusL;

    int iterations = 0;
    double errNorm = 0;
    std::vector<double> errNorms;

    DenseMatrix* x = DenseMatrix::oneMatrix(A->getColumns(), 1);

    while (true) {
        iterations++;

        DenseMatrix* Mxbm = DenseMatrix::matrixProduct(M, x);
        Mxbm->add(bm);

        delete x;
        x = Mxbm;

        DenseMatrix* res = DenseMatrix::residualVector(A, x, b);
        errNorm = res->euclideanNorm();
        delete res;

        errNorms.push_back(errNorm);

        if (errNorm < errLimit) break;

        if (iterations >= maxIterations) break;
    }

    delete M;
    delete bm;

    delete D;
    delete L;
    delete U;

    return new IterationResults(x, iterations, errNorms);
}

DenseMatrix* LUDecompositionMethod(DenseMatrix* A, DenseMatrix* b) {
    if (!A->isSquare()) throw;

    DenseMatrix* U = DenseMatrix::copyMatrix(A);

    int n = A->getColumns();

    DenseMatrix* L = DenseMatrix::identityMatrix(n);

    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            L->setValue(i, j, U->getValue(i, j) / U->getValue(j, j));

            for (int k = 0; k < n; k++) {
                U->setValue(i, k, U->getValue(i, k) - L->getValue(i, j) * U->getValue(j, k));
            }
        }
    }

    DenseMatrix* y = DenseMatrix::forwardSubstitutionSolve(L, b);

    DenseMatrix* x = DenseMatrix::backwardSubstitutionSolve(U, y);

    delete U;
    delete L;
    delete y;

    return x;
}


int sizeTaskA() {
    int index = 193483;

    int cd = index % 100;

    int N = 900 + cd;

    return N;
}

DenseMatrix* provideStartingMatrixTaskA(int size) {
    int index = 193483;

    int e = (index / 100) % 10;
    int f = (index / 1000) % 10;

    int a1 = 5 + e;
    int a2 = -1, a3 = -1;

    DenseMatrix* matrix = DenseMatrix::zeroMatrix(size, size);

    matrix->fillDiagonal(-2, a3);
    matrix->fillDiagonal(-1, a2);
    matrix->fillDiagonal(0, a1);
    matrix->fillDiagonal(1, a2);
    matrix->fillDiagonal(2, a3);

    return matrix;
}

DenseMatrix* provideStartingMatrixTaskC(int size) {
    int index = 193483;

    int e = (index / 100) % 10;
    int f = (index / 1000) % 10;

    int a1 = 3;
    int a2 = -1, a3 = -1;

    DenseMatrix* matrix = DenseMatrix::zeroMatrix(size, size);

    matrix->fillDiagonal(-2, a3);
    matrix->fillDiagonal(-1, a2);
    matrix->fillDiagonal(0, a1);
    matrix->fillDiagonal(1, a2);
    matrix->fillDiagonal(2, a3);

    return matrix;
}

DenseMatrix* provideStartingVector(int size) {
    int index = 193483;

    int f = (index / 1000) % 10;

    DenseMatrix* vector = DenseMatrix::zeroMatrix(size, 1);

    for (int i = 0; i < size; i++) {
        vector->setValue(i, 0, sin((i + 1) * (f + 1)));
    }

    return vector;
}


void taskAB() {
    DenseMatrix* A = provideStartingMatrixTaskA(sizeTaskA());
    DenseMatrix* b = provideStartingVector(sizeTaskA());

    std::chrono::time_point<std::chrono::high_resolution_clock> begin, end;
    std::chrono::duration<double, std::milli> time;

    begin = std::chrono::high_resolution_clock::now();
    IterationResults* j = JacobiMethod(A, b);
    end = std::chrono::high_resolution_clock::now();

    time = end - begin;
    std::cout << "Jacobi method time [ms]: " << time.count() << std::endl;
    std::cout << "Jacobi method iterations: " << j->iterations << std::endl;

    std::ofstream jacobiFile("JacobiA.txt");
    for (const double err : j->errNorms) {
        jacobiFile << err << std::endl;
    }
    jacobiFile.close();

    delete j;

    begin = std::chrono::high_resolution_clock::now();
    IterationResults* gs = GaussSeidelMethod(A, b);
    end = std::chrono::high_resolution_clock::now();

    time = end - begin;
    std::cout << "Gauss-Seidel method time [ms]: " << time.count() << std::endl;
    std::cout << "Gauss-Seidel method iterations: " << gs->iterations << std::endl;

    std::ofstream gaussSeidelFile("GaussSeidelA.txt");
    for (const double err : gs->errNorms) {
        gaussSeidelFile << err << std::endl;
    }
    gaussSeidelFile.close();

    delete gs;

    delete A;
    delete b;
}

void taskCD() {
    DenseMatrix* A = provideStartingMatrixTaskC(sizeTaskA());
    DenseMatrix* b = provideStartingVector(sizeTaskA());

    std::chrono::time_point<std::chrono::high_resolution_clock> begin, end;
    std::chrono::duration<double, std::milli> time;

    begin = std::chrono::high_resolution_clock::now();
    IterationResults* j = JacobiMethod(A, b);
    end = std::chrono::high_resolution_clock::now();

    time = end - begin;
    std::cout << "Jacobi method time [ms]: " << time.count() << std::endl;
    std::cout << "Jacobi method iterations: " << j->iterations << std::endl;

    std::ofstream jacobiFile("JacobiC.txt");
    for (const double err : j->errNorms) {
        jacobiFile << err << std::endl;
    }
    jacobiFile.close();

    delete j;

    begin = std::chrono::high_resolution_clock::now();
    IterationResults* gs = GaussSeidelMethod(A, b);
    end = std::chrono::high_resolution_clock::now();

    time = end - begin;
    std::cout << "Gauss-Seidel method time [ms]: " << time.count() << std::endl;
    std::cout << "Gauss-Seidel method iterations: " << gs->iterations << std::endl;

    std::ofstream gaussSeidelFile("GaussSeidelC.txt");
    for (const double err : gs->errNorms) {
        gaussSeidelFile << err << std::endl;
    }
    gaussSeidelFile.close();

    delete gs;

    begin = std::chrono::high_resolution_clock::now();
    DenseMatrix* lu = LUDecompositionMethod(A, b);
    end = std::chrono::high_resolution_clock::now();

    time = end - begin;
    std::cout << "LU decomposition method time [ms]: " << time.count() << std::endl;

    DenseMatrix* res = DenseMatrix::residualVector(A, lu, b);
    std::cout << "LU decomposition method residual error norm: " << res->euclideanNorm() << std::endl;
    delete res;
    delete lu;

    delete A;
    delete b;
}

void taskE() {
    std::vector<int> sizes = { 100, 200, 300, 400, 500, 1000, 2000, 3000, 4000, 5000 };

    std::vector<double> jacobiTimes;
    std::vector<double> gaussSeidelTimes;
    std::vector<double> LUTimes;

    for (int size : sizes) {
        std::cout << "Size " << size << ":" << std::endl;

        DenseMatrix* A = provideStartingMatrixTaskA(size);
        DenseMatrix* b = provideStartingVector(size);

        std::chrono::time_point<std::chrono::high_resolution_clock> begin, end;
        std::chrono::duration<double, std::milli> time;

        begin = std::chrono::high_resolution_clock::now();
        IterationResults* j = JacobiMethod(A, b);
        end = std::chrono::high_resolution_clock::now();

        time = end - begin;
        std::cout << "Jacobi method: " << time.count() << std::endl;
        jacobiTimes.push_back(time.count());

        delete j;

        begin = std::chrono::high_resolution_clock::now();
        IterationResults* gs = GaussSeidelMethod(A, b);
        end = std::chrono::high_resolution_clock::now();

        time = end - begin;
        std::cout << "Gauss-Seidel method: " << time.count() << std::endl;
        gaussSeidelTimes.push_back(time.count());

        delete gs;

        begin = std::chrono::high_resolution_clock::now();
        DenseMatrix* lu = LUDecompositionMethod(A, b);
        end = std::chrono::high_resolution_clock::now();

        time = end - begin;
        std::cout << "LU decomposition method: " << time.count() << std::endl;
        LUTimes.push_back(time.count());

        delete lu;

        delete A;
        delete b;

        std::cout << std::endl;
    }

    std::ofstream jacobiFile("JacobiE.txt");
    for (const double t : jacobiTimes) {
        jacobiFile << t << std::endl;
    }
    jacobiFile.close();

    std::ofstream gaussSeidelFile("GaussSeidelE.txt");
    for (const double t : gaussSeidelTimes) {
        gaussSeidelFile << t << std::endl;
    }
    gaussSeidelFile.close();

    std::ofstream LUFile("LUE.txt");
    for (const double t : LUTimes) {
        LUFile << t << std::endl;
    }
    LUFile.close();

    std::ofstream sizeFile("sizes.txt");
    for (const double t : sizes) {
        sizeFile << t << std::endl;
    }
    sizeFile.close();
}