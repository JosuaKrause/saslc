package xi.ast;

import xi.sk.SKVisitor;

/**
 * Very lazy String implementation. The list is created when needed or when the
 * length is too short. Once a lazy String is split up (via hd and tl) it is
 * unrecoverable and the internal String will eventually be garbage collected.
 * 
 * @author Joschi
 * 
 */
public final class Str extends Value {

    /**
     * The minimum length where the String is not unfolded to a list.
     */
    private static final int MIN_LENGTH = 5;

    /**
     * Returns a node representing the String i.e. the list of Characters. Very
     * short Strings are automatically unfolded to lists.
     * 
     * @param s
     *            The String.
     * @return The node.
     */
    public static Expr fromString(final String s) {
        if (s.length() < MIN_LENGTH) {
            return Char.listFromStr(s);
        }
        return new Str(s);
    }

    /**
     * The internal String.
     */
    private final String str;

    /**
     * Creates a String-Node out of a String with no list unfolding.
     * 
     * @param s
     *            The String.
     */
    private Str(final String s) {
        str = s;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Str && str.equals(((Str) other).str);
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.str(str);
    }

}
