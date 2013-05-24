/*
 * Symbolic constants.
 */
#define PI 3.1415927;
#define N 512;
#define MAGIC 0xCAFEBABEL;

// Compound define:
#define {
    PRIVATE   0;
    PUBLIC    1;
    PROTECTED 2;
    PACKAGE   3;
    STATIC    4;
    FINAL     5;
    ABSTRACT  6;

    SEPARATOR '/';
    MESSAGE   "SUCCESSFUL RUN";
}

// Private symbols start with an underscore.
#define _LOCAL 1;
#define _DEBUG false;
#define _MASK 0x7F00;

// Defines for different literal types:
#define $CHAR   '$';
#define $DEC    5005;
#define $DECL   1024L;
#define $OCT    034;
#define $OCTL   077L;
#define $HEX    0xFAFA;
#define $HEXL   0xFAFAL;
#define $FLOAT  0.0099f;
#define $DOUBLE 0.333333333;
#define $STRING "Now is the time";
#define $FALSE  false;
#define $TRUE   true;

// This should fail; don't know the type of null;
//#define NULL null;

/*
 * Global variables.
 */
int status;
long beginning, ending;
float alfa = 0.9999f, beta = 1.0001f, delta = 0.00002f;
boolean empty = true;
String s;

// Private global variable.
char _delimiter = '$';

// Global variables with different types:
boolean $$FALSE = false;
boolean $$TRUE  = true;
char $$CHAR     = '$';
byte $$BYTE     = 127;
short $$SHORT   = 12500;
int $$DEC       = 5005;
long $$DECL     = 1024L;
int $$OCT       = 034;
long $$OCTL     = 077L;
int $$HEX       = 0xFAFA;
long $$HEXL     = 0xFAFAL;
float $$FLOAT   = 0.0099f;
double $$DOUBLE = 0.333333333;
String $$STRING = "Now is the time";

// Arrays
int[10] nums;
int[10][20] matrix;
double[30][50][40] space_3D;
double[][][] space_alias;

// This is weird but valid: the initialization has precedence.
int[100] is_null = null;

// Arrays with the native types and different sizes.
boolean[10] $boolean_arr;
byte[20] $byte_arr;
char[100] $char_arr;
short[500] $short_arr;
int[1500] $int_arr;
long[2000] $long_arr;
float[5000] $float_arr;
double[1000] $double_arr;

// Multi-dimensional arrays with the native types and different sizes.
boolean[10][12] _boolean_arr;
byte[20][40] _byte_arr;
char[100][150] _char_arr;
short[500][600] _short_arr;
int[1500][1200] _int_arr;
long[200][8000] _long_arr;
float[5000][4000] _float_arr;
double[1000][2000] _double_arr;

int[50][40][30][20] raw_data;

// Reference types from java.lang
java.lang.String jls;
String[10] arr;
String[500][100] arr_2d;
Thread t;
Trhead[] pool;
java.util.concurrent.ConcurrentHashMap map;

// Function definitions
void _private_function() {
}

int firstExpr() {
  return 3 + 4;
}

int add(int a, int b) {
  return a + b;
}

int sum(int a, int b) {
  int sum = a + b;
  return sum;
}
