package xi.ast;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import xi.util.Pair;

/**
 * SASL let-in expression.
 * 
 * @author Leo
 */
public class LetIn extends Expr {

    /** Name bindings. */
    private final Module defs;

    /**
     * Constructor.
     * 
     * @param let
     *            name bindings
     * @param in
     *            expression
     */
    public LetIn(final Module let, final Expr in) {
        super(in);
        defs = let;
    }

    @Override
    public final String toString() {
        return "let " + defs + " in " + expr[0];
    }

    @Override
    public final boolean hasFree(final Name var) {
        return defs.hasFree(var) || expr[0].hasFree(var);
    }

    /**
     * Getter for this let expression's body.
     * 
     * @return body of this let expression.
     */
    public final Expr getBody() {
        return expr[0];
    }

    /**
     * Definitions of this let expression.
     * 
     * @return definition module
     */
    public final Module getModule() {
        return defs;
    }

    @Override
    protected final void freeVars(final Deque<Name> b, final Set<Name> f) {
        int n = defs.getMap().size();
        for (final Name name : defs.getMap().keySet()) {
            b.push(name);
        }
        getBody().freeVars(b, f);
        while (n-- > 0) {
            b.pop();
        }
    }

    @Override
    protected final Expr unLambda(final Name n) {
        throw new IllegalStateException("unresolved let clause");
    }

    @Override
    public int numOfUses(final Name n) {
        final Map<Name, Expr> m = defs.getMap();
        if (m.containsKey(n)) {
            return 0;
        }
        int res = expr[0].numOfUses(n);
        for (final Expr e : m.values()) {
            res += e.numOfUses(n);
        }
        return res;
    }

    @Override
    public Expr inline(final Name name, final Expr val) {
        for (final Entry<Name, Expr> e : defs.getMap().entrySet()) {
            e.setValue(e.getValue().inline(name, val));
        }
        expr[0] = expr[0].inline(name, val);
        return this;
    }

    @Override
    public Expr unLet(final String fun, final Module m,
            final List<Pair<Name, Expr>> globalDefs, final List<String> args) {
        final Map<Name, Name> alphaMap = new HashMap<Name, Name>();
        final Map<Expr, Name> exprMap = new HashMap<Expr, Name>();
        final List<Expr> oldLocals = new LinkedList<Expr>();
        for (final Entry<Name, Expr> e : defs.getMap().entrySet()) {
            final Name old = e.getKey();
            final Name globalName = Name.valueOf(fun + "$" + old.getName());
            Expr globalExpr = e.getValue();
            int i = args.size();
            while (i-- > 0) {
                globalExpr = new Lambda(args.get(i), globalExpr);
            }
            alphaMap.put(old, globalName);
            exprMap.put(globalExpr, globalName);
            oldLocals.add(globalExpr);
            expr[0] = expr[0].alpha(old.getName(), globalName.getName(), args);
        }
        for (Expr e : oldLocals) {
            final Name own = exprMap.get(e);
            for (final Entry<Name, Name> alpha : alphaMap.entrySet()) {
                e = e.alpha(alpha.getKey().getName(), alpha.getValue()
                        .getName(), args);
            }
            m.addDefinition(own, e);
            globalDefs.add(new Pair<Name, Expr>(own, e));
        }
        return expr[0].unLet(fun, m, globalDefs, args);
    }
}
