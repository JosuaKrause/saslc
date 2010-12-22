package xi.go.cst.prim;

import xi.go.cst.Thunk;

/**
 * String node.
 * 
 * @author Leo
 * @author Joschi
 */
public class Str extends Value {

    /** String value. */
    private final String string;

    /**
     * Constructor.
     * 
     * @param str
     *            string value
     */
    public Str(final String str) {
        string = str;
    }

    @Override
    public final Thunk getHead() {
        return Thunk.chr(string.codePointAt(0));
    }

    @Override
    public final Thunk getTail() {
        return Thunk.listFromStr(string.substring(Character.charCount(string
                .codePointAt(0))));
    }

    @Override
    public final boolean eq(final Value n) {
        if (n == this) {
            return true;
        }
        if (n == List.EMPTY) {
            return false;
        }
        return getHead().eq(n.getHead()) && getTail().eq(n.getTail());
    }

    @Override
    public final String toString() {
        return '"' + string + '"';
    }

    @Override
    public final boolean isChar() {
        return true;
    }

}
