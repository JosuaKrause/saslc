package xi;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import xi.compiler.Expr;
import xi.compiler.Module;
import xi.compiler.Name;
import xi.linker.Linker;
import xi.sk.SKWriter;
import xi.util.IOUtils;
import xi.util.Logging;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.stringparsers.FileStringParser;
import com.martiansoftware.jsap.stringparsers.StringStringParser;

/**
 * An SK linker, reading multiple SK libraries and creating a single SK
 * expression, starting from a given start symbol.
 * 
 * @author Joschi
 */
public class Sasln {

    /**
     * Main method.
     * 
     * @param args
     *            command-line arguments
     * @throws Exception
     *             if anything goes wrong
     */
    public static void main(final String[] args) throws Exception {
        final SimpleJSAP parser = new SimpleJSAP("sasln",
                "Links the SASL expression bound to the start symbol.");

        final FlaggedOption start = new FlaggedOption("start",
                StringStringParser.getParser(), "main", false, 's', "start",
                "start symbol");
        parser.registerParameter(start);

        final FlaggedOption out = new FlaggedOption("out", FileStringParser
                .getParser(), null, false, 'o', "out",
                "output file, default is STDOUT");
        parser.registerParameter(out);

        final Switch verbose = new Switch("verbose", 'v', "verbose",
                "prints informational messages to STDERR");
        parser.registerParameter(verbose);

        final UnflaggedOption sk = new UnflaggedOption("sk", FileStringParser
                .getParser().setMustBeFile(true).setMustExist(true), null,
                false, true, "files containing an SK library");
        parser.registerParameter(sk);

        final JSAPResult res = parser.parse(args);
        if (parser.messagePrinted()) {
            System.exit(1);
        }

        if (res.getBoolean("verbose")) {
            Logging.setLevel(Level.ALL);
        }

        final File[] files = res.getFileArray("sk");
        final List<Reader> inputs = new ArrayList<Reader>();
        if (files.length == 0) {
            inputs.add(IOUtils.STDIN);
        } else {
            for (final File f : files) {
                inputs.add(IOUtils.utf8Reader(f));
            }
        }

        final Linker linker = new Linker(inputs);
        final Expr linked = linker.link(res.getString("start"));
        final Module mod = new Module(false);
        mod.addDefinition(Name.valueOf("main"), linked);

        final File outFile = res.getFile("out");
        final SKWriter skw = new SKWriter(outFile == null ? IOUtils.STDOUT
                : IOUtils.utf8Writer(outFile));
        skw.write(mod);
        skw.close();
    }
}
