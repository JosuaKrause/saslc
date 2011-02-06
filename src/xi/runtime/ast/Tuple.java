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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void traverse(final SKVisitor v) {
        // TODO Auto-generated method stub

    }

}
