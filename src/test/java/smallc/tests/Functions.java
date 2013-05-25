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

import static smallc.antlr.SmallcLexer.IDENTIFIER;
import static smallc.antlr.SmallcLexer.QUALIFIED_NAME;
import static smallc.antlr.SmallcLexer.FUN;
import static smallc.antlr.SmallcLexer.PAR;
import static smallc.antlr.SmallcLexer.DIM;
import static smallc.antlr.SmallcLexer.BLK;

/**
 * Unit tests for function definitions.
 */
public class Functions extends BaseParser {

    public Functions() {
        //debug = true;
    }

    /*******************************************************************
     *                                                                 *
     * Definitions using simple types, with no args.                   *
     *                                                                 *
     *******************************************************************/

    @Test
    public void booleanDecl() {
        Tree tree = parse("boolean foo() { }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), BOOLEAN_TYPE);
	match(tree.getChild(2), PAR);
	match(tree.getChild(3), BLK);
    }

    /*******************************************************************
     *                                                                 *
     * Definitions using complex types, with no args.                  *
     *                                                                 *
     *******************************************************************/

    @Test
    public void booleanArrayDecl() {
        Tree root = parse("boolean[][] foo() { }");
        match(root, FUN);
        match(root.getChild(0), IDENTIFIER, "foo");
	    Tree tree = root.getChild(1);
            match(tree, BOOLEAN_TYPE);
            match(tree.getChild(0), DIM);
            match(tree.getChild(1), DIM);
	match(root.getChild(2), PAR);
	match(root.getChild(3), BLK);
    }

    @Test
    public void bigDecimalDecl() {
        Tree tree = parse("java.lang.BigInteger foo() { }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), QUALIFIED_NAME, "java.lang.BigInteger");
	match(tree.getChild(2), PAR);
	match(tree.getChild(3), BLK);
    }

    /*******************************************************************
     *                                                                 *
     * Definitions using formal parameters.                            *
     *                                                                 *
     *******************************************************************/

    @Test
    public void simpleFormalParamDecl() {
        Tree root = parse("int foo(long value) { }");
        match(root, FUN);
	match(root.getChild(0), IDENTIFIER, "foo");
	match(root.getChild(1), INT_TYPE);
            Tree params = root.getChild(2);
            match(params, PAR);
                Tree param = params.getChild(0);
                match(param, IDENTIFIER, "value");
                match(param.getChild(0), LONG_TYPE);
        match(root.getChild(3), BLK);
    }

    @Test
    public void complexFormalParamDecl() {
        Tree root = parse("int foo(long value, double x, float y) { }");
        match(root, FUN);
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
        match(root.getChild(3), BLK);
    }

    @Test
    public void arrayFormalParamDecl() {
        Tree root = parse("int foo(long[][] value) { }");
        match(root, FUN);
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
        match(root.getChild(3), BLK);
    }

    /*******************************************************************
     *                                                                 *
     * Complex types for return value and formal parameters.           *
     *                                                                 *
     *******************************************************************/

    @Test
    public void complexReturnAndFormalParamDecl() {
        Tree root = parse("int[][] foo(long[][] value, float x) { }");
        match(root, FUN);
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
        match(root.getChild(3), BLK);
    }

}
