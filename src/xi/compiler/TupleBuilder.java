package xi.compiler;

import java.util.LinkedList;

/**
 * Builds a fully functional tuple.
 * 
 * @author Joschi
 * 
 */
public class TupleBuilder {

    /** The list containing the Expressions. */
    private final LinkedList<Expr> list;

    /**
     * Creates an empty tuple builder.
     */
    public TupleBuilder() {
        list = new LinkedList<Expr>();
    }

    /**
     * Prepends the expression to the list.
     * 
     * @param e
     *            The first expression.
     * @return This TupleBuilder.
     */
    public TupleBuilder addFirst(final Expr e) {
        list.addFirst(e);
        return this;
    }

    /**
     * Appends the expression to the list.
     * 
     * @param e
     *            The last expression.
     * @return This TupleBuilder.
     */
    public TupleBuilder addLast(final Expr e) {
        list.addLast(e);
        return this;
    }

    /**
     * Returns the expressions as a Array.
     * 
     * @return All expressions combined as an Array.
     */
    public Expr[] getExpr() {
        return list.toArray(new Expr[list.size()]);
    }

}
