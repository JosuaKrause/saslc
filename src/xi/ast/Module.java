package xi.ast;

import static xi.ast.BuiltIn.Y;
import static xi.util.StringUtils.NL;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import xi.sk.SKVisitor;

/**
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class Module extends Node {
    /** Module definitions. */
    private final Map<Name, Expr> defs;
    /** Flag for top level definitions. */
    private final boolean isTopLevel;

    /**
     * Constructor.
     * 
     * @param top
     *            top level flag
     */
    public Module(final boolean top) {
        this(top, false);
        if (top) {
            addDefinition(Name.valueOf("hd"), BuiltIn.HD);
            addDefinition(Name.valueOf("tl"), BuiltIn.TL);
            addDefinition(Name.valueOf("char"), BuiltIn.CHAR);
        }
    }

    /**
     * Constructor.
     * 
     * @param top
     *            top level flag
     * @param copy
     *            copy flag
     */
    private Module(final boolean top, final boolean copy) {
        isTopLevel = top;
        defs = new TreeMap<Name, Expr>();
    }

    /**
     * Adds a function definition to this module.
     * 
     * @param n
     *            name
     * @param v
     *            definition
     */
    public final void addDefinition(final Name n, final Expr v) {
        if (defs.containsKey(n)) {
            throw new IllegalArgumentException("duplicate name: " + n);
        }
        defs.put(n, v);
    }

    /**
     * Retrieves the definition corresponding to the given function name.
     * 
     * @param name
     *            function name
     * @return definition expression
     */
    public final Expr getForName(final Name name) {
        return defs.get(name);
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        boolean removeEnd = false;
        for (final Entry<Name, Expr> def : defs.entrySet()) {
            sb.append(def.getKey()).append(" = ").append(def.getValue());
            if (isTopLevel) {
                sb.append(';').append(NL);
            } else {
                sb.append(", ");
                removeEnd = true;
            }
        }
        if (removeEnd) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    @Override
    public final boolean hasFree(final Name var) {
        if (defs.containsKey(var)) {
            return false;
        }
        boolean inDefs = false;
        for (final Entry<Name, Expr> def : defs.entrySet()) {
            inDefs |= def.getValue().hasFree(var);
        }
        return inDefs;
    }

    /**
     * Getter for the definition map.
     * 
     * @return definition map
     */
    public final Map<Name, Expr> getMap() {
        return defs;
    }

    @Override
    public final Module unLambda() {
        final Module res = new Module(isTopLevel, true);
        for (final Entry<Name, Expr> e : defs.entrySet()) {
            final Name name = e.getKey();
            Expr nw = e.getValue();

            // eliminate simple recursion
            if (nw.hasFree(name)) {
                nw = Y.app(new Lambda(name.toString(), nw));
            }
            res.addDefinition(name, nw.unLambda());
        }
        return res;
    }

    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof Module)) {
            return false;
        }
        final Module o = (Module) other;
        if (defs.size() == o.defs.size()) {
            for (final Entry<Name, Expr> e : defs.entrySet()) {
                if (!o.defs.containsKey(e.getKey())
                        || !e.getValue().equals(o.defs.get(e.getKey()))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public final int hashCode() {
        int hash = 0;
        // order of definitions irrelevant
        for (final Entry<Name, Expr> e : defs.entrySet()) {
            hash ^= 31 * e.getKey().hashCode() + e.getValue().hashCode();
        }
        return hash;
    }

    @Override
    public final void traverse(final SKVisitor v) {
        for (final Entry<Name, Expr> e : defs.entrySet()) {
            e.getValue().traverse(v);
            v.def(e.getKey().toString());
        }
    }
}
