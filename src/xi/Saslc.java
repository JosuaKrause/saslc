package xi;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import xi.ast.Node;
import xi.lexer.Lexer;
import xi.parser.Parser;
import xi.sk.SKWriter;

public class Saslc {

    private static void compile(final Reader in, final Writer out)
            throws Exception {
        final Parser p = new Parser(new Lexer(in));
        final Node n = p.parseValue().unLambda();
        final SKWriter sk = new SKWriter(out);
        sk.write(n);
        sk.close();
    }

    private static void usage(final String err) {
        System.err.println("Usage: saslc [<outfile>]");
        System.err
                .println("<outfile>: An optional file to write the result to.");
        System.err
                .println("The program is read by STD_IN and by default written to STD_OUT.");
        System.exit(1);
    }

    public static void main(final String[] args) throws Exception {
        if (args.length > 1) {
            usage("too many arguments");
        }
        final Writer out;
        try {
            if (args.length == 1) {
                final String arg = args[0].trim();
                if (arg.equals("-help")) {
                    usage("");
                }
                out = new FileWriter(arg);
            } else {
                out = new OutputStreamWriter(System.out);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            usage("An Exception occured!");
            return;
        }
        compile(new InputStreamReader(System.in), out);
    }
}
