#include <iostream>
#include <omp.h>

using namespace std;

// ��������������� �������
double func(double x) {
	return 1.0 / (1.0 + x * x);
}

void integral(const double a, const double b,
	const double h, double* res)
{
	int i, n;
	n = (int)((b - a) / h); // ���������� ����� ����� ��������������
	// ���� ���������� ��������� (������� ��������):
	// ���������� ���������� ��� �������� ���� ������ �������
	double sum1 = 0.0, sum2 = 0.0;
	double height = (b - a) / (2 * n);	// ������ ��������
#pragma omp parallel for reduction(+: sum1)
	for (i = 1; i < n; i++)
		sum1 += func(a + (2 * i - 1) * height);
#pragma omp parallel for reduction(+: sum2)
	for (i = 1; i < n - 1; i++)
		sum2 += func(a + 2 * i * height);
	*res = (height / 3) * (func(a) + func(b) + 4 * sum1 + 2 * sum2);
}

double experiment(double* res)
{
	double stime, ftime;	// ����� ������ � ����� �������
	double a = 0.0;			// ����� ������� ��������������
	double b = 1e6;			// ������ ������� ��������������
	double h = 0.001;		// ��� ��������������
	stime = clock();
	integral(a, b, h, res);	// ����� ������� ��������������
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
	int numbExp = 10;	// ���������� �������� ���������
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