#include <iomanip>
#include <iostream>
#include <cmath>
#include <complex>
#include <time.h>
using namespace std;
#define PI (3.14159265358979323846)
//Function for simple initialization of input signal elements
void DummyDataInitialization(complex<double>* mas, int size) {
	for (int i = 0; i < size; i++)
		mas[i] = 0;
	mas[size - size / 4] = 1;
}
//Function for memory allocation and data initialization
void ProcessInitialization(complex<double>*& inputSignal,
	complex<double>*& outputSignal, int& size) {
	// Setting the size of signals
	do
	{
		cout << "Enter the input signal length: ";
		cin >> size;
		if (size < 4)
			cout << "Input signal length should be >= 4" << endl;
		else
		{
			int tmpSize = size;
			while (tmpSize != 1)
			{
				if (tmpSize % 2 != 0)
				{
					cout << "Input signal length should be powers of two" << endl;
					size = -1;
					break;
				}
				tmpSize /= 2;
			}
		}
	} while (size < 4);
	cout << "Input signal length = " << size << endl;
	inputSignal = new complex<double>[size];
	outputSignal = new complex<double>[size];
	//Initialization of input signal elements - tests
	DummyDataInitialization(inputSignal, size);
	//Computational experiments
	//RandomDataInitialization(inputSignal, size);
}
//Function for computational process temination
void ProcessTermination(complex<double>*& inputSignal,
	complex<double>*& outputSignal) {
	delete[] inputSignal;
	inputSignal = NULL;
	delete[] outputSignal;
	outputSignal = NULL;
}
void BitReversing(complex<double>* inputSignal,
	complex<double>* outputSignal, int size) {
	int j = 0, i = 0;
	while (i < size)
	{
		if (j > i)
		{
			outputSignal[i] = inputSignal[j];
			outputSignal[j] = inputSignal[i];
		}
		else
			if (j == i)
				outputSignal[i] = inputSignal[i];
		int m = size >> 1;
		while ((m >= 1) && (j >= m))
		{
			j -= m;
			m = m >> 1;
		}
		j += m;
		i++;
	}
}
__inline void Butterfly(complex<double>* signal,
	complex<double> u, int offset, int butterflySize) {
	complex<double> tem = signal[offset + butterflySize] * u;
	signal[offset + butterflySize] = signal[offset] - tem;
	signal[offset] += tem;
}
void SerialFFTCalculation(complex<double>* signal, int size) {
	int m = 0;
	for (int tmp_size = size; tmp_size > 1; tmp_size /= 2, m++);
	for (int p = 0; p < m; p++)
	{
		int butterflyOffset = 1 << (p + 1);
		int butterflySize = butterflyOffset >> 1;
		double coeff = PI / butterflySize;
		for (int i = 0; i < size / butterflyOffset; i++)
			for (int j = 0; j < butterflySize; j++)
				Butterfly(signal, complex<double>(cos(-j * coeff),
					sin(-j * coeff)), j + i * butterflyOffset, butterflySize);
	}
}
// FFT computation
void SerialFFT(complex<double>* inputSignal,
	complex<double>* outputSignal, int size) {
	BitReversing(inputSignal, outputSignal, size);
	SerialFFTCalculation(outputSignal, size);
}
void PrintSignal(complex<double>* signal, int size) {
	cout << "Result signal" << endl;
	for (int i = 0; i < size; i++)
		cout << signal[i] << endl;
}
int main() {
	complex<double>* inputSignal = NULL;
	complex<double>* outputSignal = NULL;
	int size = 0;

	const int repeatCount = 16;
	double startTime;
	double duration;
	double minDuration = DBL_MAX;


	cout << "Fast Fourier Transform" << endl;
	//Memory allocation and data initialization
	ProcessInitialization(inputSignal, outputSignal, size);

	for (int i = 0; i < repeatCount; i++)
	{
		startTime = clock();
		// FFT computation
		SerialFFT(inputSignal, outputSignal, size);
		duration = (clock() - startTime) / CLOCKS_PER_SEC;
		if (duration < minDuration)
			minDuration = duration;
	}
	cout << setprecision(6);
	cout << "Execution time is " << minDuration << " s. " << endl;

	//Result signal output
	PrintSignal(outputSignal, size);
	//Computational process termination
	ProcessTermination(inputSignal, outputSignal);
	return 0;
}
