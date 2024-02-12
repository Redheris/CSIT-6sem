#include <iostream>
#include <cmath>

// Число pi
#define PI 3.1415926535897932384626433832795

using namespace std;

// Рассматриваемая функция
double func(double a1, double b1, double a2, double b2, double x, double y) {
	double e = exp(sin(PI * x) * cos(PI * y));
	return (e + 1) / ((b1 - a1) * (b2 - a2));
}

void integral(
	const double a1, const double b1,
	const double a2, const double b2,
	const double h, double* res)
{
	int i, j, n, m;
	double x, y;
	n = (int)((b1 - a1) / h); // количество точек сетки интегрирования по x
	m = (int)((b2 - a2) / h); // количество точек сетки интегрирования по y
	// Лоакальная переменная для значения суммы
	double sum = 0.0;
	double height1 = (b1 - a1) / n;
	double height2 = (b2 - a2) / m;
	// Цикл вычсления интеграла
	for (i = 0; i < n - 1; i++) {
		x = a1 + i * height1 + height1 / 2.0;
		for (j = 0; j < m - 1; j++) {
			y = a2 + j * height2 + height2 / 2.0;
			sum += height1 * height2 * func(a1, b1, a2, b2, x, y);
		}
	}
	*res = sum;
}

double experiment(double* res)
{
	double stime, ftime;	// время начала и конца расчета
	// Границы интегрирования 
	double a1 = 0, b1 = 16;
	double a2 = 0, b2 = 16;
	double h = 0.001;		// шаг интегрирования
	stime = clock();
	integral(a1, b1, a2, b2, h, res);	// вызов функции интегрирования
	ftime = clock();
	return (ftime - stime) / CLOCKS_PER_SEC;
}

int main()
{
	int i;				// переменная цикла
	double time;		// время проведенного эксперимента
	double res;			// значение вычисленного интеграла
	double min_time;	// минимальное время работы реализации алгоритма
	double max_time;	// максимальное время работы реализации алгоритма
	double avg_time;	// среднее время работы реализации алгоритма
	int numbExp = 3;	// количество запусков программы
	// первый запуск
	min_time = max_time = avg_time = experiment(&res);
	// оставшиеся запуски
	for (i = 0; i < numbExp - 1; i++)
	{
		time = experiment(&res);
		avg_time += time;
		if (max_time < time) max_time = time;
		if (min_time > time) min_time = time;
	}
	// вывод результатов эксперимента
	cout << "execution time : " << avg_time / numbExp << "; " <<
		min_time << "; " << max_time << endl;
	cout.precision(8);
	cout << "integral value : " << res << endl;
	return 0;
}