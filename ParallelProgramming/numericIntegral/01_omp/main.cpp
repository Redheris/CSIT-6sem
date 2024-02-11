#include <iostream>
#include <omp.h>

using namespace std;

// Рассматриваемая функция
double func(double x) {
	return 1.0 / (1.0 + x * x);
}

void integral(const double a, const double b,
	const double h, double* res)
{
	int i, n;
	n = (int)((b - a) / h); // количество точек сетки интегрирования
	// Цикл вычисления интеграла (Формула Симпсона):
	// Лоакальные переменные для значений сумм внутри формуле
	double sum1 = 0.0, sum2 = 0.0;
	double height = (b - a) / (2 * n);	// Высота трапеции
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
	double stime, ftime;	// время начала и конца расчета
	double a = 0.0;			// левая граница интегрирования
	double b = 1e6;			// правая граница интегрирования
	double h = 0.001;		// шаг интегрирования
	stime = clock();
	integral(a, b, h, res);	// вызов функции интегрирования
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
	int numbExp = 10;	// количество запусков программы
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