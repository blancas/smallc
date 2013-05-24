package smallc;

/**
 * Signals a compile error.
 *
 * <p>This exception is thrown to stop the compilation
 * process and return a diagnostic up the call stack.
 */
public class CompileException extends Exception {

    private int code;

    public CompileException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
