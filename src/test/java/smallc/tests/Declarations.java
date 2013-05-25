// Copyright (c) 2013 Armando Blancas. All rights reserved.
// The use and distribution terms for this software are covered by the
// Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
// which can be found in the file epl-v10.html at the root of this distribution.
// By using this software in any fashion, you are agreeing to be bound by
// the terms of this license.
// You must not remove this notice, or any other, from this software.

package smallc.tests;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import org.junit.Assert;
import org.junit.Test;

import smallc.antlr.SmallcLexer;
import smallc.antlr.SmallcParser;

import static smallc.antlr.SmallcLexer.BOOLEAN_TYPE;
import static smallc.antlr.SmallcLexer.BYTE_TYPE;
import static smallc.antlr.SmallcLexer.CHAR_TYPE;
import static smallc.antlr.SmallcLexer.SHORT_TYPE;
import static smallc.antlr.SmallcLexer.INT_TYPE;
import static smallc.antlr.SmallcLexer.FLOAT_TYPE;
import static smallc.antlr.SmallcLexer.LONG_TYPE;
import static smallc.antlr.SmallcLexer.DOUBLE_TYPE;
import static smallc.antlr.SmallcLexer.VOID_TYPE;

import static smallc.antlr.SmallcLexer.DEFINE;
import static smallc.antlr.SmallcLexer.IDENTIFIER;
import static smallc.antlr.SmallcLexer.DECIMAL_LITERAL;
import static smallc.antlr.SmallcLexer.HEX_LITERAL;
import static smallc.antlr.SmallcLexer.OCTAL_LITERAL;
import static smallc.antlr.SmallcLexer.FLOAT_LITERAL;
import static smallc.antlr.SmallcLexer.STRING_LITERAL;
import static smallc.antlr.SmallcLexer.CHAR_LITERAL;

import static smallc.antlr.SmallcLexer.DEFINE;
import static smallc.antlr.SmallcLexer.IDENTIFIER;
import static smallc.antlr.SmallcLexer.QUALIFIED_NAME;
import static smallc.antlr.SmallcLexer.TRUE;
import static smallc.antlr.SmallcLexer.FALSE;

import static smallc.antlr.SmallcLexer.VAR;
import static smallc.antlr.SmallcLexer.FWD;
import static smallc.antlr.SmallcLexer.PAR;
import static smallc.antlr.SmallcLexer.DIM;
import static smallc.antlr.SmallcLexer.BLK;

/**
 * Unit tests for declarations.
 */
public class Declarations extends BaseParser {

    public Declarations() {
        //debug = true;
    }

    /*******************************************************************
     *                                                                 *
     * Simple definitions of symbolic constants.                       *
     *                                                                 *
     *******************************************************************/

