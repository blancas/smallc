
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
import static smallc.antlr.SmallcLexer.PLUSEQ;
import static smallc.antlr.SmallcLexer.MINEQ;
import static smallc.antlr.SmallcLexer.MULTEQ;
import static smallc.antlr.SmallcLexer.DIVEQ;
import static smallc.antlr.SmallcLexer.TRUE;

import static smallc.antlr.SmallcLexer.FUN;
import static smallc.antlr.SmallcLexer.PAR;
import static smallc.antlr.SmallcLexer.DIM;
import static smallc.antlr.SmallcLexer.BLK;
import static smallc.antlr.SmallcLexer.VAR;
import static smallc.antlr.SmallcLexer.ARR;
import static smallc.antlr.SmallcLexer.INV;
import static smallc.antlr.SmallcLexer.ARG;
import static smallc.antlr.SmallcLexer.CEX;

/**
 * Unit tests for statements.
 */
public class Statements extends BaseParser {

    public Statements() {
        //debug = true;
    }

    /*******************************************************************
     *                                                                 *
     * Assignment.                                                     *
     *                                                                 *
     *******************************************************************/

    @Test
    public void simpleAssignment() {
        Tree tree = parse("void foo() { int pick; pick = 6502; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(1);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
        match(stmt.getChild(1), DECIMAL_LITERAL, "6502");
    }

    @Test
    public void simpleAssignmentReturnValue() {
        Tree tree = parse("void foo() { int pick; pick = bar(); }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(1);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
            Tree call = stmt.getChild(1);
            match(call, INV);
            match(call.getChild(0), IDENTIFIER, "bar");
            match(call.getChild(1), ARG);
    }

    @Test
    public void simpleConditionalAssignment() {
        Tree tree = parse("void foo() { int pick; pick = true ? foo : bar; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(1);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
        Tree expr = stmt.getChild(1);
        match(expr, CEX);
        match(expr.getChild(0), TRUE);
        match(expr.getChild(1), IDENTIFIER, "foo");
        match(expr.getChild(2), IDENTIFIER, "bar");
    }

    @Test
    public void simpleConditionalAssignmentFunctions() {
        Tree tree = parse("void foo() { pick = true ? foo() : bar(); }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "pick");
            Tree expr = stmt.getChild(1);
            match(expr, CEX);
            match(expr.getChild(0), TRUE);
                Tree f1 = expr.getChild(1);
                match(f1, INV);
                match(f1.getChild(0), IDENTIFIER, "foo");
                match(f1.getChild(1), ARG);
                Tree f2 = expr.getChild(2);
                match(f2, INV);
                match(f2.getChild(0), IDENTIFIER, "bar");
                match(f2.getChild(1), ARG);
    }

    @Test
    public void multipleSimpleAssignment() {
        Tree tree = parse("void foo() { retval = errcode = status = 0xFF; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, ASSIGN);
        match(stmt.getChild(0), IDENTIFIER, "retval");
	    Tree out = stmt.getChild(1);
            match(out, ASSIGN);
            match(out.getChild(0), IDENTIFIER, "errcode");
	        Tree in = out.getChild(1);
                match(in, ASSIGN);
                match(in.getChild(0), IDENTIFIER, "status");
                match(in.getChild(1), HEX_LITERAL, "0xFF");
    }

    @Test
    public void multipleInitializationWithAssignment() {
        Tree tree = parse("void foo() { int retval = errcode = status = 0xFF; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(0);
        match(stmt, VAR);
        match(stmt.getChild(0), INT_TYPE);
        match(stmt.getChild(1), IDENTIFIER, "retval");
	    Tree out = stmt.getChild(2);
            match(out, ASSIGN);
            match(out.getChild(0), IDENTIFIER, "errcode");
	        Tree in = out.getChild(1);
                match(in, ASSIGN);
                match(in.getChild(0), IDENTIFIER, "status");
                match(in.getChild(1), HEX_LITERAL, "0xFF");
    }

    @Test
    public void plusAssignment() {
        Tree tree = parse("void foo() { int pick; pick += 6502; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(1);
        match(stmt, PLUSEQ);
        match(stmt.getChild(0), IDENTIFIER, "pick");
        match(stmt.getChild(1), DECIMAL_LITERAL, "6502");
    }

    @Test
    public void minusAssignment() {
        Tree tree = parse("void foo() { int pick; pick -= 6502; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(1);
        match(stmt, MINEQ);
        match(stmt.getChild(0), IDENTIFIER, "pick");
        match(stmt.getChild(1), DECIMAL_LITERAL, "6502");
    }

    @Test
    public void multAssignment() {
        Tree tree = parse("void foo() { int pick; pick *= 6502; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(1);
        match(stmt, MULTEQ);
        match(stmt.getChild(0), IDENTIFIER, "pick");
        match(stmt.getChild(1), DECIMAL_LITERAL, "6502");
    }

    @Test
    public void divAssignment() {
        Tree tree = parse("void foo() { int pick; pick /= 6502; }");
        Tree block = tree.getChild(3);
	match(block, BLK);
        Tree stmt = block.getChild(1);
        match(stmt, DIVEQ);
        match(stmt.getChild(0), IDENTIFIER, "pick");
        match(stmt.getChild(1), DECIMAL_LITERAL, "6502");
    }

}
