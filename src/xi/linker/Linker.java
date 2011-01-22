package xi.linker;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import xi.ast.AstSKParser;
import xi.ast.Expr;
import xi.ast.LetIn;
import xi.ast.Module;
import xi.ast.Name;

/**
 * A linker for the SK output.
 * 
 * @author Leo
 */
public class Linker {

    /** Definition map. */
    final Map<String, Expr> defs = new HashMap<String, Expr>();

    /**
     * Constructor.
     * 
     * @param ins
     *            SKLib inputs
     * @throws IOException
     *             I/O exception
     */
    public Linker(final List<Reader> ins) throws IOException {
        final Map<String, Expr> module = new HashMap<String, Expr>();
        final AstSKParser parser = new AstSKParser(module);
        for (final Reader r : ins) {
            // keeps the possibility to overwrite functions in other files
            parser.read(r);
            defs.putAll(module);
            module.clear();
        }
    }

    /**
     * Creates an expression without name references by fusing all needed
     * definitions into the one associated with the name {@code startSym}.
     * 
     * @param startSym
     *            start symbol
     * @return linked expression
     */
    public Expr link(final String startSym) {
        final Map<String, Expr> required = new HashMap<String, Expr>();
        final Queue<String> queue = new ArrayDeque<String>();
        queue.add(startSym);
        while (!queue.isEmpty()) {
            final String sym = queue.poll();
            if (!required.containsKey(sym)) {
                final Expr body = defs.get(sym);
                if (body == null) {
                    throw new IllegalArgumentException("Symbol '" + sym
                            + "' not found.");
                }
                required.put(sym, body);

                for (final Name n : body.freeVars()) {
                    queue.add(n.toString());
                }
            }
        }

        final Expr start = required.remove(startSym);
        final Module mod = new Module(false);
        for (final Entry<String, Expr> e : required.entrySet()) {
            mod.addDefinition(Name.valueOf(e.getKey()), e.getValue());
        }

        final LetIn let = new LetIn(mod, start);
        return let.unLambda();
    }

}
