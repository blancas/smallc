/**
 * Small-C interop test.
 */

// Too many indexes.

int bad_two() {
  int[5][8] n;
  int m;
  m = n[15][20][12];
  return m;
}
