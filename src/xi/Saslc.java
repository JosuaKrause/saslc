package xi;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;

import xi.compiler.Node;
import xi.lexer.Lexer;
import xi.optimizer.OptLevel;
import xi.parser.Parser;
import xi.sk.SKWriter;
import xi.util.IOUtils;
import xi.util.Logging;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.stringparsers.FileStringParser;

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
        final SimpleJSAP parser = new SimpleJSAP("saslc",
                "Compiles a SASL module to SK code.");

        final Switch verbose = new Switch("verbose", 'v', "verbose",
                "prints informational messages to STDERR");
        parser.registerParameter(verbose);
        final FlaggedOption opt = new FlaggedOption("opt", BitFieldParser
                .getParser(), "-1", false, 'o', "opt",
                "sets the optimization level as octal number");
        parser.registerParameter(opt);
        final UnflaggedOption sasl = new UnflaggedOption("sasl",
                FileStringParser.getParser().setMustBeFile(true).setMustExist(
                        true), null, false, false, "SASL module to compile.\n"
                        + "If none is given, STDIN is read");
        parser.registerParameter(sasl);

        final JSAPResult res = parser.parse(args);
        if (parser.messagePrinted()) {
            System.exit(1);
        }

        if (res.getBoolean("verbose")) {
            Logging.setLevel(Level.ALL);
        }

        OptLevel.setLevel(res.getInt("opt"));

        final File f = res.getFile("sasl");
        final Writer out;
        try {
            if (f != null) {
                out = IOUtils.utf8Writer(f);
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
