package xi;

import java.io.File;
import java.io.Reader;
import java.io.Writer;

import xi.ast.Node;
import xi.lexer.Lexer;
import xi.parser.Parser;
import xi.sk.SKWriter;
import xi.util.IOUtils;

/**
 * SASL compiler tool.
 * 
 * @author Leo Woerteler
 */
public class Saslc {

    /**
     * Compiles the SASL module given in the Reader, writing the resulting SK
     * expression to the given Writer.
     * 
     * @param in
     *            SASL reader
     * @param out
     *            Writer to write the SK code to
     * @throws Exception
     *             in case of alien invasion
     */
    private static void compile(final Reader in, final Writer out)
            throws Exception {
        final Parser p = new Parser(new Lexer(in));
        final Node n = p.parseValue().unLambda();
        final SKWriter sk = new SKWriter(out);
        sk.write(n);
        sk.close();
    }

    /**
     * Main method.
     * 
     * @param args
     *            command-line arguments
     * @throws Exception
     *             if anything goes wrong
     */
    public static void main(final String[] args) throws Exception {
        final Writer out;
        try {
            if (args.length == 1) {
                final String arg = args[0].trim();
                out = IOUtils.utf8Writer(new File(arg));
            } else {
                out = IOUtils.STDOUT;
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        compile(IOUtils.STDIN, out);
    }
}
