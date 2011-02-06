package xi.runtime.ast;

import java.util.Map;
import java.util.Set;

import xi.sk.SKVisitor;

/**
 * A fully functional tuple.
 * 
 * @author Joschi
 * 
 */
public class Tuple extends Node {

    /** The contained thunks. */
    private final Thunk[] thunks;

    /**
     * Constructs a tuple out of a tuple builder.
     * 
     * @param builder
     *            The tuple builder.
     */
    public Tuple(final TupleBuilder builder) {
        thunks = builder.getThunks();
    }

    @Override
    public boolean isTuple() {
        return true;
    }

    @Override
    public Thunk getAtTuple(final int pos) {
        return thunks[pos];
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Tuple)) {
            return false;
        }
        final Tuple t = (Tuple) obj;
        int i = thunks.length;
        if (i != t.thunks.length) {
            return false;
        }
        while (i-- > 0) {
            if (!thunks[i].equals(t.thunks[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int mul = 1;
        int res = 0;
        for (final Thunk t : thunks) {
            res += t.hashCode() * mul;
            mul *= 31;
        }
        return res;
    }

    @Override
    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        int i = thunks.length;
        while (i-- > 0) {
            thunks[i] = thunks[i].link(defs, linked);
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        for (final Thunk t : thunks) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(t.toString());
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void traverse(final SKVisitor v) {
        boolean first = true;
        v.tuple(true);
        for (final Thunk t : thunks) {
            if (first) {
                first = false;
            } else {
                v.nextInTuple();
            }
            t.traverse(v);
        }
        v.tuple(false);
    }

}
