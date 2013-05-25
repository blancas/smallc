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

/**
 * Base class for test suites.
 */
public class BaseParser {

    protected boolean debug = false;

    protected CommonTree parse(String code) {
        CommonTree tree = null;
        try {
            ANTLRStringStream input = new ANTLRStringStream(code);
	    SmallcLexer lexer = new SmallcLexer(input);
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    SmallcParser parser = new SmallcParser(tokens);
	    SmallcParser.compilation_unit_return result = parser.compilation_unit();
            tree = (CommonTree) result.getTree();
          
            if (debug) {
                System.out.println(tree.toStringTree());
            }
	} catch (RecognitionException re) {
            // fall through
        }
        return tree;
    }

    protected void match(Tree tree, int type) {
        if (debug) return;
        CommonTree ct = (CommonTree) tree;
        Assert.assertTrue(!ct.isNil() && ct.getType() == type);
    }

    protected void match(Tree tree, int[] types) {
        if (debug) return;
        CommonTree ct = (CommonTree) tree;
        Assert.assertFalse(ct.isNil());
        boolean foundIt = false;
        int targetType = ct.getType();
        for (int type : types) {
            if (type == targetType) {
                foundIt = true;
                break;
            }
        }
        Assert.assertTrue(foundIt);
    }

    protected void match(Tree tree, int type, String text) {
        if (debug) return;
        CommonTree ct = (CommonTree) tree;
        Assert.assertTrue( 
            !ct.isNil() && ct.getType() == type && ct.getText().equals(text));
    }


    // NOTE: I'm not stupid; JUnit made me do it.
    @Test
    public void dummy() {
        Assert.assertTrue(true);
    }

}
