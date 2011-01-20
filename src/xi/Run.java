package xi;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;

import xi.go.Eval;
import xi.go.VM;
import xi.go.cst.Thunk;
import xi.lexer.Lexer;
import xi.parser.Parser;
import xi.sk.SKTree;
import xi.util.GlueReader;
import xi.util.Logging;

/**
 * 
 * @author Joschi
 * 
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

        final SKTree skt = new Parser(new Lexer(r)).parseValue().unLambda();
        final Thunk[] main = { Eval.link(Eval.parse(skt), "main") };

        try {
            VM.run(main, new OutputStreamWriter(System.out));
        } catch (final Exception e) {
            // explicit catch block for other versions compatibility
            System.out.println("Execution aborted!");
            e.printStackTrace();
        }
    }

}
