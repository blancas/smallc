package smallc.antlr;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 * An AST tree node that can store type information.
 *
 * <p>The type system uses type information for semantic
 * analysis and stores it in tree nodes so it can be
 * reused during code generation.
 */
public class TypedTree extends CommonTree {

    private String first;  // A child's type
    private String second; // A child's type
    private String third;  // A child's type
    private String lead;   // The tree's lead type

    public TypedTree(Token token) {
        super(token);
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String getThird() {
        return third;
    }

    public String getLead() {
        return lead;
    }

    public void setFirst(String t) {
        first = t;
    }

    public void setSecond(String t) {
        second = t;
    }

    public void setThird(String t) {
        third = t;
    }

    public void setLead(String t) {
        lead = t;
    }

}
