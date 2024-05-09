#include <iostream>
#include "mpi.h"

// ����� pi
#define PI 3.1415926535897932384626433832795

using namespace std;

int nProc, procId;

// ��������������� �������
double func(double a1, double b1, double a2, double b2, double x, double y) {
	double e = exp(sin(PI * x) * cos(PI * y));
	return (e + 1) / ((b1 - a1) * (b2 - a2));
}

void integral(
	const double a1, const double b1,
	const double a2, const double b2,
	const double h, double* res)
{
	int n, m;
	n = (int)((b1 - a1) / h); // ���������� ����� ����� �������������� �� x
	m = (int)((b2 - a2) / h); // ���������� ����� ����� �������������� �� y
	// ���������� ���������� ��� �������� �����
	double resSum = 0.0;
	double sum = 0.0;
	double height1 = (b1 - a1) / n;
	double height2 = (b2 - a2) / m;
	// ���� ��������� ���������
	double x, y;
	for (int i = procId; i < n - 1; i += nProc) {
		x = a1 + i * height1 + height1 / 2.0;
		for (int j = 0; j < m - 1; j++) {
			y = a2 + j * height2 + height2 / 2.0;
			sum += height1 * height2 * func(a1, b1, a2, b2, x, y);
		}
	}
	MPI_Reduce(&sum, &resSum, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
	
	*res = resSum;
}

double experiment(double* res)
{
	double stime, ftime;	// ����� ������ � ����� �������
	// ������� �������������� 
	double a1 = 0, b1 = 16;
	double a2 = 0, b2 = 16;
	double h = 0.001;		// ��� ��������������
	stime = clock();
	integral(a1, b1, a2, b2, h, res);	// ����� ������� ��������������
	ftime = clock();
	return (ftime - stime) / CLOCKS_PER_SEC;
}

int main()
{
	MPI_Init(NULL, NULL);
	MPI_Comm_rank(MPI_COMM_WORLD, &procId);
	MPI_Comm_size(MPI_COMM_WORLD, &nProc);

	int i;				// ���������� �����
	double time;		// ����� ������������ ������������
	double res;			// �������� ������������ ���������
	double min_time;	// ����������� ����� ������ ���������� ���������
	double max_time;	// ������������ ����� ������ ���������� ���������
	double avg_time;	// ������� ����� ������ ���������� ���������
	int numbExp = 1;	// ���������� �������� ���������
	// ������ ������
	min_time = max_time = avg_time = experiment(&res);
	// ���������� �������
	for (i = 0; i < numbExp - 1; i++)
	{
		time = experiment(&res);
		avg_time += time;
		if (max_time < time) max_time = time;
		if (min_time > time) min_time = time;
	}
	// ����� ����������� ������������ �� ������ ������
	if (procId == 0) {
		cout << "execution time : " << avg_time / numbExp << "; " <<
			min_time << "; " << max_time << endl;
		cout.precision(8);
		cout << "integral value : " << res << endl;
	}
	MPI_Finalize();
	return 0;
}