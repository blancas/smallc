/**
 * Small-C interop test.
 */

// Insufficient indexes.

int bad_three() {
  int[5][8] n;
  int m;
  m = n[15];
  return m;
}
