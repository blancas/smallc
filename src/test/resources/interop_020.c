/**
 * Small-C interop test.
 */

// Function reference must be function call.

int foo() {
  return 1;
}

void bar() {
  int sum = 1 + foo; // Should be 1 + foo();
}
