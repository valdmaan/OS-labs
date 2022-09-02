#include <vector>
#include <thread>
#include <cassert>

using Matrix = std::vector<std::vector<int>>;

int _MultiplyRowCol(Matrix& A, Matrix& B, Matrix& C, int row, int col) {
	int res = 0;

	for (int k = 0; k < A[row].size(); ++k)
		res += A[row][k] * B[k][col];

	printf("COMPUTED: C[%2d][%2d] = %d\n", row, col, res);
	C[row][col] = res;

	return res;
}

Matrix Multiply(Matrix& A, Matrix& B) {

	assert(A[0].size() == B.size());

	size_t n = A.size(), m = B.size(), k = B[0].size();

	Matrix C(n, std::vector<int>(k));

	std::vector<std::thread> threads;

	for (int i = 0; i < n; ++i)
		for (int j = 0; j < k; ++j)
			threads.push_back(
				std::thread(
					_MultiplyRowCol,
					std::ref(A),
					std::ref(B),
					std::ref(C),
					i,
					j
				)
			);

	for (std::thread& t : threads)
		t.join();

	return C;

}

Matrix RandomMatrix(unsigned int n, unsigned int m) {
	Matrix M(n, std::vector<int>(m));

	for (int i = 0; i < n; ++i)
		for (int j = 0; j < m; ++j)
			M[i][j] = 1 + rand() % 100;

	return M;
}

void PrintMatrix(Matrix& M) {
	for (int i = 0; i < M.size(); ++i) {
		printf("[ ");
		for (int j = 0; j < M[i].size(); ++j)
			printf("%9d ", M[i][j]);

		printf("]\n");
	}
}

int main() {
	clock_t now;

	now = clock();
	Matrix A = RandomMatrix(40, 20);
	Matrix B = RandomMatrix(20, 30);
	Matrix C = Multiply(A, B);

	printf("%.4fs\n", double(clock() - now) / CLOCKS_PER_SEC);

	PrintMatrix(C);

	return 0;
}
