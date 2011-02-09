package xi;

import java.io.File;
import java.util.logging.Level;

import xi.linker.Make;
import xi.optimizer.OptLevel;
import xi.util.BitFieldParser;
import xi.util.Logging;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.stringparsers.FileStringParser;
import com.martiansoftware.jsap.stringparsers.IntSizeStringParser;

/**
 * The sasl_make command-line tool, making automatic builds possible.
 * 
 * @author Joschi
 */
public class SaslMake {

    /**
     * Main method.
     * 
     * @param args
     *            command-line arguments
     * @throws Exception
     *             in case of tornadoes
     */
    public static void main(final String[] args) throws Exception {
        final SimpleJSAP parser = new SimpleJSAP("sasl_make",
                "Interprets a SASL make file (*.smake), compiling, linking"
                        + " and optionally executing a SASL program.");

        final FlaggedOption jobs = new FlaggedOption("jobs",
                IntSizeStringParser.getParser(), "1", false, 'j', "jobs",
                "number of threads used for compilation");
        parser.registerParameter(jobs);
        parser.registerParameter(new Switch("run", 'r', "run",
                "runs the compiled SASL program"));
        parser.registerParameter(new Switch("force", 'f', "force",
                "forces a complete rebuild"));
        final FlaggedOption opt = new FlaggedOption("opt", BitFieldParser
                .getParser(), "-1", false, 'o', "opt",
                "sets the optimization level as octal number");
        parser.registerParameter(opt);
        parser.registerParameter(new Switch("shared", 'c', "cse",
                "performs common subexpression elimination"));
        final Switch verbose = new Switch("verbose", 'v', "verbose",
                "prints informational messages to STDERR");
        parser.registerParameter(verbose);
        final UnflaggedOption makefile = new UnflaggedOption(
                "makefile",
                FileStringParser.getParser().setMustBeFile(true).setMustExist(
                        true),
                null,
                true,
                false,
                "the makefile to "
                        + "interpret\nThe syntax of a makefile is fairly easy.\n"
                        + "Every line is a SASL file ("
                        + Make.SASL
                        + ") or a directory containing them (will be scanned "
                        + "recursively) to compile. The files are linked in the "
                        + "order given by the makefile. A line starting with '"
                        + Make.START
                        + "' defines the starting symbol. When no such line is "
                        + "given, the default main is used. Everything after a "
                        + Make.COMMENT
                        + " is interpreted as a comment. Old "
                        + Make.SK_LIB
                        + " files and the <makefile>"
                        + Make.SK
                        + " will be overwritten. Informations about the "
                        + "progress will be printed to STD_ERR. The results of "
                        + "the program when started with -r will be printed to STDOUT.");
        parser.registerParameter(makefile);

        final JSAPResult res = parser.parse(args);
        if (parser.messagePrinted()) {
            System.exit(1);
        }

        if (res.getBoolean("verbose")) {
            Logging.setLevel(Level.ALL);
        }

        OptLevel.setLevel(res.getInt("opt"));

        final int j = Math.max(1, res.getInt("jobs"));
        final boolean run = res.getBoolean("run");
        final boolean force = res.getBoolean("force");
        final boolean shared = res.getBoolean("shared");
        final File makeFile = res.getFile("makefile");
        final Make m = new Make(shared, force, j);
        try {
            m.make(makeFile, run);
        } catch (final Exception e) {
            System.err.println();
            e.printStackTrace();
        }
    }

}
