/**
 * Small-C interop test.
 */

// Bitwise logical operators.

#define MASK1 0xF0 & 0x0F;
#define MASK2 0xF0 | 0x0F;
#define MASK3 ~MASK2;
#define MASK4 0xF0 ^ 0x0F;

long mask1 = 0xF0L & 0x0FL;
long mask2 = 0xF0L | 0x0FL;
long mask3 = ~mask2;
long mask4 = 0xF0L ^ 0x0FL;

// Work on literals.

int iand() {
  return 0xF0 & 0x31;
}

int ior() {
  return 0x0F | 0xF0;
}

int inot() {
  return ~0x0F0F;
}

int ixor() {
  return 0x05 ^ 0x10;
}

long land() {
  return 0xF0L & 0x31L;
}

long lor() {
  return 0x0FL | 0xF0L;
}

long lnot() {
  return ~0x0F0FL;
}

long lxor() {
  return 0x05L ^ 0x10L;
}

// Work on variables.

int iand_var(int a, int b) {
  return b & a;
}

int ior_var(int a, int b) {
  return a | b;
}

int inot_var(int a) {
  return ~a;
}

int ixor_var(int a, int b) {
  return a ^ b;
}

long land_var(long a, long b) {
  return a & b ;
}

long lor_var(long a, long b) {
  return a | b;
}

long lnot_var(long a) {
  return ~a;
}

long lxor_var(long a, long b) {
  return a ^ b;
}
