package xi.go.cst.prim;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import xi.go.cst.Thunk;

/**
 * Name reference.
 * 
 * @author Leo
 */
public final class Ref extends Value {

    /** Referenced function name. */
    private final String fun;

    /**
     * Constructor.
     * 
     * @param n
     *            referenced name
     */
    private Ref(final String n) {
        fun = n;
    }

    /** Caching map. */
    private static final HashMap<String, Thunk> MAP = new HashMap<String, Thunk>();

    /**
     * Caching getter for reference nodes.
     * 
     * @param name
     *            name to be referenced
     * @return reference node
     */
    public static Thunk get(final String name) {
        if (!MAP.containsKey(name)) {
            MAP.put(name, new Thunk(new Ref(name)));
        }
        return MAP.get(name);
    }

    @Override
    public String toString() {
        return fun;
    }

    @Override
    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        final Thunk funDef = defs.get(fun);
        if (funDef == null) {
            throw new IllegalStateException("Name '" + fun + "' not defined.");
        }
        if (linked.add(fun)) {
            funDef.link(defs, linked);
        }
        return funDef;
    }

    @Override
    public boolean eq(final Value n) {
        return n instanceof Ref && ((Ref) n).fun.equals(fun);
    }

    @Override
    public boolean isRef() {
        return true;
    }

}
