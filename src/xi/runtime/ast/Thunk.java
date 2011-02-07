package xi.runtime.ast;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import xi.runtime.ast.prim.Char;
import xi.runtime.ast.prim.Function;
import xi.runtime.ast.prim.List;
import xi.runtime.ast.prim.Num;
import xi.runtime.ast.prim.Str;
import xi.runtime.ast.prim.Value;
import xi.sk.SKTree;
import xi.sk.SKVisitor;

/**
 * A thunk, wrapping every node of the SK tree and allowing to freely exchange
 * the wrapped node for its evaluation result.
 * 
 * @author Leo
 * @author Joschi
 */
public class Thunk implements SKTree {

    /** the wrapped {@link Node SK node}. */
    private Node node;

    /** Number of pushes in the current evaluation. */
    public static volatile int pushes = 0;
    /** Number of reductions in the current evaluation. */
    public static volatile int reductions = 0;

    /**
     * Constructor.
     * 
     * @param nd
     *            node to be wrapped
     */
    public Thunk(final Node nd) {
        node = nd;
    }

    /**
     * Creates a wrapped application node.
     * 
     * @param f
     *            function to be applied
     * @param x
     *            value the function should be applied to
     * @return the application node wrapped in a Thunk
     */
    public static Thunk app(final Thunk f, final Thunk x) {
        return new Thunk(new App(f, x));
    }

    /**
     * Creates a wrapped number node.
     * 
     * @param i
     *            number to be wrapped
     * @return Thunk with wrapped number node
     */
    public static Thunk num(final BigInteger i) {
        return new Thunk(new Num(i));
    }

    /**
     * Creates a wrapped character node.
     * 
     * @param cp
     *            character to be wrapped
     * @return Thunk with wrapped character node
     */
    public static Thunk chr(final int cp) {
        return new Thunk(new Char(cp));
    }

    /**
     * Builds a list of Characters for the given String.
     * 
     * @param s
     *            The String.
     * @return The list thunk.
     */
    public static Thunk listFromStr(final String s) {
        if (s.length() > 3) {
            return new Thunk(new Str(s));
        }
        Thunk res = List.EMPTY.thunk;
        int i = s.length();
        while (i-- > 0) {
            final char c = s.charAt(i);
            int cp = c;
            if (Character.isLowSurrogate(c)) {
                cp = Character.toCodePoint(s.charAt(--i), c);
            }
            res = List.get(chr(cp), res);
        }
        return res;
    }

    /**
     * Reduces a node to its weak head normal form. The node in this Thunk will
     * be exchanged.
     * 
     * @return The new node.
     */
    public final Value wHNF() {
        final Stack<Thunk> stack = new Stack<Thunk>();
        Thunk curr = this;
        while (!curr.isValue()) {
            if (curr.isApp()) {
                ++pushes;
                stack.push(curr);
                curr = curr.node.getLeft();
            } else if (curr.isTuple()) {
                final Thunk app = stack.pop();
                // TODO: no implicit conversion
                // implicit BigInt -> Integer conversion with loss of precision
                final int pos = app.node.getRight().wHNF().getNum().intValue();
                final Thunk res = curr.node.getAtTuple(pos);
                curr = app;
                curr.node = Function.Def.indirect(res);
            } else {
                ++reductions;
                final Function.Def funDef = curr.node.getFunction();
                if (stack.size() < funDef.arity()) {
                    throw new IllegalStateException("Not enough arguments for "
                            + funDef);
                }
                if (funDef == Function.Def.I) {
                    curr = stack.pop().node.getRight();
                    continue;
                }

                final Thunk[] args = new Thunk[funDef.arity()];
                for (int i = 0; i < funDef.arity(); i++) {
                    curr = stack.pop();
                    args[i] = curr.node.getRight();
                }
                curr.node = funDef.apply(args);
            }
        }
        if (!stack.isEmpty()) {
            throw new IllegalStateException("Value '" + curr.node
                    + "' can't be applied.");
        }
        node = curr.node;
        return (Value) node;
    }

    /**
     * @return Whether this thunk contains a tuple.
     */
    private boolean isTuple() {
        return node.isTuple();
    }

    @Override
    public String toString() {
        return node.toString();
    }

    /**
     * Links the application tree, i.e. replaces all references with the
     * corresponding expressions.
     * 
     * @param defs
     *            definitions
     * @return a new thunk that this thunk should be replaced with, or the same
     *         thunk
     */
    public Thunk link(final Map<String, Thunk> defs) {
        return link(defs, new HashSet<String>());
    }

    /**
     * Links the application tree, i.e. replaces all references with the
     * corresponding expressions.
     * 
     * @param defs
     *            definitions
     * @param linked
     *            set of already linked definitions to avoid loops
     * @return a new thunk that this thunk should be replaced with, or the same
     *         thunk
     */
    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        final Thunk res = node.link(defs, linked);
        return res == null ? this : res;
    }

    /**
     * Checks whether the node wrapped in this thunk is equivalent to the one
     * inside the given thunk.
     * 
     * @param o
     *            node to compare to.
     * @return {@code true}, if both nodes are equal, {@code false} otherwise
     */
    public boolean eq(final Thunk o) {
        return wHNF().eq(o.wHNF());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Thunk && node.equals(((Thunk) obj).node);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    /**
     * Checks whether the node wrapped in this Thunk is an application node.
     * 
     * @return result of check
     */
    boolean isApp() {
        return node.isApp();
    }

    /**
     * Checks whether the node wrapped in this Thunk is a name reference.
     * 
     * @return result of check
     */
    public boolean isRef() {
        return node.isRef();
    }

    /**
     * Checks whether the node wrapped in this Thunk is a value.
     * 
     * @return result of check
     */
    private boolean isValue() {
        return node.isValue();
    }

    @Override
    public void traverse(final SKVisitor v) {
        node.traverse(v);
    }
}
