package xi.runtime.ast.prim;

import xi.runtime.ast.Thunk;
import xi.sk.SKVisitor;

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
    public boolean isList() {
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Str && string.equals(((Str) obj).string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.str(string);
    }
}
