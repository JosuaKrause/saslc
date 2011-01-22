package xi;

import java.util.logging.Level;

import xi.go.VM;
import xi.go.cst.CstSKParser;
import xi.go.cst.Thunk;
import xi.util.IOUtils;
import xi.util.Logging;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.stringparsers.FileStringParser;

/**
 * An SK interpreter, reading and executing a single fully linked SK expression.
 * 
 * @author Leo Woerteler
 */
public class SK {

    /**
     * Test method.
     * 
     * @param args
     *            command line arguments
     * @throws Exception
     *             in case of fire
     */
    public static void main(final String[] args) throws Exception {

        final SimpleJSAP parser = new SimpleJSAP("sk",
                "Executes a single fully linked SK expression.");

        parser.registerParameter(new Switch("shared", 'c', "cse",
                "performs common subexpression elimination"));
        final Switch verbose = new Switch("verbose", 'v', "verbose",
                "prints informational messages to STDERR");
        parser.registerParameter(verbose);
        final UnflaggedOption sk = new UnflaggedOption("sk", FileStringParser
                .getParser().setMustBeFile(true).setMustExist(true), null,
                true, false, "SK file to be executed");
        parser.registerParameter(sk);

        final JSAPResult res = parser.parse(args);
        if (parser.messagePrinted()) {
            System.exit(1);
        }

        if (res.getBoolean("verbose")) {
            Logging.setLevel(Level.ALL);
        }

        final Thunk[] main = { null };

        new CstSKParser(res.getBoolean("shared")) {
            @Override
            protected void def(final String name, final Thunk body) {
                if (main[0] != null || !name.equals("main")) {
                    throw new IllegalArgumentException("A linked SK file "
                            + "should only contain one 'main' function.");
                }
                main[0] = body;
            }
        }.read(IOUtils.utf8Reader(res.getFile("sk")));

        if (main[0] == null) {
            throw new IllegalArgumentException("No main method.");
        }

        System.out.println("SK code: " + main[0]);
        VM.run(main, IOUtils.STDOUT);
    }
}
