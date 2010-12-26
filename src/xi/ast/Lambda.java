package xi.ast;

import java.util.Deque;
import java.util.List;
import java.util.Set;

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

    public Expr getBody() {
        return expr[0];
    }

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
    public Expr alpha(final String name, final String newName,
            final List<String> args) {
        if (!this.name.getName().equals(name)) {
            expr[0] = expr[0].alpha(name, newName, args);
        }
        return this;
    }

    @Override
    public void getArgs(final List<String> args) {
        args.add(name.getName());
        expr[0].getArgs(args);
    }

}
