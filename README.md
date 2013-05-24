## SmallC

A compiler for a subset of C.

## Contents:

* `dev.clj        `  - Snippets for development and testing.
* logback.xml      - Logback configuration file.
* pom.xml          - Maven build file.
* repl             - REPL launcher with the project's dependencies.
* src              - Source directories.
* target           - Build target.

### src/test/resources:

* logback-test.xml - Logback configuration file.
* simple.c         - Test file used in JUnit tests.
* smoke.c          - C file for testing the compiler.
* Smoking.java     - Java class for consuming smoke.class.
* interop_nnn.c    - C source test cases.

### src/test/java/smallc/test:

* BaseParser.java
* Declarations.java
* Expressions.java
* Functions.java
* Interop.java
* Statements.java

## License

Copyright © 2013 Armando Blancas.

Licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html).
