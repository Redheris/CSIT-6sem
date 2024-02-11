#include <iostream>

// ����� pi
#define PI 3.1415926535897932384626433832795

using namespace std;

void integral(const double a, const double b,
	const double h, double* res)
{
	int i, n;
	double sum;				// ��������� ���������� ��� �������� ���������
	double x;				// ���������� ����� �����
	n = (int)((b - a) / h); // ���������� ����� ����� ��������������
	// ���� ���������� ���������:
	sum = 0.0;
	for (i = 0; i < n; i++)
	{
		x = a + i * h + h / 2.0;
		sum += sin(x) * h;
	}
	*res = sum;
}

double experiment(double* res)
{
	double stime, ftime;	// ����� ������ � ����� �������
	double a = 0.0;			// ����� ������� ��������������
	double b = PI;			// ������ ������� ��������������
	double h = 0.0000001;	// ��� ��������������
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
	int numbExp = 10; // ���������� �������� ���������
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