package xi;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;

import xi.optimizer.Optimizer;
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
 * A pattern-matching optimizer for SK code.
 * 
 * @author Leo Woerteler
 */
public class Saslo {

    /**
     * Main method.
     * 
     * @param args
     *            command-line arguments
     * @throws Exception
     *             in case of fire
     */
    public static void main(final String[] args) throws Exception {

        final SimpleJSAP parser = new SimpleJSAP("saslo");
        // verbose flag
        parser.registerParameter(new Switch("verbose", 'v', "verbose",
                "if set, info messages are printed to STDERR"));
        // output filename
        final FlaggedOption out = new FlaggedOption("out", FileStringParser
                .getParser(), null, false, 'o', "out",
                "output file, default is STDOUT");
        parser.registerParameter(out);
        // input file
        final FileStringParser fsp = FileStringParser.getParser().setMustExist(
                true).setMustBeFile(true);
        final UnflaggedOption in = new UnflaggedOption("in", fsp, null, false,
                false, "input file, default is STDIN");
        parser.registerParameter(in);

        final JSAPResult opts = parser.parse(args);
        if (parser.messagePrinted()) {
            return;
        }

        // set verbose
        if (opts.getBoolean("verbose")) {
            Logging.setLevel(Level.ALL);
        }

        final File inFile = opts.getFile("in"), outFile = opts.getFile("out");
        final Reader r = inFile == null ? IOUtils.STDIN : IOUtils
                .utf8Reader(inFile);
        final Writer w = outFile == null ? IOUtils.STDOUT : IOUtils
                .utf8Writer(outFile);

        final SKWriter skw = new SKWriter(w);
        skw.write(Optimizer.optimize(r));
        w.close();
    }
}
