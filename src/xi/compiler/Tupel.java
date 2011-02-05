package xi.compiler;

import static xi.compiler.BuiltIn.K;

import java.util.Deque;
import java.util.Set;

import xi.sk.SKVisitor;

/**
 * Represents a tupel of expressions.
 * 
 * @author Joschi
 * 
 */
public class Tupel extends Expr {

    /**
     * Creates a tupel containing only one expression.
     * 
     * @param e
     *            The expression.
     */
    public Tupel(final Expr e) {
        super(e);
    }

    /**
     * Creates a tupel containing the expression {@code e} and all expressions
     * given by {@code t}.
     * 
     * @param e
     *            The first expression.
     * @param t
     *            The following expressions.
     */
    public Tupel(final Expr e, final Tupel t) {
        super(t.combined(e));
    }

    /**
     * Combines the expressions of the tupel with a new one, which is prepended.
     * 
     * @param e
     *            The first expression.
     * @return All expressions combined.
     */
    private Expr[] combined(final Expr e) {
        // TODO: maybe use LinkedLists...
        final int newLength = expr.length + 1;
        int i = 0;
        final Expr[] res = new Expr[newLength];
        res[i++] = e;
        for (final Expr exp : expr) {
            res[i++] = exp;
        }
        return res;
    }

    @Override
    protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
        for (final Expr e : expr) {
            e.freeVars(bound, free);
        }
    }

    @Override
    public Expr inline(final Name name, final Expr val) {
        int i = expr.length;
        while (i-- > 0) {
            expr[i] = expr[i].inline(name, val);
        }
        return this;
    }

    @Override
    boolean match(final Expr[] pat, final int l, final int r) {
        // TODO: implement correct matching...
        if (r - l == 1) {
            pat[l] = this;
            return true;
        }
        return false;
    }

    @Override
    public int numOfUses(final Name n) {
        final int count = 0;
        for (final Expr e : expr) {
            e.numOfUses(n);
        }
        return count;
    }

    @Override
    protected Expr unLambda(final Name n) {
        if (n == null) {
            int i = expr.length;
            while (i-- > 0) {
                expr[i] = expr[i].unLambda();
            }
            return this;
        }
        if (!hasFree(n)) {
            return K.app(this);
        }
        int i = expr.length;
        while (i-- > 0) {
            expr[i] = expr[i].unLambda(n);
        }
        return this;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Tupel)) {
            return false;
        }
        final Tupel o = (Tupel) other;
        if (o.expr.length != expr.length) {
            return false;
        }
        int i = expr.length;
        while (i-- > 0) {
            if (!expr[i].equals(o.expr[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int mul = 1;
        int res = 0;
        for (final Expr e : expr) {
            res += e.hashCode() * mul;
            mul *= 31;
        }
        return res;
    }

    @Override
    public String toString() {
        boolean first = true;
        final StringBuilder sb = new StringBuilder("(");
        for (final Expr e : expr) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(e.toString());
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void traverse(final SKVisitor v) {
        // TODO think of traversing method...
    }

}
