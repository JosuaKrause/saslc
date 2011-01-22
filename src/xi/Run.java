package xi;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import xi.go.Eval;
import xi.go.VM;
import xi.go.cst.Thunk;
import xi.lexer.Lexer;
import xi.parser.Parser;
import xi.sk.SKTree;
import xi.util.IOUtils;
import xi.util.Logging;

/**
 * Runs a given SASL file.
 * 
 * @author Joschi
 */
public class Run {

    /**
     * Main method for the interpreter.
     * 
     * @param args
     *            currently unused
     * @throws Exception
     *             if anything goes wrong
     */
    public static void main(final String[] args) throws Exception {
        final List<Reader> r = new ArrayList<Reader>();
        if (args.length == 0) {
            r.add(IOUtils.STDIN);
        } else {
            for (final String arg : args) {
                if (arg.equals("-v")) {
                    Logging.setLevel(Level.ALL);
                } else {
                    final File f = new File(arg);
                    if (f.exists()) {
                        r.add(IOUtils.utf8Reader(f));
                    } else {
                        r.add(new StringReader(arg));
                    }
                }
            }
        }

        final Map<String, Thunk> map = new HashMap<String, Thunk>();
        for (final Reader in : r) {
            final SKTree skt = new Parser(new Lexer(in)).parseValue()
                    .unLambda();
            map.putAll(Eval.parse(skt));
        }
        final Thunk[] main = { Eval.link(map, "main") };

        try {
            VM.run(main, IOUtils.STDOUT);
        } catch (final Exception e) {
            // explicit catch block for other versions compatibility
            System.out.println("Execution aborted!");
            e.printStackTrace();
        }
    }

}
