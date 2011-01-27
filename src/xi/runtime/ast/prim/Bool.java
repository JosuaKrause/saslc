package xi.runtime.ast.prim;

import xi.runtime.ast.Thunk;
import xi.sk.SKVisitor;

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
    public boolean equals(final Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.bool(getBool());
    }

}
