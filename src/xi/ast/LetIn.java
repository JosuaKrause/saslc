package xi.ast;

import static xi.ast.BuiltIn.CONS;
import static xi.ast.BuiltIn.K;
import static xi.ast.BuiltIn.NIL;
import static xi.ast.BuiltIn.TL;
import static xi.ast.BuiltIn.U;
import static xi.ast.BuiltIn.Y;

import java.util.ArrayList;
import java.util.Collections;
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
        final LinkedList<Set<Name>> sorted = topoSort(map);

        Expr ex = getBody();
        for (final Set<Name> names : sorted) {
            if (names.size() == 1) {
                final Name nm = names.iterator().next();
                final Expr def = map.get(nm);
                final int uses = ex.numOfUses(nm);
                if (uses == 0) {
                    // name unused, ignore it
                    log.info("removing unused definition " + nm + ": " + def);
                } else if (uses == 1 || def instanceof Value) {
                    // can be inlined
                    log.info("inlining " + nm + ": " + def);
                    ex = ex.inline(nm, def);
                } else {
                    ex = App.create(new Lambda(nm.toString(), ex), map.get(nm));
                }
            } else {
                final Name[] na = names.toArray(new Name[names.size()]);

                Expr funs = NIL, res = K.app(ex);
                for (final Name element : na) {
                    funs = CONS.app(map.get(element), funs);
                }

                funs = K.app(funs);

                for (final Name element : na) {
                    funs = U.app(new Lambda(element.toString(), funs));
                    if (res.hasFree(element)) {
                        res = U.app(new Lambda(element.toString(), res));
                    } else {
                        // name unused, ignore it
                        log.info("rewriting unused name: U { " + element
                                + " -> f } ==> (f . tl)");
                        final Name nm = Name.createName();
                        res = new Lambda(nm.toString(), App.create(res, TL
                                .app(nm)));
                    }

                }

                ex = App.create(res, Y.app(funs));
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
    private LinkedList<Set<Name>> topoSort(final Map<Name, Expr> map) {

        final List<Name> names = new ArrayList<Name>(map.keySet());
        final Map<Name, Set<Name>> refs = new HashMap<Name, Set<Name>>();

        // find dependencies
        for (final Entry<Name, Expr> e : map.entrySet()) {
            final Set<Name> ref = e.getValue().freeVars();
            ref.retainAll(names);
            refs.put(e.getKey(), ref);
        }

        final Set<Name> marked = new HashSet<Name>();
        final Stack<Name> stack = new Stack<Name>();
        final LinkedList<Name> out = new LinkedList<Name>();
        final HashMap<Name, Set<Name>> equiv = new HashMap<Name, Set<Name>>();
        for (final Name n : names) {
            visit(n, refs, marked, stack, out, equiv);
        }

        marked.clear();
        final LinkedList<Set<Name>> res = new LinkedList<Set<Name>>();
        while (!out.isEmpty()) {
            final Name n = out.pollLast();
            if (marked.add(n)) {
                final Set<Name> eq = equiv.get(n);
                if (eq != null) {
                    marked.addAll(eq);
                    res.add(eq);
                } else {
                    res.add(Collections.singleton(n));
                }
            }
        }

        return res;
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
     * @param equiv
     *            equivalence classes
     */
    private void visit(final Name n, final Map<Name, Set<Name>> refs,
            final Set<Name> marked, final Stack<Name> stack,
            final List<Name> out, final Map<Name, Set<Name>> equiv) {
        if (marked.add(n)) { // name wasn't marked
            stack.push(n);

            for (final Name name : refs.get(n)) {
                visit(name, refs, marked, stack, out, equiv);
            }

            out.add(stack.pop());
        } else {
            final int pos = stack.lastIndexOf(n);
            if (pos >= 0) {
                final Set<Name> eqClass = new HashSet<Name>();
                for (final Name eq : stack.subList(pos, stack.size())) {
                    eqClass.add(eq);
                    final Set<Name> old = equiv.get(eq);
                    if (old != null) {
                        eqClass.addAll(old);
                    }
                }
                for (final Name nm : eqClass) {
                    equiv.put(nm, eqClass);
                }
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
        if (!defs.getMap().containsKey(name)) {
            for (final Entry<Name, Expr> e : defs.getMap().entrySet()) {
                e.setValue(e.getValue().inline(name, val));
            }
            expr[0] = expr[0].inline(name, val);
        }
        return this;
    }
}
