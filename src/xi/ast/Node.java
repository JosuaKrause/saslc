package xi.ast;

import java.util.logging.Logger;

import xi.sk.SKTree;
import xi.util.Logging;

/**
 * Interface for subSASL expressions.
 * 
 * @author Leo
 * @author Joschi
 */
public abstract class Node implements SKTree {

    /** Logger. */
    static final Logger log = Logging.getLogger(Node.class);

    @Override
    public abstract String toString();

    /**
     * Checks if the given variable is free in this Syntax tree.
     * 
     * @param var
     *            the variable
     * @return {@code true} if the variable is free, {@code false} otherwise
     */
    public abstract boolean hasFree(final Name var);

    /**
     * Creates a new AST in which all names below the top level are replaced by
     * combinators from the extended SK calculus.
     * 
     * @return transformed AST
     */
    public abstract Node unLambda();

    @Override
    public abstract boolean equals(final Object other);

    @Override
    public abstract int hashCode();

}
