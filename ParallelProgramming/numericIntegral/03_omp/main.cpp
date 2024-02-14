#include <iostream>
#include <omp.h>

// ����� pi
#define PI 3.1415926535897932384626433832795

using namespace std;

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
	double sum = 0.0;
	double height1 = (b1 - a1) / n;
	double height2 = (b2 - a2) / m;
	// ���� ���������� ���������
	double x, y, xd = 0, yd = 0;
#pragma omp parallel for private(x, xd)
	for (int i = 0; i < n - 1; i++) {
		x = a1 + xd + height1 / 2.0;
		xd += height1;
#pragma omp parallel for private(y, yd) reduction(+: sum)
		for (int j = 0; j < m - 1; j++) {
			y = a2 + yd + height2 / 2.0;
			yd += height2;
			sum += height1 * height2 * func(a1, b1, a2, b2, x, y);
		}
	}
	*res = sum;
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
	int i;				// ���������� �����
	double time;		// ����� ������������ ������������
	double res;			// �������� ������������ ���������
	double min_time;	// ����������� ����� ������ ���������� ���������
	double max_time;	// ������������ ����� ������ ���������� ���������
	double avg_time;	// ������� ����� ������ ���������� ���������
	int numbExp = 3;	// ���������� �������� ���������
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
	// ����� ����������� ������������
	cout << "execution time : " << avg_time / numbExp << "; " <<
		min_time << "; " << max_time << endl;
	cout.precision(8);
	cout << "integral value : " << res << endl;
	return 0;
}