    @Test
    public void decimalDef() {
        Tree tree = parse("#define foo 6502;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,      "foo");
        match(tree.getChild(1), DECIMAL_LITERAL, "6502");
    }

    @Test
    public void decimalDef_l() {
        Tree tree = parse("#define foo 6502l;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,      "foo");
        match(tree.getChild(1), DECIMAL_LITERAL, "6502l");
    }

    @Test
    public void decimalDef_L() {
        Tree tree = parse("#define foo 6502L;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,      "foo");
        match(tree.getChild(1), DECIMAL_LITERAL, "6502L");
    }

    @Test
    public void hexDef() {
        Tree tree = parse("#define foo 0xCAFEBABE;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,  "foo");
        match(tree.getChild(1), HEX_LITERAL, "0xCAFEBABE");
    }

    @Test
    public void hexDef_l() {
        Tree tree = parse("#define foo 0xCAFEBABEl;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,  "foo");
        match(tree.getChild(1), HEX_LITERAL, "0xCAFEBABEl");
    }

    @Test
    public void hexDef_L() {
        Tree tree = parse("#define foo 0xCAFEBABEL;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,  "foo");
        match(tree.getChild(1), HEX_LITERAL, "0xCAFEBABEL");
    }

    @Test
    public void octalDef() {
        Tree tree = parse("#define foo 06502;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,    "foo");
        match(tree.getChild(1), OCTAL_LITERAL, "06502");
    }

    @Test
    public void octalDef_l() {
        Tree tree = parse("#define foo 06502l;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,    "foo");
        match(tree.getChild(1), OCTAL_LITERAL, "06502l");
    }

    @Test
    public void octalDef_L() {
        Tree tree = parse("#define foo 06502L;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,    "foo");
        match(tree.getChild(1), OCTAL_LITERAL, "06502L");
    }

    @Test
    public void doubleDef() {
        Tree tree = parse("#define PI 3.1415927;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,             "PI");
        match(tree.getChild(1), FLOAT_LITERAL, "3.1415927");
    }

    @Test
    public void doubleDef_d() {
        Tree tree = parse("#define PI 3.1415927d;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,             "PI");
        match(tree.getChild(1), FLOAT_LITERAL, "3.1415927d");
    }

    @Test
    public void doubleDef_D() {
        Tree tree = parse("#define PI 3.1415927D;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,             "PI");
        match(tree.getChild(1), FLOAT_LITERAL, "3.1415927D");
    }

    @Test
    public void floatDef_f() {
        Tree tree = parse("#define PI 3.1415927f;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,             "PI");
        match(tree.getChild(1), FLOAT_LITERAL, "3.1415927f");
    }

    @Test
    public void doubleDef_F() {
        Tree tree = parse("#define PI 3.1415927F;");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,             "PI");
        match(tree.getChild(1), FLOAT_LITERAL, "3.1415927F");
    }

    @Test
    public void stringDef() {
        Tree tree = parse("#define foo \"foo bar\";");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,     "foo");
        match(tree.getChild(1), STRING_LITERAL, "\"foo bar\"");
    }

    @Test
    public void empty_stringDef() {
        Tree tree = parse("#define foo \"\";");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,     "foo");
        match(tree.getChild(1), STRING_LITERAL, "\"\"");
    }

    @Test
    public void characterDef() {
        Tree tree = parse("#define foo '@';");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,        "foo");
        match(tree.getChild(1), CHAR_LITERAL, "'@'");
    }

    @Test
    public void stringDef_escape() {
        Tree tree = parse("#define foo \"foo \\u000D \\377 \\\\ bar\";");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,     "foo");
        match(tree.getChild(1), STRING_LITERAL, "\"foo \\u000D \\377 \\\\ bar\"");
    }

    @Test
    public void stringDef_escape_ex() {
        Tree tree = parse("#define foo \"foo \\b \\t \\n \\f \\r \\' \";");
        match(tree, DEFINE);
        match(tree.getChild(0), IDENTIFIER,     "foo");
        match(tree.getChild(1), STRING_LITERAL, "\"foo \\b \\t \\n \\f \\r \\' \"");
    }

    /*******************************************************************
     *                                                                 *
     * Compound declaration of symbolic constants.                     *
     *                                                                 *
     *******************************************************************/

    @Test
    public void compoundDef() {
        Tree tree = parse("#define { foo 6502; bar 8088; flag true; }");
        Tree decl = tree.getChild(0);
        match(decl, DEFINE);
        match(decl.getChild(0), IDENTIFIER,      "foo");
        match(decl.getChild(1), DECIMAL_LITERAL, "6502");
        decl = tree.getChild(1);
        match(decl, DEFINE);
        match(decl.getChild(0), IDENTIFIER,      "bar");
        match(decl.getChild(1), DECIMAL_LITERAL, "8088");
        decl = tree.getChild(2);
        match(decl, DEFINE);
        match(decl.getChild(0), IDENTIFIER,      "flag");
        match(decl.getChild(1), TRUE);
    }

    /*******************************************************************
     *                                                                 *
     * Simple variable declarations with no assignment.                *
     *                                                                 *
     *******************************************************************/

    @Test
    public void booleanDecl() {
        Tree tree = parse("boolean foo;");
        match(tree, VAR);
	match(tree.getChild(0), BOOLEAN_TYPE);
	match(tree.getChild(1), IDENTIFIER, "foo");
    }

    @Test
    public void byteDecl() {
        Tree tree = parse("byte foo;");
        match(tree, VAR);
        match(tree.getChild(0), BYTE_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
    }

    @Test
    public void charDecl() {
        Tree tree = parse("char foo;");
        match(tree, VAR);
        match(tree.getChild(0), CHAR_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
    }

    @Test
    public void shortDecl() {
        Tree tree = parse("short foo;");
        match(tree, VAR);
        match(tree.getChild(0), SHORT_TYPE); 
        match(tree.getChild(1), IDENTIFIER, "foo");
   }

    @Test
    public void intDecl() {
        Tree tree = parse("int foo;");
        match(tree, VAR);
        match(tree.getChild(0), INT_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
    }

    @Test
    public void floatDecl() {
        Tree tree = parse("float foo;");
        match(tree, VAR);
        match(tree.getChild(0), FLOAT_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
    }

    @Test
    public void longDecl() {
        Tree tree = parse("long foo;");
        match(tree, VAR);
        match(tree.getChild(0), LONG_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
    }

    @Test
    public void doubleDecl() {
        Tree tree = parse("double foo;");
        match(tree, VAR);
        match(tree.getChild(0), DOUBLE_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
    }

    /*******************************************************************
     *                                                                 *
     * Variable declarations with assignment.                          *
     *                                                                 *
     *******************************************************************/

    @Test
    public void booleanAssignDecl() {
        Tree tree = parse("boolean foo = true;");
        match(tree, VAR);
	match(tree.getChild(0), BOOLEAN_TYPE);
	match(tree.getChild(1), IDENTIFIER, "foo");
	match(tree.getChild(2), TRUE);
    }

    @Test
    public void decimal() {
        Tree tree = parse("int foo = 6502;");
        match(tree, VAR);
        match(tree.getChild(0), INT_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), DECIMAL_LITERAL, "6502");
    }

    @Test
    public void decimal_l() {
        Tree tree = parse("long foo = 6502l;");
        match(tree, VAR);
        match(tree.getChild(0), LONG_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), DECIMAL_LITERAL, "6502l");
    }

    @Test
    public void decimal_L() {
        Tree tree = parse("long foo = 6502L;");
        match(tree, VAR);
        match(tree.getChild(0), LONG_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), DECIMAL_LITERAL, "6502L");
    }

    @Test
    public void hex() {
        Tree tree = parse("int foo = 0xCAFEBABE;");
        match(tree, VAR);
        match(tree.getChild(0), INT_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), HEX_LITERAL, "0xCAFEBABE");
    }

    @Test
    public void hex_l() {
        Tree tree = parse("long foo = 0xCAFEBABEl;");
        match(tree, VAR);
        match(tree.getChild(0), LONG_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), HEX_LITERAL, "0xCAFEBABEl");
    }

    @Test
    public void hex_L() {
        Tree tree = parse("long foo = 0xCAFEBABEL;");
        match(tree, VAR);
        match(tree.getChild(0), LONG_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), HEX_LITERAL, "0xCAFEBABEL");
    }

    @Test
    public void octal() {
        Tree tree = parse("int foo = 06502;");
        match(tree, VAR);
        match(tree.getChild(0), INT_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), OCTAL_LITERAL, "06502");
    }

    @Test
    public void octal_l() {
        Tree tree = parse("long foo = 06502l;");
        match(tree, VAR);
        match(tree.getChild(0), LONG_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), OCTAL_LITERAL, "06502l");
    }

    @Test
    public void octal_L() {
        Tree tree = parse("long foo = 06502L;");
        match(tree, VAR);
        match(tree.getChild(0), LONG_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), OCTAL_LITERAL, "06502L");
    }

    @Test
    public void doubleDeclEx() {
        Tree tree = parse("double PI = 3.1415927;");
        match(tree, VAR);
        match(tree.getChild(0), DOUBLE_TYPE);
        match(tree.getChild(1), IDENTIFIER, "PI");
        match(tree.getChild(2), FLOAT_LITERAL, "3.1415927");
    }

    @Test
    public void double_d() {
        Tree tree = parse("double PI = 3.1415927d;");
        match(tree, VAR);
        match(tree.getChild(0), DOUBLE_TYPE);
        match(tree.getChild(1), IDENTIFIER, "PI");
        match(tree.getChild(2), FLOAT_LITERAL, "3.1415927d");
    }

    @Test
    public void double_D() {
        Tree tree = parse("double PI = 3.1415927D;");
        match(tree, VAR);
        match(tree.getChild(0), DOUBLE_TYPE);
        match(tree.getChild(1), IDENTIFIER, "PI");
        match(tree.getChild(2), FLOAT_LITERAL, "3.1415927D");
    }

    @Test
    public void float_f() {
        Tree tree = parse("float PI = 3.1415927f;");
        match(tree, VAR);
        match(tree.getChild(0), FLOAT_TYPE);
        match(tree.getChild(1), IDENTIFIER, "PI");
        match(tree.getChild(2), FLOAT_LITERAL, "3.1415927f");
    }

    @Test
    public void float_F() {
        Tree tree = parse("float PI = 3.1415927F;");
        match(tree, VAR);
        match(tree.getChild(0), FLOAT_TYPE);
        match(tree.getChild(1), IDENTIFIER, "PI");
        match(tree.getChild(2), FLOAT_LITERAL, "3.1415927F");
    }

    @Test
    public void string() {
        Tree tree = parse("String foo = \"foo bar\";");
        match(tree, VAR);
        match(tree.getChild(0), IDENTIFIER, "String");
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), STRING_LITERAL, "\"foo bar\"");
    }

    @Test
    public void empty_string() {
        Tree tree = parse("java.lang.String foo = \"\";");
        match(tree, VAR);
        match(tree.getChild(0), QUALIFIED_NAME, "java.lang.String");
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), STRING_LITERAL, "\"\"");
    }

    @Test
    public void character() {
        Tree tree = parse("char foo = '@';");
        match(tree, VAR);
        match(tree.getChild(0), CHAR_TYPE);
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), CHAR_LITERAL, "'@'");
    }

    @Test
    public void string_escape() {
        Tree tree = parse("String foo = \"foo \\u000D \\377 \\\\ bar\";");
        match(tree, VAR);
        match(tree.getChild(0), IDENTIFIER, "String");
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), STRING_LITERAL, "\"foo \\u000D \\377 \\\\ bar\"");
    }

    @Test
    public void string_escape_ex() {
        Tree tree = parse("String foo = \"foo \\b \\t \\n \\f \\r \\' \";");
        match(tree, VAR);
        match(tree.getChild(0), IDENTIFIER, "String");
        match(tree.getChild(1), IDENTIFIER, "foo");
        match(tree.getChild(2), STRING_LITERAL, "\"foo \\b \\t \\n \\f \\r \\' \"");
    }

    /*******************************************************************
     *                                                                 *
     * Simple variable declarations with arrays and no assignment.     *
     *                                                                 *
     *******************************************************************/

    @Test
    public void intArrayDecl() {
        Tree tree = parse("int[] foo;");
        match(tree, VAR);
	match(tree.getChild(1), IDENTIFIER, "foo");
        tree = tree.getChild(0);
	match(tree, INT_TYPE);
	match(tree.getChild(0), DIM);
    }

    @Test
    public void intMultiArrayDecl() {
        Tree tree = parse("int[][] foo;");
        match(tree, VAR);
	match(tree.getChild(1), IDENTIFIER, "foo");
        tree = tree.getChild(0);
	match(tree, INT_TYPE);
	match(tree.getChild(0), DIM);
	match(tree.getChild(1), DIM);
    }

    /*******************************************************************
     *                                                                 *
     * Variable declarations with initialized arrays and no assignment.*
     *                                                                 *
     *******************************************************************/

    @Test
    public void intMultiArrayInitDecl() {
        Tree tree = parse("int[10][20] foo;");
        match(tree, VAR);
	match(tree.getChild(1), IDENTIFIER, "foo");
        tree = tree.getChild(0);
	match(tree, INT_TYPE);
	match(tree.getChild(0), DECIMAL_LITERAL, "10");
	match(tree.getChild(1), DECIMAL_LITERAL, "20");
    }

    /*******************************************************************
     *                                                                 *
     * Multiple variable declarations with assignments.                *
     *                                                                 *
     *******************************************************************/

    @Test
    public void booleanMultipleAssign() {
        Tree root = parse("boolean foo = true, bar = false;");
        Tree foo = root.getChild(0);
        Tree bar = root.getChild(1);
        match(foo, VAR);
        match(foo.getChild(0), BOOLEAN_TYPE);
        match(foo.getChild(1), IDENTIFIER, "foo");
        match(foo.getChild(2), TRUE);
        match(bar, VAR);
        match(bar.getChild(0), BOOLEAN_TYPE);
        match(bar.getChild(1), IDENTIFIER, "bar");
        match(bar.getChild(2), FALSE);
    }

    @Test
    public void decimalMultipleDecl() {
        Tree root = parse("int foo, bar, foobar;");
        Tree foo = root.getChild(0);
        Tree bar = root.getChild(1);
        Tree foobar = root.getChild(2);
        match(foo, VAR);
        match(foo.getChild(0), INT_TYPE);
        match(foo.getChild(1), IDENTIFIER, "foo");
        match(bar, VAR);
        match(bar.getChild(0), INT_TYPE);
        match(bar.getChild(1), IDENTIFIER, "bar");
        match(foobar, VAR);
        match(foobar.getChild(0), INT_TYPE);
        match(foobar.getChild(1), IDENTIFIER, "foobar");
    }

    @Test
    public void decimalMultipleAssign() {
        Tree root = parse("int foo = 6502, bar = 8080, foobar = 68000;");
        Tree foo = root.getChild(0);
        Tree bar = root.getChild(1);
        Tree foobar = root.getChild(2);
        match(foo, VAR);
        match(foo.getChild(0), INT_TYPE);
        match(foo.getChild(1), IDENTIFIER, "foo");
	match(foo.getChild(2), DECIMAL_LITERAL, "6502");
        match(bar, VAR);
        match(bar.getChild(0), INT_TYPE);
        match(bar.getChild(1), IDENTIFIER, "bar");
	match(bar.getChild(2), DECIMAL_LITERAL, "8080");
        match(foobar, VAR);
        match(foobar.getChild(0), INT_TYPE);
        match(foobar.getChild(1), IDENTIFIER, "foobar");
	match(foobar.getChild(2), DECIMAL_LITERAL, "68000");
    }

    @Test
    public void decimalMixedAssign() {
        Tree root = parse("int foo, bar = 386, foobar;");
        Tree foo = root.getChild(0);
        Tree bar = root.getChild(1);
        Tree foobar = root.getChild(2);
        match(foo, VAR);
        match(foo.getChild(0), INT_TYPE);
        match(foo.getChild(1), IDENTIFIER, "foo");
        match(bar, VAR);
        match(bar.getChild(0), INT_TYPE);
        match(bar.getChild(1), IDENTIFIER, "bar");
        match(bar.getChild(2), DECIMAL_LITERAL, "386");
        match(foobar, VAR);
        match(foobar.getChild(0), INT_TYPE);
        match(foobar.getChild(1), IDENTIFIER, "foobar");
    }

    @Test
    public void doubleArrayMultiple() {
        Tree root = parse("double[][] foo, bar, foobar;");
        Tree foo = root.getChild(0);
        Tree bar = root.getChild(1);
        Tree foobar = root.getChild(2);
        match(foo, VAR);
	match(foo.getChild(1), IDENTIFIER, "foo");
            Tree tree = foo.getChild(0);
	    match(tree, DOUBLE_TYPE);
	    match(tree.getChild(0), DIM);
	    match(tree.getChild(1), DIM);
        match(bar, VAR);
	match(bar.getChild(1), IDENTIFIER, "bar");
            tree = bar.getChild(0);
	    match(tree, DOUBLE_TYPE);
	    match(tree.getChild(0), DIM);
	    match(tree.getChild(1), DIM);
        match(foobar, VAR);
	match(foobar.getChild(1), IDENTIFIER, "foobar");
            tree = foobar.getChild(0);
	    match(tree, DOUBLE_TYPE);
	    match(tree.getChild(0), DIM);
	    match(tree.getChild(1), DIM);
    }

    @Test
    public void doubleArrayMultipleInit() {
        Tree root = parse("double[200][300] foo, bar, foobar;");
        Tree foo = root.getChild(0);
        Tree bar = root.getChild(1);
        Tree foobar = root.getChild(2);
        match(foo, VAR);
	match(foo.getChild(1), IDENTIFIER, "foo");
            Tree tree = foo.getChild(0);
	    match(tree, DOUBLE_TYPE);
	    match(tree.getChild(0), DECIMAL_LITERAL, "200");
	    match(tree.getChild(1), DECIMAL_LITERAL, "300");
        match(bar, VAR);
	match(bar.getChild(1), IDENTIFIER, "bar");
            tree = bar.getChild(0);
	    match(tree, DOUBLE_TYPE);
	    match(tree.getChild(0), DECIMAL_LITERAL, "200");
	    match(tree.getChild(1), DECIMAL_LITERAL, "300");
        match(foobar, VAR);
	match(foobar.getChild(1), IDENTIFIER, "foobar");
            tree = foobar.getChild(0);
	    match(tree, DOUBLE_TYPE);
	    match(tree.getChild(0), DECIMAL_LITERAL, "200");
	    match(tree.getChild(1), DECIMAL_LITERAL, "300");
    }

    /*******************************************************************
     *                                                                 *
     * Forward declarations using simple types, with no args.          *
     *                                                                 *
     *******************************************************************/

    @Test
    public void booleanFwd() {
        Tree tree = parse("boolean foo();");
        match(tree, FWD);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), BOOLEAN_TYPE);
	match(tree.getChild(2), PAR);
    }

    /*******************************************************************
     *                                                                 *
     * Forward declarations using complex types, with no args.         *
     *                                                                 *
     *******************************************************************/

    @Test
    public void booleanArrayFwd() {
        Tree root = parse("boolean[][] foo();");
        match(root, FWD);
        match(root.getChild(0), IDENTIFIER, "foo");
	    Tree tree = root.getChild(1);
            match(tree, BOOLEAN_TYPE);
            match(tree.getChild(0), DIM);
            match(tree.getChild(1), DIM);
	match(root.getChild(2), PAR);
    }

    @Test
    public void bigDecimalFwd() {
        Tree tree = parse("java.lang.BigInteger foo();");
        match(tree, FWD);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), QUALIFIED_NAME, "java.lang.BigInteger");
	match(tree.getChild(2), PAR);
    }

    /*******************************************************************
     *                                                                 *
     * Forward declarations using formal parameters.                   *
     *                                                                 *
     *******************************************************************/

    @Test
    public void simpleFormalParamFwd() {
        Tree root = parse("int foo(long value);");
        match(root, FWD);
	match(root.getChild(0), IDENTIFIER, "foo");
	match(root.getChild(1), INT_TYPE);
            Tree params = root.getChild(2);
            match(params, PAR);
                Tree param = params.getChild(0);
                match(param, IDENTIFIER, "value");
                match(param.getChild(0), LONG_TYPE);
    }

    @Test
    public void complexFormalParamFwd() {
        Tree root = parse("int foo(long value, double x, float y);");
        match(root, FWD);
	match(root.getChild(0), IDENTIFIER, "foo");
	match(root.getChild(1), INT_TYPE);
            Tree params = root.getChild(2);
            match(params, PAR);
                Tree param = params.getChild(0);
                match(param, IDENTIFIER, "value");
                match(param.getChild(0), LONG_TYPE);
                param = params.getChild(1);
                match(param, IDENTIFIER, "x");
                match(param.getChild(0), DOUBLE_TYPE);
                param = params.getChild(2);
                match(param, IDENTIFIER, "y");
                match(param.getChild(0), FLOAT_TYPE);
    }

    @Test
    public void arrayFormalParamFwd() {
        Tree root = parse("int foo(long[][] value);");
        match(root, FWD);
	match(root.getChild(0), IDENTIFIER, "foo");
	match(root.getChild(1), INT_TYPE);
            Tree params = root.getChild(2);
            match(params, PAR);
                Tree param = params.getChild(0);
                match(param, IDENTIFIER, "value");
		    Tree type = param.getChild(0);
                    match(type, LONG_TYPE);
                    match(type.getChild(0), DIM);
                    match(type.getChild(1), DIM);
    }

    /*******************************************************************
     *                                                                 *
     * Complex types for return value and formal parameters.           *
     *                                                                 *
     *******************************************************************/

    @Test
    public void complexReturnAndFormalParamFwd() {
        Tree root = parse("int[][] foo(long[][] value, float x);");
        match(root, FWD);
        match(root.getChild(0), IDENTIFIER, "foo");
            Tree ret = root.getChild(1);
            match(ret, INT_TYPE);
            match(ret.getChild(0), DIM);
            match(ret.getChild(1), DIM);
            Tree params = root.getChild(2);
            match(params, PAR);
                Tree param = params.getChild(0);
                match(param, IDENTIFIER, "value");
                    Tree type = param.getChild(0);
                    match(type, LONG_TYPE);
                    match(type.getChild(0), DIM);
                    match(type.getChild(1), DIM);
                param = params.getChild(1);
                match(param, IDENTIFIER, "x");
                match(param.getChild(0), FLOAT_TYPE);
    }

}
