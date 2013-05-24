/**
 * Small-C interop test.
 */

// Function call statement.

void foo() {	
}
	
int bar() {
  return 2112;
}
	
void empty(int a, int b) {
}
	
int sum(int a, int b) {
  return a + b;
}
	
void make_calls() {
  foo();
  bar();
  empty(1, 2);
  sum(3, 4);
}

int call_sum() {
  int n;
  n = sum(3, 4);
  return n;
}

int call_nested() {
  int n;
  n = sum(bar(), bar());
  return n;
}

int call_sums() {
  int[4] n;
  n[0] = 3;
  n[1] = 4;
  n[2] = sum(n[0], n[1]);
  n[3] = sum(n[2]*2, n[1]*4);
  return n[3];
}
