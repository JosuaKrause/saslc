package xi.ast;

import static xi.ast.BuiltIn.K;
import static xi.ast.BuiltIn.S;

import java.util.Deque;
import java.util.Set;

/**
 * SASL function application.
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public final class App extends Expr {

    /**
     * Creates an application-node for the given expressions.
     * 
     * @param e
     *            First expression
     * @param es
     *            further expressions
     * @return The application-node.
     */
    public static Expr create(final Expr e, final Expr... es) {
        Expr res = e;
        for (final Expr x : es) {
            res = new App(res, x);
        }
        return res;
    }

    /**
     * Constructor taking a function and an argument.
     * 
     * @param f
     *            function expression
     * @param x
     *            argument expression
     */
    private App(final Expr f, final Expr x) {
        super(f, x);
    }

    @Override
    public String toString() {
        return "(" + expr[0] + " " + expr[1] + ')';
    }

    /**
     * Getter for the function.
     * 
     * @return the function expression
     */
    public Expr getLeft() {
        return expr[0];
    }

    /**
     * Getter for the value.
     * 
     * @return the value expression
     */
    public Expr getRight() {
        return expr[1];
    }

    @Override
    protected Expr unLambda(final Name n) {
        final Expr f = getLeft(), x = getRight();
        if (n == null) {
            return App.create(f.unLambda(), x.unLambda());
        }
        final boolean nf = !f.hasFree(n), nx = !x.hasFree(n);

        // (\x . a b) => K (a b), if x is free in neither a nor b
        if (nf && nx) {
            return K.app(this);
        }

        // (\x . f x) => f, if x isn't free in f
        if (nf && !nx && x.equals(n)) {
            return nf ? f : f.unLambda(n);
        }

        return S.app(nf ? K.app(f) : f.unLambda(n), nx ? K.app(x) : x
                .unLambda(n));
    }

    @Override
    protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
        getLeft().freeVars(bound, free);
        getRight().freeVars(bound, free);
    }

    @Override
    public int numOfUses(final Name n) {
        return expr[0].numOfUses(n) + expr[1].numOfUses(n);
    }

    @Override
    public Expr inline(final Name name, final Expr val) {
        expr[0] = expr[0].inline(name, val);
        expr[1] = expr[1].inline(name, val);
        return this;
    }

}
