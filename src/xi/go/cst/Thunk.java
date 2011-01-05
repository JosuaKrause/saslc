package xi.go.cst;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import xi.go.cst.prim.Char;
import xi.go.cst.prim.Function;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Num;
import xi.go.cst.prim.Str;
import xi.go.cst.prim.Value;
import xi.go.cst.stefan.Outputter;

/**
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class Thunk {

    private Node node;

    public static int pushes = 0;
    public static int reductions = 0;

    public Thunk(final Node nd) {
        node = nd;
    }

    /**
     * Use only for CST creation.
     * 
     * @return The internal node.
     */
    public Node getNode() {
        return node;
    }

    public static Thunk app(final Thunk f, final Thunk x) {
        return new Thunk(new App(f, x));
    }

    public static Thunk num(final BigInteger i) {
        return new Thunk(new Num(i));
    }

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
            Outputter.draw(curr, stack);
            if (curr.isApp()) {
                pushes++;
                stack.push(curr);
                curr = curr.node.getLeft();
            } else {
                reductions++;
                final Function.Def funDef = curr.node.getFunction();
                if (stack.size() < funDef.cardinality) {
                    throw new IllegalStateException("Not enough arguments for "
                            + funDef);
                }
                if (funDef == Function.Def.I) {
                    curr = stack.pop().node.getRight();
                    continue;
                }

                final Thunk[] args = new Thunk[funDef.cardinality];
                for (int i = 0; i < funDef.cardinality; i++) {
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
        if (Outputter.isVerboseMode()) {
            Outputter.draw(this, stack);
        }
        return (Value) node;
    }

    boolean isApp() {
        return node.isApp();
    }

    private boolean isValue() {
        return node.isValue();
    }

    @Override
    public String toString() {
        return node.toString();
    }

    public Thunk link(final Map<String, Thunk> defs) {
        return link(defs, new HashSet<String>());
    }

    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        final Thunk res = node.link(defs, linked);
        return res == null ? this : res;
    }

    public boolean eq(final Thunk o) {
        return wHNF().eq(o.wHNF());
    }

    public boolean isRef() {
        return node.isRef();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Thunk && node.equals(((Thunk) obj).node);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }
}
