package xi.compiler;

import java.util.LinkedList;

/**
 * Builds a fully functional Tupel.
 * 
 * @author Joschi
 * 
 */
public class TupelBuilder {

    /** The list containing the Expressions. */
    private final LinkedList<Expr> list;

    /**
     * Creates a TupelBuilder containing the expression {@code e}.
     * 
     * @param e
     *            The first expression.
     */
    public TupelBuilder(final Expr e) {
        list = new LinkedList<Expr>();
        list.add(e);
    }

    /**
     * Prepends the expression to the list.
     * 
     * @param e
     *            The first expression.
     * @return This TupelBuilder.
     */
    public TupelBuilder addFirst(final Expr e) {
        list.addFirst(e);
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
