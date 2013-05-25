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

import static smallc.antlr.SmallcLexer.DECIMAL_LITERAL;
import static smallc.antlr.SmallcLexer.HEX_LITERAL;
import static smallc.antlr.SmallcLexer.IDENTIFIER;
import static smallc.antlr.SmallcLexer.QUALIFIED_NAME;
import static smallc.antlr.SmallcLexer.ASSIGN;
import static smallc.antlr.SmallcLexer.INC;
import static smallc.antlr.SmallcLexer.DEC;
import static smallc.antlr.SmallcLexer.PMM;
import static smallc.antlr.SmallcLexer.PPP;
import static smallc.antlr.SmallcLexer.PLUS;
import static smallc.antlr.SmallcLexer.MINUS;
import static smallc.antlr.SmallcLexer.NOT;
import static smallc.antlr.SmallcLexer.BWNOT;
import static smallc.antlr.SmallcLexer.MULT;
import static smallc.antlr.SmallcLexer.DIV;
import static smallc.antlr.SmallcLexer.MOD;
import static smallc.antlr.SmallcLexer.SHIFTL;
import static smallc.antlr.SmallcLexer.SHIFTR;
import static smallc.antlr.SmallcLexer.LESS;
import static smallc.antlr.SmallcLexer.LESSEQ;
import static smallc.antlr.SmallcLexer.GRTR;
import static smallc.antlr.SmallcLexer.GRTREQ;
import static smallc.antlr.SmallcLexer.EQUALS;
import static smallc.antlr.SmallcLexer.NOTEQ;
import static smallc.antlr.SmallcLexer.BWAND;
import static smallc.antlr.SmallcLexer.BWOR;
import static smallc.antlr.SmallcLexer.XOR;
import static smallc.antlr.SmallcLexer.AND;
import static smallc.antlr.SmallcLexer.OR;
import static smallc.antlr.SmallcLexer.TRUE;
import static smallc.antlr.SmallcLexer.FALSE;
import static smallc.antlr.SmallcLexer.CEX;
import static smallc.antlr.SmallcLexer.SEQ;

import static smallc.antlr.SmallcLexer.FUN;
import static smallc.antlr.SmallcLexer.PAR;
import static smallc.antlr.SmallcLexer.DIM;
import static smallc.antlr.SmallcLexer.BLK;
import static smallc.antlr.SmallcLexer.VAR;
import static smallc.antlr.SmallcLexer.ARR;
import static smallc.antlr.SmallcLexer.INV;
import static smallc.antlr.SmallcLexer.ARG;
import static smallc.antlr.SmallcLexer.FLD;
import static smallc.antlr.SmallcLexer.CST;

/**
 * Unit tests for expressions.
 */
public class Expressions extends BaseParser {

    public Expressions() {
        //debug = true;
    }

    /*******************************************************************
     *                                                                 *
     * Single-dimension array indexing.                                *
     *                                                                 *
     *******************************************************************/

