package xi.runtime.ast;

import java.util.LinkedList;

/**
 * Builds a fully functional tuple.
 * 
 * @author Joschi
 * 
 */
public class TupleBuilder {

    /** The list containing the Thunks. */
    private final LinkedList<Thunk> list;

    /**
     * Creates an empty tuple builder.
     */
    public TupleBuilder() {
        list = new LinkedList<Thunk>();
    }

    /**
     * Prepends the thunk to the list.
     * 
     * @param e
     *            The first thunk.
     * @return This TupleBuilder.
     */
    public TupleBuilder addFirst(final Thunk e) {
        list.addFirst(e);
        return this;
    }

    /**
     * Appends the thunk to the list.
     * 
     * @param e
     *            The last thunk.
     * @return This TupleBuilder.
     */
    public TupleBuilder addLast(final Thunk e) {
        list.addLast(e);
        return this;
    }

    /**
     * Returns the thunk as a Array.
     * 
     * @return All thunk combined as an Array.
     */
    public Thunk[] getThunks() {
        return list.toArray(new Thunk[list.size()]);
    }

}
