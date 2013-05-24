/**
 * Small-C interop test.
 */

// Unary plus and minus.

#define MFIVE -5L;
#define PFIVE +5L;

double mroot = -0.00188;
double proot = +0.00188;

int pluslit() {
  return +1;
}

int neglit() {
  return -1;
}

int plusvar() {
  int m = 500;
  return +m;
}

int negvar() {
  int m = 500;
  return -m;
}

long multlit() {
  return PFIVE * (-10L);
}

long multvar() {
  return MFIVE * (-PFIVE);
}

long negexpr() {
  return -((PFIVE * 10L + MFIVE) / 10L);
}


