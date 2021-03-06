package xi.compiler;

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

    /**
     * Tries to match the given pattern against this subtree, filling gaps with
     * the corresponding expressions. {@code match([K, null, null], 0, 3)}
     * invoked on a subtree {@code ((K 1) 3)} should return {@code true},
     * filling the pattern as follows: {@code [K, 1, 3]}.
     * 
     * @param pat
     *            Pattern to match against
     * @param l
     *            left delimiter
     * @param r
     *            right delimiter
     * @return whether the pattern matched.
     */
    abstract boolean match(final Expr[] pat, final int l, final int r);

    /**
     * Tries to match the given pattern against this subtree, returning a copy
     * of the pattern with filled in gaps on success, {@code null} otherwise.
     * {@code match([K, null, null])} invoked on a subtree {@code ((K 1) 3)}
     * should return {@code [K, 1, 3]}.
     * 
     * @param pattern
     *            Pattern to match against
     * @return the matched expressions or {@code null}
     */
    public Expr[] match(final Expr[] pattern) {
        final Expr[] copy = pattern.clone();
        return match(copy, 0, copy.length) ? copy : null;
    }
}
