package xi.ast;

import java.util.Deque;
import java.util.Set;

import xi.sk.SKVisitor;

/**
 * Lambda expression.
 * 
 * @author Leo
 */
public class Lambda extends Expr {

    /** Variable name. */
    private final Name name;

    /**
     * Constructor.
     * 
     * @param n
     *            variable name
     * @param expr
     *            expression
     */
    public Lambda(final String n, final Expr expr) {
        super(expr);
        name = Name.valueOf(n);
    }

    @Override
    public final String toString() {
        return "{ " + name + " -> " + expr[0] + " }";
    }

    @Override
    public boolean hasFree(final Name var) {
        return !name.equals(var) && expr[0].hasFree(var);
    }

    /**
     * Getter for the Lambda expression's body.
     * 
     * @return body expression
     */
    public Expr getBody() {
        return expr[0];
    }

    /**
     * Getter for the lambda expression's variable name.
     * 
     * @return variable name
     */
    public String getName() {
        return name.getName();
    }

    @Override
    protected Expr unLambda(final Name n) {
        return expr[0].unLambda(n).unLambda(name);
    }

    @Override
    protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
        bound.push(name);
        getBody().freeVars(bound, free);
        bound.pop();
    }

    @Override
    public int numOfUses(final Name n) {
        return name.equals(n.toString()) ? 0 : expr[0].numOfUses(n);
    }

    @Override
    public Expr inline(final Name n, final Expr val) {
        if (!name.equals(n.toString())) {
            expr[0] = expr[0].inline(n, val);
        }
        return this;
    }

    @Override
    public boolean match(final Expr[] pat, final int l, final int r) {
        throw new IllegalStateException("Can only match expressions after"
                + " unLambda().");
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Lambda)) {
            return false;
        }
        final Lambda o = (Lambda) other;
        return name.equals(o.name) && expr[0].equals(o.expr[0]);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + expr[0].hashCode();
    }

    @Override
    public void traverse(final SKVisitor v) {
        throw new IllegalStateException("Can only traverse SK code.");
    }

}
