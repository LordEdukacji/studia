#include "DenseMatrix.h"
#include <vector>

class IterationResults {
public:
    DenseMatrix* x;

    int iterations;
    std::vector<double> errNorms;

    IterationResults(DenseMatrix* x, int iterations, std::vector<double>& errNorms);
    ~IterationResults();
};

IterationResults* JacobiMethod(DenseMatrix* A, DenseMatrix* b, double errLimit, int maxIterations);

IterationResults* GaussSeidelMethod(DenseMatrix* A, DenseMatrix* b, double errLimit, int maxIterations);

DenseMatrix* LUDecompositionMethod(DenseMatrix* A, DenseMatrix* b);

int sizeTaskA();

DenseMatrix* provideStartingMatrixTaskA(int size);

DenseMatrix* provideStartingMatrixTaskC(int size);

DenseMatrix* provideStartingVector(int size);

void taskAB();

void taskCD();

void taskE();