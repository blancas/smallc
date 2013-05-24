package smallc;

/**
 * Interface to the Small-C Compiler.
 *
 * @author Armando Blancas
 * @version 1.0
 */
public interface ISmallcc {

    /**
     * Compiles a Small-C source file.
     *
     * @param source Where to find the source file.
     * @param target Where to write the class file.
     * @param classpath Where to find user class files.
     * @param file Name of the file to compile.
     * @return Zero on success or an error code.
     */
    int compile(String source, String target, String classpath, String file);

    /**
     * Gets the message from the last compiler result.
     *
     * @return A text description of the last result.
     */
    String getMessage();
}
