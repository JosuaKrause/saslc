package xi;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
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

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.ParseException;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.StringParser;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

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
        final SimpleJSAP parser = new SimpleJSAP("run", "Runs an SK file.");

        final Switch verbose = new Switch("verbose", 'v', "verbose",
                "prints informational messages to STDERR");
        parser.registerParameter(verbose);
        final UnflaggedOption run = new UnflaggedOption("run",
                new FileOrStringStringParser(), null, true, true,
                "list of SASL files and explicit SASL definitions to run");
        parser.registerParameter(run);

        final JSAPResult res = parser.parse(args);
        if (parser.messagePrinted()) {
            System.exit(1);
        }

        if (res.getBoolean("verbose")) {
            Logging.setLevel(Level.ALL);
        }

        final Object[] input = res.getObjectArray("run");

        final Map<String, Thunk> map = new HashMap<String, Thunk>();
        if (input.length == 0) {
            final SKTree skt = new Parser(new Lexer(IOUtils.STDIN))
                    .parseValue().unLambda();
            map.putAll(Eval.parse(skt));
        } else {
            for (final Object o : input) {
                map.putAll(Eval.parse((SKTree) o));
            }
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

/**
 * Parses a string, alternatively as a File and a SASL expression.
 * 
 * @author Leo Woerteler
 */
class FileOrStringStringParser extends StringParser {

    @Override
    public Object parse(final String arg) throws ParseException {
        final File f = new File(arg);
        try {
            final Reader r = f.isFile() ? IOUtils.utf8Reader(f)
                    : new StringReader(arg);
            return new Parser(new Lexer(r)).parseValue().unLambda();
        } catch (final IOException e) {
            throw new ParseException(e);
        } catch (final Exception e) {
            throw new ParseException("No valid SASL module: " + arg, e);
        }
    }
}
