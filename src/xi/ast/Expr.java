package xi.ast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Expressions in subSASL.
 * 
 * @author Leo
 * @author Joschi
 */
public abstract class Expr extends Node {

    /** Only use S, K for Lambda, not B, C. */
    final static boolean DISABLE_BC = false;

    /** Argument array. */
    protected final Expr[] expr;

    /**
     * Constructor taking argument expressions.
     * 
     * @param args
     *            arguments
     */
    public Expr(final Expr... args) {
        expr = args;
    }

    @Override
    public boolean hasFree(final Name var) {
        for (final Expr e : expr) {
            if (e.hasFree(var)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new {@link Expr expression} where all occurrences of {@code n}
     * are replaced by S and K combinators.
     * 
     * @param n
     *            name of the variable to eliminate
     * @return SK expression
     */
    protected abstract Expr unLambda(final Name n);

    @Override
    public final Expr unLambda() {
        return unLambda(null);
    }

    /**
     * Set of free variables in this expression.
     * 
     * @return free variables
     */
    public final Set<Name> freeVars() {
        final Set<Name> free = new HashSet<Name>();
        freeVars(new ArrayDeque<Name>(), free);
        return free;
    }

    /**
     * Collects all free variables of this expression.
     * 
     * @param bound
     *            bound variables
     * @param free
     *            free variables
     */
    protected abstract void freeVars(final Deque<Name> bound,
            final Set<Name> free);

    /**
     * Checks how often the given name is used in this expression.
     * 
     * @param n
     *            name to be checked
     * @return number of uses
     */
    public abstract int numOfUses(final Name n);

    /**
     * Inlines the given expression.
     * 
     * @param name
     *            name of the expression
     * @param val
     *            the expression
     * @return expression with inlined expression
     */
    public abstract Expr inline(final Name name, final Expr val);
}
