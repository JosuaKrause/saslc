package xi.go.cst.prim;

import xi.go.cst.Thunk;

/**
 * Boolean value.
 * 
 * @author Leo
 */
public final class Bool extends Value {

    /** Boolean instance representing {@code true}. */
    public static final Bool TRUE = new Bool();
    /** Boolean instance representing {@code false}. */
    public static final Bool FALSE = new Bool();

    /** Hidden default constructor. */
    private Bool() {
        /* void */
    }

    /** Thunk wrapping this node. */
    public final Thunk thunk = new Thunk(this);

    @Override
    public boolean getBool() {
        return this == TRUE;
    }

    /**
     * Gets the Bool object for the given boolean.
     * 
     * @param b
     *            boolean
     * @return Bool instance
     */
    public static Bool get(final boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return Boolean.toString(getBool());
    }

    @Override
    public boolean eq(final Value n) {
        return this == n;
    }

    @Override
    public boolean shareNode() {
        return false;
    }
}
