package xi.compiler;

import xi.sk.SKVisitor;

/**
 * Boolean literal.
 * 
 * @author Leo
 * @author Joschi
 */
public final class Bool extends Value {

    /**
     * The value for <code>true</code>.
     */
    private static final Bool TRUE = new Bool();

    /**
     * The value for <code>false</code>.
     */
    private static final Bool FALSE = new Bool();

    /**
     * @param b
     *            The boolean value.
     * @return The Value representing <code>b</code>.
     */
    public static Bool valueOf(final boolean b) {
        return b ? TRUE : FALSE;
    }

    /**
     * We do not memorize any boolean values. The value is given by the object's
     * identity.
     */
    private Bool() {
        // nothing to construct here
    }

    @Override
    public String toString() {
        return Boolean.toString(getValue());
    }

    /**
     * @return The boolean value of the object.
     */
    public boolean getValue() {
        return this == TRUE;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.bool(getValue());
    }

}
