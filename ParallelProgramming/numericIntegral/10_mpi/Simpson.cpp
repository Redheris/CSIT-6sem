#include <iostream>
#include "mpi.h"

// Число pi
#define PI 3.1415926535897932384626433832795

using namespace std;

// Рассматриваемая функция
double func(double x) {
	return 1.0 / (1.0 + x * x);
}

void integral(const double a, const double b,
	const double h, double* res, int rank, int size)
{
	int i, n;
	n = (int)((b - a) / h); // количество точек сетки интегрирования
	// Цикл вычисления интеграла (Формула Симпсона):
		// Лоакальные переменные для значений сумм внутри формуле
	double resSum1 = 0.0, resSum2 = 0.0;
	double sum1 = 0.0, sum2 = 0.0;
	double height = (b - a) / (2 * n); // Высота трапеции

	for (i = rank; i < n; i += size)
		sum1 += func(a + (2 * i - 1) * height);
	MPI_Reduce(&sum1, &resSum1, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

	for (i = rank; i < n - 1; i += size)
		sum2 += func(a + 2 * i * height);
	MPI_Reduce(&sum2, &resSum2, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

	*res = (height / 3) * (func(a) + func(b) + 4 * resSum1 + 2 * resSum2);
}


double experiment(double* res, int rank, int size)
{
	double stime, ftime; // время начала и конца расчета
	double a = 0.0;		 // левая граница интегрирования
	double b = PI;		 // правая граница интегрирования
	double h = 0.000001;	 // шаг интегрирования
	stime = clock();
	integral(a, b, h, res, rank, size); // вызов функции интегрирования
	ftime = clock();
	return (ftime - stime) / CLOCKS_PER_SEC;
}

int main(int argc, char** argv)
{
	int size, rank;

	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &size);

	int i;			  // переменная цикла
	double time;	  // время проведенного эксперимента
	double res;		  // значение вычисленного интеграла
	double min_time;  // минимальное время работы
	// реализации алгоритма
	double max_time;  // максимальное время работы
	// реализации алгоритма
	double avg_time;  // среднее время работы
	// реализации алгоритма
	int numbExp = 10; // количество запусков программы

	// первый запуск
	min_time = max_time = avg_time = experiment(&res, rank, size);
	// оставшиеся запуски
	for (i = 0; i < numbExp - 1; i++)
	{
		time = experiment(&res, rank, size);
		avg_time += time;
		if (max_time < time) max_time = time;
		if (min_time > time) min_time = time;
	}
	// Вывод результата только на первом потоке
	if (rank == 0) {
		// вывод результатов эксперимента
		cout << "execution time : " << avg_time / numbExp << "; " <<
			min_time << "; " << max_time << endl;
		cout.precision(8);
		cout << "integral value : " << res << endl;
	}

	MPI_Finalize();
	return 0;
}
