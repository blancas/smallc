/**
 * Small-C interop test.
 */

// Indexing a primitive type (a variant of too many indexes).

int bad_one() {
  int n;
  int m;
  m = n[15][20][12];
  return m;
}
