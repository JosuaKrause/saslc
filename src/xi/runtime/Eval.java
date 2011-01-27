package xi.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import xi.runtime.ast.CstSKParser;
import xi.runtime.ast.Thunk;
import xi.sk.SKParser;
import xi.sk.SKTree;
import xi.util.Logging;

/**
 * Evaluator.
 * 
 * @author Leo
 */
public class Eval {

    /** Logger. */
    static final Logger log = Logging.getLogger(Eval.class);

    /**
     * Parses the definitions inside an SKTree into a map.
     * 
     * @param tree
     *            SKTree to read
     * @return map of definitions
     */
    public static Map<String, Thunk> parse(final SKTree tree) {

        final Map<String, Thunk> fTable = new HashMap<String, Thunk>();
        final SKParser<Thunk> parser = new CstSKParser(false) {
            @Override
            public void def(final String name, final Thunk body) {
                if (fTable.put(name, body) != null) {
                    throw new IllegalArgumentException("Duplicate definition '"
                            + name + "'.");
                }
            }
        };

        parser.read(tree);

        return fTable;
    }

    /**
     * Links the expression to execute through direct pointers.
     * 
     * @param fns
     *            functions to link
     * @param entry
     *            start symbol
     * @return linked expression
     */
    public static Thunk link(final Map<String, Thunk> fns, final String entry) {

        if (!fns.containsKey(entry)) {
            throw new IllegalArgumentException("Entry point '" + entry
                    + "' not found.");
        }
        for (final Entry<String, Thunk> e : fns.entrySet()) {
            final Thunk th = e.getValue();
            if (th.isRef()) {
                final String name = th.toString();
                if (!fns.containsKey(name)) {
                    throw new IllegalArgumentException("Function '" + name
                            + "' not found.");
                }
                if (!name.equals(e.getKey())) {
                    e.setValue(fns.get(name));
                }
            }
        }
        final Thunk main = fns.get(entry);
        System.out.println("SK code: " + main);
        return main.link(fns);
    }

}
