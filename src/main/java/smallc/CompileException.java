// Copyright (c) 2013 Armando Blancas. All rights reserved.
// The use and distribution terms for this software are covered by the
// Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
// which can be found in the file epl-v10.html at the root of this distribution.
// By using this software in any fashion, you are agreeing to be bound by
// the terms of this license.
// You must not remove this notice, or any other, from this software.

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
