package xi.go.cst;

import java.util.Map;
import java.util.Set;

/**
 * Application node.
 * 
 * @author Leo
 * @author Joschi
 */
public class App extends Node {

    /** Left child. */
    private Thunk left;

    /** Right child. */
    private Thunk right;

    /**
     * Constructor taking the left and right child.
     * 
     * @param l
     *            left child
     * @param r
     *            right child
     */
    public App(final Thunk l, final Thunk r) {
        left = l;
        right = r;
    }

    @Override
    public final Thunk getLeft() {
        return left;
    }

    @Override
    public final Thunk getRight() {
        return right;
    }

    @Override
    public final String toString() {
        return "(" + left + " " + right + ")";
    }

    @Override
    public final Thunk link(final Map<String, Thunk> defs,
            final Set<String> linked) {
        left = left.link(defs, linked);
        right = right.link(defs, linked);
        return null;
    }

    @Override
    public final boolean isApp() {
        return true;
    }

    /**
     * Creates an application loop for the Y combinator, i.e.
     * 
     * <pre>
     *   +--+
     *   &#64;  |
     *  / \_|
     * th
     * </pre>
     * 
     * @param th
     *            thunk
     * @return looping application
     */
    public static App loop(final Thunk th) {
        final App a = new App(th, null);
        a.right = new Thunk(a);
        return a;
    }

    @Override
    public boolean shareNode() {
        return true;
    }

}
