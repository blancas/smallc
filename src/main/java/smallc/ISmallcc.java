// Copyright (c) 2013 Armando Blancas. All rights reserved.
// The use and distribution terms for this software are covered by the
// Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
// which can be found in the file epl-v10.html at the root of this distribution.
// By using this software in any fashion, you are agreeing to be bound by
// the terms of this license.
// You must not remove this notice, or any other, from this software.

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
