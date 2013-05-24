/**
 * Small-C interop test.
 */

#define SIZE 100;

int limit;

// Assignment

int sum() {
  int sum;
  sum = 3 + 4;
  return sum;
}

int sum_args(int a, int b) {
  int sum;
  sum = a + b;
  return sum;
}

long multiple() {
  long base = 2112000000L;
  long m, n;
  m = n = ++base;
  return m;
}

int global() {
  int n;
  limit = SIZE;
  return limit;
}
