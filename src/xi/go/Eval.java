package xi.go;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import stefan.Cout;
import xi.ast.stefan.LazyTree;
import xi.go.cst.CstSKParser;
import xi.go.cst.Thunk;
import xi.lexer.Lexer;
import xi.parser.Parser;
import xi.parser.sk.SKParser;
import xi.util.GlueReader;
import xi.util.Logging;

/**
 * Evaluator.
 * 
 * @author Leo
 */
public class Eval {

    /** Logger. */
    static final Logger log = Logging.getLogger(Eval.class);

    public static Map<String, Thunk> parse(final Reader r) {

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

        parser.read(r);

        return fTable;
    }

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

    /**
     * Main method for the interpreter.
     * 
     * @param args
     *            currently unused
     * @throws Exception
     *             if anything goes wrong
     */
    public static void main(final String[] args) throws Exception {
        final Reader r;
        if (args.length == 0) {
            r = new InputStreamReader(System.in, "UTF-8");
        } else {
            final GlueReader g = new GlueReader();
            for (final String arg : args) {
                if (arg.equals("-v")) {
                    Logging.setLevel(Level.ALL);
                } else {
                    final File f = new File(arg);
                    if (f.exists()) {
                        g.addReader(new FileReader(f));
                    } else {
                        g.addReader(new StringReader(arg));
                    }
                }
            }
            r = g;
        }

        final Lexer l = new Lexer(r);
        final StringWriter w = new StringWriter();

        Cout.module(LazyTree.create(new Parser(l).parseValue().unLambda()), w);

        final Thunk[] main = { link(parse(new StringReader(w.toString())),
                "main") };

        try {
            VM.run(main, new OutputStreamWriter(System.out));
        } catch (final Exception e) {
            // explicit catch block for other versions compatibility
            System.out.println("Execution aborted!");
            e.printStackTrace();
        }
    }

}
