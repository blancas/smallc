/**
 * Small-C interop test.
 */

// Shift operators.

#define L4 1 << 4;
#define R2 32 >> 2;
#define U4 512 >>> 4;

long MAX = 2L << 5;
long MIN = 64L >> 2;
long UMIN = 128L >>> 3;

int left8() {
  return 1 << 8;
}

int right4() {
  return 1024 >> 4;
}

int uright2() {
  return 1024 >>> 2;
}
