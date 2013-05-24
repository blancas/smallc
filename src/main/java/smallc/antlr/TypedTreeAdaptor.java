package smallc.antlr;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

/**
 * An ANTLR adaptor for creating TypedTree instances.
 */
public class TypedTreeAdaptor extends CommonTreeAdaptor {

    public Object create(Token token) {
        return new TypedTree(token);
    }

}
