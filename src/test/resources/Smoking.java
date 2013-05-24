// Test access in package classes.
import static pkg.smallc.simple.MAX_BYTE;

public class Smoking {

    /*
     * Simbolic constant in org.pkg.smallc.simple.c
     */
    static void printSimpleGlobals() {
        System.out.printf("pkg.smallc.simple.MAX_BYTE = %d\n", MAX_BYTE);
    }

    /*
     * Symbolic constants and global variables in smoke.c
     */
    static void printSmokeGlobals() {
        System.out.printf("PI        = %f\n", smoke.PI);
        System.out.printf("N         = %d\n", smoke.N);
        System.out.printf("MAGIC     = %X\n", smoke.MAGIC);
        System.out.printf("PRIVATE   = %d\n", smoke.PRIVATE);
        System.out.printf("PUBLIC    = %d\n", smoke.PUBLIC);
        System.out.printf("PROTECTED = %d\n", smoke.PROTECTED);
        System.out.printf("PACKAGE   = %d\n", smoke.PACKAGE);
        System.out.printf("STATIC    = %d\n", smoke.STATIC);
        System.out.printf("FINAL     = %d\n", smoke.FINAL);
        System.out.printf("ABSTRACT  = %d\n", smoke.ABSTRACT);
        System.out.printf("SEPARATOR = %c\n", smoke.SEPARATOR);
        System.out.printf("MESSAGE   = %s\n", smoke.MESSAGE);
        System.out.printf("status    = %d\n", smoke.status);
        System.out.printf("beginning = %d\n", smoke.beginning);
        System.out.printf("ending    = %d\n", smoke.ending);
        System.out.printf("alfa      = %f\n", smoke.alfa);
        System.out.printf("beta      = %f\n", smoke.beta);
        System.out.printf("delta     = %f\n", smoke.delta);
        System.out.printf("empty     = %b\n", smoke.empty);
    }

    public static void main(String[] args) {
        printSmokeGlobals();
        System.out.printf("\n");
        printSimpleGlobals();
        System.out.printf("\nsmoke.add(3, 4) = %d\n", smoke.add(3, 4));
        System.out.printf("\nsmoke.firstExpr() [3 + 4]= %d\n", smoke.firstExpr());
    }

}
