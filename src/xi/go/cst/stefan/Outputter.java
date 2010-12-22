package xi.go.cst.stefan;

import java.io.IOException;
import java.util.Stack;

import stefan.CommonNode;
import stefan.Draw;
import xi.go.cst.Thunk;

public class Outputter {

    private static Thunk MAIN = null;

    private static String prefix = null;

    /**
     * Generates dotty output, when main is non <code>null</code>.
     * 
     * @param main
     *            The main Thunk.
     * @param namePrefix
     *            The name prefix for the generated output.
     */
    public static void setVerboseMode(final Thunk main, final String namePrefix) {
        prefix = namePrefix;
        MAIN = main;
    }

    public static boolean isVerboseMode() {
        return MAIN != null;
    }

    private static int draws = 0;

    public static void draw(final Thunk curr, final Stack<Thunk> stack) {
        if (!isVerboseMode()) {
            return;
        }
        CSTree.clear();
        final CommonNode main = CSTree.cached(MAIN);
        final Stack<CommonNode> s = new Stack<CommonNode>();
        s.push(CSTree.cached(curr));
        // reverse and no main
        for (int i = stack.size() - 1; i >= 0; --i) {
            final Thunk t = stack.elementAt(i);
            if (t == MAIN) {
                continue;
            }
            s.push(CSTree.cached(t));
        }
        try {
            Draw.state(main, s, prefix + (draws++) + ".dot");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