    @Test
    public void assignArrayRval() {
        Tree tree = parse("void foo() { int[1] cpu; int pick = cpu[0]; }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), VOID_TYPE);
	match(tree.getChild(2), PAR);
            Tree block = tree.getChild(3);
	    match(block, BLK);
                Tree decl = block.getChild(0);
                match(decl, VAR);
		    Tree type = decl.getChild(0);
                    match(type, INT_TYPE);
                    match(type.getChild(0), DECIMAL_LITERAL, "1");
                match(decl.getChild(1), IDENTIFIER, "cpu");
                decl = block.getChild(1);
                match(decl, VAR);
                match(decl.getChild(0), INT_TYPE); 
                match(decl.getChild(1), IDENTIFIER, "pick");
		    Tree val = decl.getChild(2);
                    match(val, ARR);
                    match(val.getChild(0), IDENTIFIER, "cpu");
                    match(val.getChild(1), DECIMAL_LITERAL, "0");
    }

    @Test
    public void assignArrayLval() {
        Tree tree = parse("void foo() { int[1] cpu; cpu[0] = 6600; }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), VOID_TYPE);
	match(tree.getChild(2), PAR);
            Tree block = tree.getChild(3);
	    match(block, BLK);
                Tree decl = block.getChild(0);
                match(decl, VAR);
		    Tree type = decl.getChild(0);
                    match(type, INT_TYPE);
                    match(type.getChild(0), DECIMAL_LITERAL, "1");
                match(decl.getChild(1), IDENTIFIER, "cpu");
                Tree stmt = block.getChild(1);
                match(stmt, ASSIGN);
		    Tree val = stmt.getChild(0);
                    match(val, ARR);
                    match(val.getChild(0), IDENTIFIER, "cpu");
                    match(val.getChild(1), DECIMAL_LITERAL, "0");
		match(stmt.getChild(1), DECIMAL_LITERAL, "6600");
    }

    /*******************************************************************
     *                                                                 *
     * Multiple-dimension array indexing.                              *
     *                                                                 *
     *******************************************************************/

    @Test
    public void assignMultipleArrayRval() {
        Tree tree = parse("void foo() { int[256][128] cpu; int pick = cpu[15][10]; }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), VOID_TYPE);
	match(tree.getChild(2), PAR);
            Tree block = tree.getChild(3);
	    match(block, BLK);
                Tree decl = block.getChild(0);
                match(decl, VAR);
		    Tree type = decl.getChild(0);
                    match(type, INT_TYPE);
                    match(type.getChild(0), DECIMAL_LITERAL, "256");
                    match(type.getChild(1), DECIMAL_LITERAL, "128");
                match(decl.getChild(1), IDENTIFIER, "cpu");
                decl = block.getChild(1);
                match(decl, VAR);
                match(decl.getChild(0), INT_TYPE); 
                match(decl.getChild(1), IDENTIFIER, "pick");
		    Tree out = decl.getChild(2);
                    match(out, ARR);
		        Tree in = out.getChild(0);
                        match(in, ARR);
                        match(in.getChild(0), IDENTIFIER, "cpu");
                        match(in.getChild(1), DECIMAL_LITERAL, "15");
                    match(out.getChild(1), DECIMAL_LITERAL, "10");
    }

    @Test
    public void assignMultipleArrayLval() {
        Tree tree = parse("void foo() { int[256][128] cpu; cpu[15][10] = 68000; }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), VOID_TYPE);
	match(tree.getChild(2), PAR);
            Tree block = tree.getChild(3);
	    match(block, BLK);
                Tree decl = block.getChild(0);
                match(decl, VAR);
		    Tree type = decl.getChild(0);
                    match(type, INT_TYPE);
                    match(type.getChild(0), DECIMAL_LITERAL, "256");
                    match(type.getChild(1), DECIMAL_LITERAL, "128");
                match(decl.getChild(1), IDENTIFIER, "cpu");
                Tree stmt = block.getChild(1);
                match(stmt, ASSIGN);
		    Tree out = stmt.getChild(0);
                    match(out, ARR);
		        Tree in = out.getChild(0);
                        match(in, ARR);
                        match(in.getChild(0), IDENTIFIER, "cpu");
                        match(in.getChild(1), DECIMAL_LITERAL, "15");
                    match(out.getChild(1), DECIMAL_LITERAL, "10");
		match(stmt.getChild(1), DECIMAL_LITERAL, "68000");
    }

    /*******************************************************************
     *                                                                 *
     * Function call.                                                  *
     *                                                                 *
     *******************************************************************/

    @Test
    public void simpleFunctionCall() {
        Tree tree = parse("void foo() { bar(); }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), VOID_TYPE);
	match(tree.getChild(2), PAR);
            Tree block = tree.getChild(3);
	    match(block, BLK);
                Tree call = block.getChild(0);
                match(call, INV);
                match(call.getChild(0), IDENTIFIER, "bar");
                match(call.getChild(1), ARG); 
    }

    /*******************************************************************
     *                                                                 *
     * Field access.                                                   *
     *                                                                 *
     *******************************************************************/

    @Test
    public void simpleFieldAccess() {
        Tree tree = parse("void foo() { int pick = cpu.PDP_11; }");
        match(tree, FUN);
	match(tree.getChild(0), IDENTIFIER, "foo");
	match(tree.getChild(1), VOID_TYPE);
	match(tree.getChild(2), PAR);
            Tree block = tree.getChild(3);
	    match(block, BLK);
                Tree decl = block.getChild(0);
                match(decl, VAR);
                match(decl.getChild(0), INT_TYPE);
                match(decl.getChild(1), IDENTIFIER, "pick");
                match(decl.getChild(2), QUALIFIED_NAME, "cpu.PDP_11");
    }

    @Test
    public void fieldAccessOnArrayElement() {
        Tree tree = parse("void foo() { pick = cpu[0].PDP_11; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree val = stmt.getChild(1);
            match(val, FLD);
                Tree arr = val.getChild(0);
                match(arr, ARR);
                match(arr.getChild(0), IDENTIFIER, "cpu");
                match(arr.getChild(1), DECIMAL_LITERAL, "0");
            match(val.getChild(1), IDENTIFIER, "PDP_11");
    }

    @Test
    public void fieldAccessOnReturnValue() {
        Tree tree = parse("void foo() { pick = cpu().PDP_11; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree val = stmt.getChild(1);
            match(val, FLD);
                Tree arr = val.getChild(0);
                match(arr, INV);
                match(arr.getChild(0), IDENTIFIER, "cpu");
                match(arr.getChild(1), ARG);
            match(val.getChild(1), IDENTIFIER, "PDP_11");
    }

    /*******************************************************************
     *                                                                 *
     * Increment and decrement.                                        *
     *                                                                 *
     *******************************************************************/

    @Test
    public void postfixIncrement() {
        Tree tree = parse("void foo() { pick = cpu++; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree val = stmt.getChild(1);
            match(val, PPP);
            match(val.getChild(0), IDENTIFIER, "cpu");
    }

    @Test
    public void prefixIncrement() {
        Tree tree = parse("void foo() { pick = ++cpu; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree val = stmt.getChild(1);
            match(val, INC);
            match(val.getChild(0), IDENTIFIER, "cpu");
    }

    @Test
    public void postfixIncrementArray() {
        Tree tree = parse("void foo() { pick = cpu[0]++; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree val = stmt.getChild(1);
            match(val, PPP);
	        Tree arr = val.getChild(0);
                match(arr, ARR);
                match(arr.getChild(0), IDENTIFIER, "cpu");
                match(arr.getChild(1), DECIMAL_LITERAL, "0");
    }

    @Test
    public void prefixIncrementArray() {
        Tree tree = parse("void foo() { pick = ++cpu[0]; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree val = stmt.getChild(1);
            match(val, INC);
	        Tree arr = val.getChild(0);
                match(arr, ARR);
                match(arr.getChild(0), IDENTIFIER, "cpu");
                match(arr.getChild(1), DECIMAL_LITERAL, "0");
    }

    @Test
    public void prefixIncrementVariableTwice() {
        Tree tree = parse("void foo() { pick = ++++cpu; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree out = stmt.getChild(1);
            match(out, INC);
	        Tree in = out.getChild(0);
                match(in, INC);
                match(in.getChild(0), IDENTIFIER, "cpu");
    }

    @Test
    public void postfixDecrementVariableTwice() {
        Tree tree = parse("void foo() { pick = cpu----; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
	    Tree out = stmt.getChild(1);
            match(out, PMM);
	        Tree in = out.getChild(0);
                match(in, PMM);
                match(in.getChild(0), IDENTIFIER, "cpu");
    }

    /*******************************************************************
     *                                                                 *
     * Unary expressions.                                              *
     *                                                                 *
     *******************************************************************/

    @Test
    public void unaryPlus() {
        Tree tree = parse("void foo() { n = +1; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, PLUS);
            match(val.getChild(0), DECIMAL_LITERAL, "1");
    }

    @Test
    public void unaryMinus() {
        Tree tree = parse("void foo() { n = -100; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, MINUS);
            match(val.getChild(0), DECIMAL_LITERAL, "100");
    }

    @Test
    public void unaryNot() {
        Tree tree = parse("void foo() { flag = !found; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, NOT);
            match(val.getChild(0), IDENTIFIER, "found");
    }

    @Test
    public void unaryBitwiseNot() {
        Tree tree = parse("void foo() { n = ~0x7F3A; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, BWNOT);
            match(val.getChild(0), HEX_LITERAL, "0x7F3A");
    }

    @Test
    public void unaryIncrement() {
        Tree tree = parse("void foo() { n = ++bar; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, INC);
            match(val.getChild(0), IDENTIFIER, "bar");
    }

    @Test
    public void unaryDecrement() {
        Tree tree = parse("void foo() { n = --bar; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, DEC);
            match(val.getChild(0), IDENTIFIER, "bar");
    }

    @Test
    public void unarySimpleCast() {
        Tree tree = parse("void foo() { n = (int) bar; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, CST);
            match(val.getChild(0), INT_TYPE);
            match(val.getChild(1), IDENTIFIER, "bar");
    }

    @Test
    public void unaryTypeCast() {
        Tree tree = parse("void foo() { s = (String) bar; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "s");
	    Tree val = stmt.getChild(1);
            match(val, CST);
            match(val.getChild(0), IDENTIFIER, "String");
            match(val.getChild(1), IDENTIFIER, "bar");
    }

    @Test
    public void unaryQualifiedTypeCast() {
        Tree tree = parse("void foo() { s = (java.lang.String) bar; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "s");
	    Tree val = stmt.getChild(1);
            match(val, CST);
            match(val.getChild(0), QUALIFIED_NAME, "java.lang.String");
            match(val.getChild(1), IDENTIFIER, "bar");
    }

    /*******************************************************************
     *                                                                 *
     * Multiplicative expressions.                                     *
     *                                                                 *
     *******************************************************************/

    @Test
    public void multiplyLiterals() {
        Tree tree = parse("void foo() { n =  1024 * 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, MULT);
            match(val.getChild(0), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void multiplyVariables() {
        Tree tree = parse("void foo() { line_item = quantity * price; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "line_item");
	    Tree val = stmt.getChild(1);
            match(val, MULT);
            match(val.getChild(0), IDENTIFIER, "quantity");
            match(val.getChild(1), IDENTIFIER, "price");
    }

    @Test
    public void multiplyLiteralsAndVariables() {
        Tree tree = parse("void foo() { bytes = 1024 * 1024 * N; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "bytes");
	    Tree val = stmt.getChild(1);
            match(val, MULT);
	        Tree term = val.getChild(0);
                match(term, MULT);
                match(term.getChild(0), DECIMAL_LITERAL, "1024");
                match(term.getChild(1), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), IDENTIFIER, "N");
    }

    @Test
    public void divideVariables() {
        Tree tree = parse("void foo() { unit_price = total / quantity; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "unit_price");
	    Tree val = stmt.getChild(1);
            match(val, DIV);
            match(val.getChild(0), IDENTIFIER, "total");
            match(val.getChild(1), IDENTIFIER, "quantity");
    }

    @Test
    public void moduloVariables() {
        Tree tree = parse("void foo() { change = quaters % 4; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "change");
	    Tree val = stmt.getChild(1);
            match(val, MOD);
            match(val.getChild(0), IDENTIFIER, "quaters");
            match(val.getChild(1), DECIMAL_LITERAL, "4");
    }

    /*******************************************************************
     *                                                                 *
     * Additive expressions.                                           *
     *                                                                 *
     *******************************************************************/

    @Test
    public void addLiterals() {
        Tree tree = parse("void foo() { n =  1024 + 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, PLUS);
            match(val.getChild(0), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void addVariables() {
        Tree tree = parse("void foo() { total = subtotal + tax; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "total");
	    Tree val = stmt.getChild(1);
            match(val, PLUS);
            match(val.getChild(0), IDENTIFIER, "subtotal");
            match(val.getChild(1), IDENTIFIER, "tax");
    }

    @Test
    public void subtractLiterals() {
        Tree tree = parse("void foo() { n =  1024 - 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, MINUS);
            match(val.getChild(0), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void subtractVariables() {
        Tree tree = parse("void foo() { subtotal = total - tax; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "subtotal");
	    Tree val = stmt.getChild(1);
            match(val, MINUS);
            match(val.getChild(0), IDENTIFIER, "total");
            match(val.getChild(1), IDENTIFIER, "tax");
    }

    @Test
    public void addMultiplyLiterals() {
        Tree tree = parse("void foo() { n =  1024 * 512 + 2048 * 1024; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, PLUS);
	        Tree left = val.getChild(0);
                match(left, MULT);
                match(left.getChild(0), DECIMAL_LITERAL, "1024");
                match(left.getChild(1), DECIMAL_LITERAL, "512");
	        Tree right = val.getChild(1);
                match(right, MULT);
                match(right.getChild(0), DECIMAL_LITERAL, "2048");
                match(right.getChild(1), DECIMAL_LITERAL, "1024");
    }

    @Test
    public void mixedMultSubDivVariables() {
        Tree tree = parse("void foo() { bytes = len * items - used / units; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "bytes");
	    Tree val = stmt.getChild(1);
            match(val, MINUS);
	        Tree left = val.getChild(0);
                match(left, MULT);
                match(left.getChild(0), IDENTIFIER, "len");
                match(left.getChild(1), IDENTIFIER, "items");
	        Tree right = val.getChild(1);
                match(right, DIV);
                match(right.getChild(0), IDENTIFIER, "used");
                match(right.getChild(1), IDENTIFIER, "units");
    }

    /*******************************************************************
     *                                                                 *
     * Shift expressions.                                              *
     *                                                                 *
     *******************************************************************/

    @Test
    public void shiftLetf() {
        Tree tree = parse("void foo() { n =  1024 << 4; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, SHIFTL);
            match(val.getChild(0), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), DECIMAL_LITERAL, "4");
    }

    @Test
    public void shiftRight() {
        Tree tree = parse("void foo() { n =  size >> 4; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "n");
	    Tree val = stmt.getChild(1);
            match(val, SHIFTR);
            match(val.getChild(0), IDENTIFIER, "size");
            match(val.getChild(1), DECIMAL_LITERAL, "4");
    }

    @Test
    public void shiftRightAfterMult() {
        Tree tree = parse("void foo() { len =  size >>  2 * i ; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "len");
	    Tree val = stmt.getChild(1);
            match(val, SHIFTR);
            match(val.getChild(0), IDENTIFIER, "size");
            Tree term = val.getChild(1);
            match(term, MULT);
            match(term.getChild(0), DECIMAL_LITERAL, "2");
            match(term.getChild(1), IDENTIFIER, "i");
    }

    /*******************************************************************
     *                                                                 *
     * Relational expressions.                                         *
     *                                                                 *
     *******************************************************************/

    @Test
    public void lessThanLiterals() {
        Tree tree = parse("void foo() { flag =  1024 < 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, LESS);
            match(val.getChild(0), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void lessThanOrEqualLiterals() {
        Tree tree = parse("void foo() { flag =  1024 <= 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, LESSEQ);
            match(val.getChild(0), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void greaterThanOrEqualVariables() {
        Tree tree = parse("void foo() { flag = total >= tax; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, GRTREQ);
            match(val.getChild(0), IDENTIFIER, "total");
            match(val.getChild(1), IDENTIFIER, "tax");
    }

    @Test
    public void greaterThanVariables() {
        Tree tree = parse("void foo() { flag = total > tax * subtotal + 1; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, GRTR);
            match(val.getChild(0), IDENTIFIER, "total");
                Tree sum = val.getChild(1);
                match(sum, PLUS);
                    Tree mult = sum.getChild(0);
                    match(mult, MULT);
                    match(mult.getChild(0), IDENTIFIER, "tax");
                    match(mult.getChild(1), IDENTIFIER, "subtotal");
                match(sum.getChild(1), DECIMAL_LITERAL, "1");
    }

    /*******************************************************************
     *                                                                 *
     * Relational expressions.                                         *
     *                                                                 *
     *******************************************************************/

    @Test
    public void equalsLiterals() {
        Tree tree = parse("void foo() { flag =  512 == 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, EQUALS);
            match(val.getChild(0), DECIMAL_LITERAL, "512");
            match(val.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void notEqualsLiterals() {
        Tree tree = parse("void foo() { flag =  1024 != 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, NOTEQ);
            match(val.getChild(0), DECIMAL_LITERAL, "1024");
            match(val.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void equalVariables() {
        Tree tree = parse("void foo() { flag = total == tax; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, EQUALS);
            match(val.getChild(0), IDENTIFIER, "total");
            match(val.getChild(1), IDENTIFIER, "tax");
    }

    @Test
    public void notEqualsVariables() {
        Tree tree = parse("void foo() { flag = total != tax * subtotal + 1; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, NOTEQ);
            match(val.getChild(0), IDENTIFIER, "total");
                Tree sum = val.getChild(1);
                match(sum, PLUS);
                    Tree mult = sum.getChild(0);
                    match(mult, MULT);
                    match(mult.getChild(0), IDENTIFIER, "tax");
                    match(mult.getChild(1), IDENTIFIER, "subtotal");
                match(sum.getChild(1), DECIMAL_LITERAL, "1");
    }

    /*******************************************************************
     *                                                                 *
     * Bitwise expressions.                                            *
     *                                                                 *
     *******************************************************************/

    @Test
    public void bitwiseAndLiterals() {
        Tree tree = parse("void foo() { flag =  0xCAFE0000 & 0xBABE ; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, BWAND);
            match(val.getChild(0), HEX_LITERAL, "0xCAFE0000");
            match(val.getChild(1), HEX_LITERAL, "0xBABE");
    }

    @Test
    public void bitwiseOrVariables() {
        Tree tree = parse("void foo() { flag =  value | mask ; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, BWOR);
            match(val.getChild(0), IDENTIFIER, "value");
            match(val.getChild(1), IDENTIFIER, "mask");
    }

    @Test
    public void bitwiseXorMixed() {
        Tree tree = parse("void foo() { flag =  0xCAFEBABE ^ mask ; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, XOR);
            match(val.getChild(0), HEX_LITERAL, "0xCAFEBABE");
            match(val.getChild(1), IDENTIFIER, "mask");
    }

    /*******************************************************************
     *                                                                 *
     * Logical expressions.                                            *
     *                                                                 *
     *******************************************************************/

    @Test
    public void andSimpleLiterals() {
        Tree tree = parse("void foo() { flag = true && false; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, AND);
            match(val.getChild(0), TRUE);
            match(val.getChild(1), FALSE);
    }

    @Test
    public void andLiterals() {
        Tree tree = parse("void foo() { flag = 512 == 512 && 1024 > 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, AND);
	        Tree left = val.getChild(0);
                match(left, EQUALS);
                match(left.getChild(0), DECIMAL_LITERAL, "512");
                match(left.getChild(1), DECIMAL_LITERAL, "512");
	        Tree right = val.getChild(1);
                match(right, GRTR);
                match(right.getChild(0), DECIMAL_LITERAL, "1024");
                match(right.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void andSimpleVariables() {
        Tree tree = parse("void foo() { flag = status && found; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, AND);
            match(val.getChild(0), IDENTIFIER, "status");
            match(val.getChild(1), IDENTIFIER, "found");
    }

    @Test
    public void andVariables() {
        Tree tree = parse("void foo() { flag = cost <= price && date > firstDay; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, AND);
	        Tree left = val.getChild(0);
                match(left, LESSEQ);
                match(left.getChild(0), IDENTIFIER, "cost");
                match(left.getChild(1), IDENTIFIER, "price");
	        Tree right = val.getChild(1);
                match(right, GRTR);
                match(right.getChild(0), IDENTIFIER, "date");
                match(right.getChild(1), IDENTIFIER, "firstDay");
    }

    @Test
    public void orSimpleVariables() {
        Tree tree = parse("void foo() { flag = true || false; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, OR);
            match(val.getChild(0), TRUE);
            match(val.getChild(1), FALSE);
    }

    @Test
    public void orLiterals() {
        Tree tree = parse("void foo() { flag = 512 == 512 || 1024 > 512; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, OR);
	        Tree left = val.getChild(0);
                match(left, EQUALS);
                match(left.getChild(0), DECIMAL_LITERAL, "512");
                match(left.getChild(1), DECIMAL_LITERAL, "512");
	        Tree right = val.getChild(1);
                match(right, GRTR);
                match(right.getChild(0), DECIMAL_LITERAL, "1024");
                match(right.getChild(1), DECIMAL_LITERAL, "512");
    }

    @Test
    public void orLiteralsWithParens() {
        Tree tree = parse("void foo() { flag = ( (512 == 512) || (1024 > 512) ); }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val = stmt.getChild(1);
            match(val, OR);
	        Tree left = val.getChild(0);
                match(left, EQUALS);
                match(left.getChild(0), DECIMAL_LITERAL, "512");
                match(left.getChild(1), DECIMAL_LITERAL, "512");
	        Tree right = val.getChild(1);
                match(right, GRTR);
                match(right.getChild(0), DECIMAL_LITERAL, "1024");
                match(right.getChild(1), DECIMAL_LITERAL, "512");
    }

    /*******************************************************************
     *                                                                 *
     * Conditional expressions.                                        *
     *                                                                 *
     *******************************************************************/

    @Test
    public void conditionalWithLiterals() {
        Tree tree = parse("void foo() { size =  true ? 1024 : 2048; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "size");
	    Tree val = stmt.getChild(1);
            match(val, CEX);
            match(val.getChild(0), TRUE);
            match(val.getChild(1), DECIMAL_LITERAL, "1024");
            match(val.getChild(2), DECIMAL_LITERAL, "2048");
    }

    @Test
    public void conditionalWithVariables() {
        Tree tree = parse("void foo() { total = size > LIMIT  ? n : m; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "total");
	    Tree val = stmt.getChild(1);
            match(val, CEX);
	        Tree test = val.getChild(0);
                match(test, GRTR);
                match(test.getChild(0), IDENTIFIER, "size");
                match(test.getChild(1), IDENTIFIER, "LIMIT");
            match(val.getChild(1), IDENTIFIER, "n");
            match(val.getChild(2), IDENTIFIER, "m");
    }

    /*******************************************************************
     *                                                                 *
     * Assignment expressions.                                         *
     *                                                                 *
     *******************************************************************/

    @Test
    public void multipleAssignVariables() {
        Tree tree = parse("void foo() { flag = status = code = false; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "flag");
	    Tree val1 = stmt.getChild(1);
            match(val1, ASSIGN);
            match(val1.getChild(0), IDENTIFIER, "status"); 
                Tree val2 = val1.getChild(1);
                match(val2, ASSIGN);
                match(val2.getChild(0), IDENTIFIER, "code");
                match(val2.getChild(1), FALSE);
    }

    @Test
    public void simpleSequential() {
        Tree tree = parse("void foo() { i = 0, j = 0, k = -1; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, SEQ);
	    Tree val1 = stmt.getChild(0);
            match(val1, SEQ);
	        Tree exp1 = val1.getChild(0);
                match(exp1, ASSIGN);
                match(exp1.getChild(0), IDENTIFIER, "i"); 
                match(exp1.getChild(1), DECIMAL_LITERAL, "0"); 
                Tree exp2 = val1.getChild(1);
                match(exp2, ASSIGN);
                match(exp2.getChild(0), IDENTIFIER, "j");
                match(exp2.getChild(1), DECIMAL_LITERAL, "0");
	    Tree val2 = stmt.getChild(1);
            match(val2, ASSIGN);
            match(val2.getChild(0), IDENTIFIER, "k");
	        Tree val3 = val2.getChild(1);
                match(val3, MINUS);
                match(val3.getChild(0), DECIMAL_LITERAL, "1");
    }

}
