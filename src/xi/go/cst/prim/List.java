package xi.go.cst.prim;

import java.util.Map;
import java.util.Set;

import xi.go.cst.Thunk;

/**
 * List node.
 * 
 * @author Leo
 * @author Joschi
 */
public class List extends Value {

    /** Instance representing the empty list. */
    public static final List EMPTY = new List(null, null) {

        @Override
        public Thunk getHead() {
            throw new IllegalStateException("Empty list has no head.");
        }

        @Override
        public Thunk getTail() {
            throw new IllegalStateException("Empty list has no tail.");
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        public Thunk link(final Map<String, Thunk> defs, final Set<String> l) {
            return null;
        }

        @Override
        public boolean eq(final Value n) {
            return this == n;
        }

        @Override
        public boolean shareNode() {
            return false;
        }
    };

    /** Head of the list. */
    private Thunk head;

    /** Tail of the list. */
    private Thunk tail;

    /** Thunk of this list. */
    public final Thunk thunk;

    /**
     * Constructor.
     * 
     * @param hd
     *            head
     * @param tl
     *            tail
     */
    public List(final Thunk hd, final Thunk tl) {
        head = hd;
        tail = tl;
        thunk = new Thunk(this);
    }

    @Override
    public Thunk getHead() {
        return head;
    }

    @Override
    public Thunk getTail() {
        return tail;
    }

    /**
     * Static list node construction.
     * 
     * @param hd
     *            head
     * @param tl
     *            tail
     * @return list node
     */
    public static Thunk get(final Thunk hd, final Thunk tl) {
        return new List(hd, tl).thunk;
    }

    @Override
    public String toString() {
        return head + " : " + tail;
    }

    @Override
    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        head = head.link(defs, linked);
        tail = tail.link(defs, linked);
        return null;
    }

    @Override
    public boolean eq(final Value n) {
        if (n == this) {
            return true;
        }
        if (n == EMPTY) {
            return false;
        }
        return getHead().eq(n.getHead()) && getTail().eq(n.getTail());
    }
    
    @Override
    public boolean isList() {
        return true;
    }
}
