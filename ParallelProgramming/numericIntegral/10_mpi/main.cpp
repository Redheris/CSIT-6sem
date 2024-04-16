//#include <iostream>
//#include "mpi.h"
//
//// Число pi
//#define PI 3.1415926535897932384626433832795
//
//using namespace std;
//
//// Рассматриваемая функция
//double func(double x) {
//	return 1.0 / (1.0 + x * x);
//}
//
//void integral(const double a, const double b,
//	const double h, double* res, int rank, int size)
//{
//	int i, n;
//	//double resSum;
//	double sum;
//	double x;
//	n = (int)((b - a) / h);
//	sum = 0.0;
//
//	for (i = 0; i < n; i++)
//	{
//		x = a + i * h + h / 2.0;
//		sum += func(x) * h;
//	}
//	MPI_Reduce(&sum, res, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
//
//	//*res = resSum;
//}
//
//double experiment(double* res, int rank, int size)
//{
//	double stime, ftime;
//	double a = 0.0;
//	double b = PI;
//	double h = 0.0000001;
//	stime = clock();
//	integral(a, b, h, res, rank, size);
//	ftime = clock();
//	return (ftime - stime) / CLOCKS_PER_SEC;
//}
//
//int main(int argc, char** argv)
//{
//	int size, rank;
//
//	MPI_Init(&argc, &argv);
//	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
//	MPI_Comm_size(MPI_COMM_WORLD, &size);
//
//	int i;
//	double time;
//	double res;
//	double min_time;
//	double max_time;
//	double avg_time;
//	int numbExp = 10;
//
//	min_time = max_time = avg_time = experiment(&res, rank, size);
//	
//	for (i = 0; i < numbExp - 1; i++)
//	{
//		time = experiment(&res, rank, size);
//		avg_time += time;
//		if (max_time < time) max_time = time;
//		if (min_time > time) min_time = time;
//	}
//	
//	// Вывод результата только первым потоком
//	if (rank == 0) {
//		cout << "execution time : " << avg_time / numbExp << "; " <<
//			min_time << "; " << max_time << endl;
//		cout.precision(8);
//		cout << "integral value : " << res << endl;
//	}
//
//	MPI_Finalize();
//
//	return 0;
//}