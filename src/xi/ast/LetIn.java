package xi.ast;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

/**
 * SASL let-in expression.
 * 
 * @author Leo
 */
public class LetIn extends Expr {

    // TODO: define alpha and unLet and call it before unlambda

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
        final Map<Name, Expr> map = defs.unLambda().getMap();
        final LinkedList<Name> sorted = topoSort(map);

        Expr ex = getBody();
        while (!sorted.isEmpty()) {
            final Name name = sorted.pollLast();
            final Expr def = map.get(name);
            final int uses = ex.numOfUses(name);
            switch (uses) {
            case 0:
                // name unused, ignore it
                System.err.println("removing unused definition " + name + ": "
                        + def);
                break;
            case 1:
                // can be inlined
                System.err.println("inlining " + name + ": " + def);
                ex = ex.inline(name, def);
                break;
            default:
                ex = App.create(new Lambda(name.toString(), ex), map.get(name));
                break;
            }
        }

        return ex.unLambda(n);
    }

    /**
     * Sorts the definitions topologically according to their dependency order.
     * 
     * @param map
     *            map of definitions
     * @return ordered list of definitions
     */
    private LinkedList<Name> topoSort(final Map<Name, Expr> map) {

        final List<Name> names = new ArrayList<Name>(map.keySet());
        final Map<Name, Set<Name>> refs = new HashMap<Name, Set<Name>>();

        for (final Entry<Name, Expr> e : map.entrySet()) {
            final Set<Name> ref = new HashSet<Name>(e.getValue().freeVars());
            ref.retainAll(names);
            refs.put(e.getKey(), ref);
        }

        final Set<Name> marked = new HashSet<Name>();
        final Stack<Name> stack = new Stack<Name>();
        final LinkedList<Name> out = new LinkedList<Name>();
        for (final Name n : names) {
            visit(n, refs, marked, stack, out);
        }

        return out;
    }

    /**
     * Depth-first search on the dependency graph. The correctness of this
     * algorithm follows from the invariant that a function is only added to the
     * output after all its dependencies have been added.
     * 
     * @param n
     *            name of the function to be visited
     * @param refs
     *            adjacency map
     * @param marked
     *            already visited nodes
     * @param stack
     *            stack of current nodes
     * @param out
     *            output list
     */
    private void visit(final Name n, final Map<Name, Set<Name>> refs,
            final Set<Name> marked, final Stack<Name> stack,
            final List<Name> out) {
        if (marked.add(n)) { // name wasn't marked
            stack.push(n);

            for (final Name name : refs.get(n)) {
                visit(name, refs, marked, stack, out);
            }

            stack.pop();
            out.add(n);
        } else {
            final int pos = stack.lastIndexOf(n);
            if (pos >= 0) {
                // TODO support mutually recursive functions
                throw new IllegalArgumentException("found loop: "
                        + stack.subList(pos, stack.size()));
            }
        }
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
            final List<Expr> globalDefs, final List<String> args) {
        final Map<Name, Name> alphaMap = new HashMap<Name, Name>();
        final Map<Expr, Name> exprMap = new HashMap<Expr, Name>();
        final List<Expr> oldLocals = new LinkedList<Expr>();
        for (final Entry<Name, Expr> e : defs.getMap().entrySet()) {
            final Name old = e.getKey();
            final Name globalName = Name.valueOf(fun + "$" + old.getName());
            Expr globalExpr = e.getValue();
            for (final String arg : args) {
                globalExpr = new Lambda(arg, globalExpr);
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
            globalDefs.add(e);
        }
        return expr[0];
    }
}
