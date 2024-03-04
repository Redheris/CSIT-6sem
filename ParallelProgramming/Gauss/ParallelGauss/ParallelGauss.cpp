#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <time.h>
#include <math.h>
#include <omp.h>

typedef struct {
	int PivotRow;
	double MaxValue;
} TThreadPivotRow;

int* pPivotPos; // The number of pivot rows selected at the iterations
int* pPivotIter; // The iterations, at which the rows were pivots// Finding the pivot row
int ParallelFindPivotRow(double* pMatrix, int Size, int Iter) {
	int PivotRow = -1; // The index of the pivot row
	double MaxValue = 0; // The value of the pivot element
	int i; // Loop variable
	// Choose the row, that stores the maximum element
#pragma omp parallel
	{
		TThreadPivotRow ThreadPivotRow;
		ThreadPivotRow.MaxValue = 0;
		ThreadPivotRow.PivotRow = -1;
#pragma omp for
		for (i = 0; i < Size; i++) {
			if ((pPivotIter[i] == -1) &&
				(fabs(pMatrix[i * Size + Iter]) > ThreadPivotRow.MaxValue)) {
				ThreadPivotRow.PivotRow = i;
				ThreadPivotRow.MaxValue = fabs(pMatrix[i * Size + Iter]);
			}
		}
		printf("\n Local thread (id = %i) pivot row : %i ",
			omp_get_thread_num(), ThreadPivotRow.PivotRow);
#pragma omp critical
		{
			if (ThreadPivotRow.MaxValue > MaxValue) {
				MaxValue = ThreadPivotRow.MaxValue;
				PivotRow = ThreadPivotRow.PivotRow;
			}
		} // pragma omp critical
	}// pragma omp parallel
	return PivotRow;
}// Function for the execution of Gauss algorithm
void ParallelResultCalculation(double* pMatrix, double* pVector,
	double* pResult, int Size) {
	// Memory allocation
	pPivotPos = new int[Size];
	pPivotIter = new int[Size];
	for (int i = 0; i < Size; i++) {
		pPivotIter[i] = -1;
	}
	int GlobalPivotPos;
	GlobalPivotPos = ParallelFindPivotRow(pMatrix, Size, 0);
	printf("\n Global pivot row : %i ", GlobalPivotPos);
	// Memory deallocation
	delete[] pPivotPos;
	delete[] pPivotIter;
}
// Function for simple initialization of the matrix
// and the vector elements
void DummyDataInitialization(double* pMatrix, double* pVector, int
	Size) {
	int i, j; // Loop variables
	for (i = 0; i < Size; i++) {
		pVector[i] = i + 1;
		for (j = 0; j < Size; j++) {
			if (j <= i)
				pMatrix[i * Size + j] = 1;
			else
				pMatrix[i * Size + j] = 0;
		}
	}
}
// Function for random initialization of the matrix
// and the vector elements
void RandomDataInitialization(double* pMatrix, double* pVector,
	int Size) {
	int i, j; // Loop variables
	srand(unsigned(clock()));
	for (i = 0; i < Size; i++) {
		pVector[i] = rand() / double(1000);
		for (j = 0; j < Size; j++) {
			if (j <= i)
				pMatrix[i * Size + j] = rand() / double(1000);
			else
				pMatrix[i * Size + j] = 0;
		}
	}
}

// Column elimination
void ParallelColumnElimination(double* pMatrix, double* pVector,
	int Pivot, int Iter, int Size) {
	double PivotValue, PivotFactor;
	PivotValue = pMatrix[Pivot * Size + Iter];
#pragma omp parallel for private(PivotFactor) schedule(dynamic,1)
	for (int i = 0; i < Size; i++) {
		if (pPivotIter[i] == -1) {
			PivotFactor = pMatrix[i * Size + Iter] / PivotValue;
			for (int j = Iter; j < Size; j++) {
				pMatrix[i * Size + j] -= PivotFactor * pMatrix[Pivot * Size + j];
			}
			pVector[i] -= PivotFactor * pVector[Pivot];
		}
	}
}

// Function for formatted matrix output
void PrintMatrix(double* pMatrix, int RowCount, int ColCount) {
	int i, j; // Loop variables
	for (i = 0; i < RowCount; i++) {
		for (j = 0; j < ColCount; j++)
			printf("%7.4f ", pMatrix[i * RowCount + j]);
		printf("\n");
	}
}
// Function for formatted vector output
void PrintVector(double* pVector, int Size) {
	int i;
	for (i = 0; i < Size; i++)
		printf("%7.4f ", pVector[i]);
}

// Function for memory allocation and definition of the objects elements
void ProcessInitialization(double*& pMatrix, double*& pVector,
	double*& pResult, int& Size) {
	// Setting the size of the matrix and the vector
	do {
		printf("\nEnter size of the matrix and the vector: ");
		scanf_s("%d", &Size);
		printf("\nChosen size = %d \n", Size);
		if (Size <= 0)
			printf("\nSize of objects must be greater than 0!\n");
	} while (Size <= 0);
	// Memory allocation
	pMatrix = new double[Size * Size];
	pVector = new double[Size];
	pResult = new double[Size];
	// Initialization of the matrix and the vector elements
	DummyDataInitialization(pMatrix, pVector, Size);
	//RandomDataInitialization(pMatrix, pVector, Size);
}

void ProcessTermination(double* pMatrix, double* pVector, double*
	pResult) {
	delete[] pMatrix;
	delete[] pVector;
	delete[] pResult;
}

int main() {
	double* pMatrix; // The matrix of the linear system
	double* pVector; // The right parts of the linear system
	double* pResult; // The result vector
	int Size; // The size of the matrix and the vectors
	double start, finish, duration;
	// Data initialization
	ProcessInitialization(pMatrix, pVector, pResult, Size);

	// The matrix and the vector output
	printf("Initial Matrix \n");
	PrintMatrix(pMatrix, Size, Size);
	printf("Initial Vector \n");
	PrintVector(pVector, Size);

	ParallelResultCalculation(pMatrix, pVector, pResult, Size);
	// Program termination
	ProcessTermination(pMatrix, pVector, pResult);
	return 0;
}