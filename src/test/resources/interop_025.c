/**
 * Small-C interop test.
 */

// Increment and decrement.

byte index1 = 1;
char index2 = 2;
short index3 = 3;
int index4 = 4;
long index5 = 5L;
float x = 3.1415927f;
double y = 0.000033339999;

int inc_globals() {
  ++index1;
  index1++;
  ++index2;
  index2++;
  ++index3;
  index3++;
  ++index4;
  index4++;
  ++index5;
  index5++;
  ++x;
  x++;
  ++y;
  y++;
  return ++index4;
}

int dec_globals() {
  --index1;
  index1--;
  --index2;
  index2--;
  --index3;
  index3--;
  --index4;
  index4--;
  --index5;
  index5--;
  --x;
  x--;
  --y;
  y--;
  return --index4;
}

double inc_locals() {
  byte index1 = 1;
  char index2 = 2;
  short index3 = 3;
  int index4 = 4;
  long index5 = 5L;
  float x = 3.1415927f;
  double y = 0.000033339999;

  ++index1;
  index1++;
  ++index2;
  index2++;
  ++index3;
  index3++;
  ++index4;
  index4++;
  ++index5;
  index5++;
  ++x;
  x++;
  ++y;
  y++;
  return y++;
}

short dec_locals() {
  byte index1 = 1;
  char index2 = 2;
  short index3 = 3;
  int index4 = 4;
  long index5 = 5L;
  float x = 3.1415927f;
  double y = -0.000033339999;

  --index1;
  index1--;
  --index2;
  index2--;
  --index3;
  index3--;
  --index4;
  index4--;
  --index5;
  index5--;
  --x;
  x--;
  --y;
  y--;
  return --index3;
}

int inc() {
  int var = 9999;
  return ++var;
}

int dec() {
  int var = 10000;
  return --var;
}

long inc_arg(long arg) {
  return ++arg;
}

long dec_arg(long arg) {
  return --arg;
}

int incpost() {
  int var = 9999;
  return var--;
}

int decpost() {
  int var = 10000;
  return var--;
}

long incpost_arg(long arg) {
  return arg++;
}

long decpost_arg(long arg) {
  return arg--;
}
