package xi.optimizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import stefan.Cout;
import xi.ast.App;
import xi.ast.BuiltIn;
import xi.ast.Expr;
import xi.ast.Module;
import xi.ast.Name;
import xi.ast.parser.AstSKParser;
import xi.ast.stefan.LazyTree;
import xi.util.Logging;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.stringparsers.FileStringParser;

/**
 * A linker for the SK output.
 * 
 * @author Leo
 */
public class Optimizer extends AstSKParser {

    /**
     * Hidden default constructor.
     * 
     * @param map
     *            definition map
     */
    private Optimizer(final Map<String, Expr> map) {
        super(map);
    }

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

        Cout.module(LazyTree.create(optimize(r)), w);
    }

    /**
     * Optimizes the SK code provided by the given reader.
     * 
     * @param r
     *            SK code reader
     * @return AST of the optimized SK module
     */
    private static Module optimize(final Reader r) {
        final Map<String, Expr> map = new HashMap<String, Expr>();
        final Optimizer opt = new Optimizer(map);
        opt.read(r);

        final Module mod = new Module(false);
        for (final Entry<String, Expr> e : map.entrySet()) {
            mod.addDefinition(Name.valueOf(e.getKey()), e.getValue());
        }
        return mod;
    }

    @Override
    protected Expr app(final Expr f, final Expr x) {
        final Expr orig = App.create(f, x), opt = optimize(orig);
        if (orig != opt) {
            System.out.println(orig + "  ==>  " + opt);
        }
        return opt;
    }

    private static final Expr[] B_PAT = { BuiltIn.B, null, null };
    private static final Expr[] C_PAT = { BuiltIn.C, null, null };
    private static final Expr[] CI_PAT = { BuiltIn.C, BuiltIn.I, null, null };
    private static final Expr[] K_PAT = { BuiltIn.K, null };
    private static final Expr[] S_PAT = { BuiltIn.S, null, null };

    public static Expr optimize(final Expr e) {

        final Expr[] s = e.match(S_PAT);
        if (s != null) {
            // e = S f g
            final Expr f = s[1], g = s[2];

            final Expr[] k = f.match(K_PAT);
            if (k != null) {
                // e = S (K f2) g
                final Expr f2 = k[1];

                if (g.equals(BuiltIn.I)) {
                    // S (K f2) I ==> f2
                    return f2;
                }

                final Expr[] k2 = g.match(K_PAT);
                if (k2 != null) {
                    // S (K f2) (K g) ==> K (f2 g)
                    return BuiltIn.K.app(App.create(f2, k2[1]));
                }

                final Expr[] b = g.match(B_PAT);
                if (b != null) {
                    // S (K f) (B g h) ==> B* f g h
                    return BuiltIn.B_STAR.app(f2, b[1], b[2]);
                }

                // S (K f) g ==> B f g
                return BuiltIn.B.app(f2, g);
            }

            final Expr[] k2 = g.match(K_PAT);
            if (k2 != null) {
                // e = S f (K g2)
                final Expr g2 = k2[1];

                final Expr[] b = f.match(B_PAT);
                if (b != null) {
                    // S (B f g) (K h) ==> C' f g h
                    return BuiltIn.C_PRIME.app(b[1], b[2], g2);
                }

                // S f (K g) ==> C f g
                return BuiltIn.C.app(f, g2);
            }

            final Expr[] b = f.match(B_PAT);
            if (b != null) {
                // S (B f g) h ==> S' f g h
                return BuiltIn.S_PRIME.app(b[1], b[2], g);
            }

        }

        final Expr[] ci = e.match(CI_PAT);
        if (ci != null) {
            // C I x f ==> f x
            return App.create(ci[3], ci[2]);
        }

        final Expr[] c = e.match(C_PAT);
        if (c != null) {
            // e = C f g
            final Expr f = c[1], g = c[2];

            final Expr[] b = f.match(B_PAT);
            if (b != null) {
                // C (B b1 b2) g ==> C' b1 b2 g
                return BuiltIn.C_PRIME.app(b[1], b[2], g);
            }
        }

        return e;
    }
}
