package xi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;

import xi.optimizer.Optimizer;
import xi.sk.SKWriter;
import xi.util.Logging;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.stringparsers.FileStringParser;

public class Saslo {

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
        final Reader r = new InputStreamReader(inFile == null ? System.in
                : new FileInputStream(inFile), "UTF-8");
        final Writer w = new OutputStreamWriter(outFile == null ? System.out
                : new FileOutputStream(outFile), "UTF-8");

        final SKWriter skw = new SKWriter(w);
        skw.write(Optimizer.optimize(r));
        w.close();
    }
}
