/**
 * Small-C interop test.
 */

// Test for correct signatures.

void ret_void() {}

boolean ret_boolean() {}

byte ret_byte() {}

char ret_char() {}

short ret_short() {}

int ret_int() {}

long ret_long() {}

float ret_float() {}

double ret_double() {}

String ret_string() {}

/********** Arrays **********/

boolean[] ret_boolean_arr(boolean[][] p) {}

byte[] ret_byte_arr(byte[][] p) {}

char[] ret_char_arr(char[][] p) {}

short[] ret_short_arr(short[][] p) {}

int[] ret_int_arr(int[][] p) {}

long[] ret_long_arr(long[][] p) {}

float[] ret_float_arr(float[][] p) {}

double[] ret_double_arr(double[][] p) {}

String[] ret_string_arr(String[][] p) {}

/********** Mixed types **********/

long pow(int num, int exp) {}

int strlen(String s) {}

String substr(String s, int start, int end) {}

String[] split(String s, String regex) {}

double avg(double[] nums, float delta) {}
