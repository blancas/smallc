/**
 * Small-C interop test.
 */

int[10] INTS;
long[10][15][20] LONGS;

int single_dim() {
  int[10] n;
  int m;
  n[5] = 1;
  m = n[5] + 2;
  return m;
}


int double_dim() {
  int[10][20] n;
  int m;
  n[5][19] = 3;
  m = n[5][19] + 4;
  return m;
}

int triple_dim() {
  int[10][20][30] n;
  int m;
  n[5][19][12] = -1;
  m = n[5][19][12] - 2;
  return m;
}

int global_INTS() {
  INTS[4] = 3;
  int m = INTS[4] + 4;
  return m;
}

long global_LONGS() {
  LONGS[0][1][2] = 2L;
  long m = 1L + LONGS[0][1][2];
  return m;
}

int assign() {
  int[10][5] n;
  int[][] m;
  n[3][3] = 2112;
  m = n;
  return m[3][3];
}

int massign() {
  int[10] n;
  n[0] = n[1] = n[2] = 2112;
  return n[0];
}

int massignm() {
  int[10][12][14] n;
  n[0][2][4] = n[1][2][3] = n[2][1][0] = 2112;
  return n[0][2][4];
}

