package xi.optimizer;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import xi.compiler.App;
import xi.compiler.AstSKParser;
import xi.compiler.Bool;
import xi.compiler.BuiltIn;
import xi.compiler.Expr;
import xi.compiler.Module;
import xi.compiler.Name;
import xi.util.Logging;

/**
 * An optimizer for the SK output.
 * 
 * @author Leo
 * @author Joschi
 */
public class Optimizer extends AstSKParser {

    /** Logger. */
    private static final Logger LOG = Logging.getLogger(Optimizer.class);

    /**
     * Hidden default constructor.
     * 
     * @param map
     *            definition map
     */
    private Optimizer(final Map<String, Expr> map) {
        super(map);
    }

    /**
     * Optimizes the SK code provided by the given reader.
     * 
     * @param r
     *            SK code reader
     * @return AST of the optimized SK module
     */
    public static Module optimize(final Reader r) {
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
        return optApp(f, x);
    }

    /**
     * Optimizes the application of the function {@code f} to the expression
     * {@code x}.
     * 
     * @param f
     *            function
     * @param x
     *            expression
     * @return optimized application
     */
    public static Expr optApp(final Expr f, final Expr x) {
        Expr orig;
        Expr opt = App.create(f, x);
        for (;;) {
            orig = opt;
            opt = optimize(orig);
            if (orig != opt) {
                LOG.fine(orig + "  ==>  " + opt);
            } else {
                break;
            }
        }
        return opt;
    }

    /** Pattern for (B f g). */
    private static final Expr[] B2 = { BuiltIn.B, null, null };
    /** Pattern for (B f g x). */
    private static final Expr[] B3 = { BuiltIn.B, null, null, null };
    /** Pattern for (B* c f g x). */
    private static final Expr[] BS = { BuiltIn.B_STAR, null, null, null, null };
    /** Pattern for (C f g). */
    private static final Expr[] C2 = { BuiltIn.C, null, null };
    /** Pattern for (C f g x). */
    private static final Expr[] C3 = { BuiltIn.C, null, null, null };
    /** Pattern for (B* c f g x). */
    private static final Expr[] CP = { BuiltIn.C_PRIME, null, null, null, null };
    /** Pattern for (K x). */
    private static final Expr[] K1 = { BuiltIn.K, null };
    /** Pattern for (K x y). */
    private static final Expr[] K2 = { BuiltIn.K, null, null };
    /** Pattern for (S f g). */
    private static final Expr[] S2 = { BuiltIn.S, null, null };
    /** Pattern for (I x). */
    private static final Expr[] I = { BuiltIn.I, null };
    /** Pattern for (x:xs). */
    private static final Expr[] CONS = { BuiltIn.CONS, null, null };
    /** Pattern for (hd xs). */
    private static final Expr[] HD = { BuiltIn.HD, null };
    /** Pattern for (tl xs). */
    private static final Expr[] TL = { BuiltIn.TL, null };
    /** Pattern for (if a then b else c). */
    private static final Expr[] IF = { BuiltIn.BRANCH, null, null, null };
    /** Pattern for (!x). */
    private static final Expr[] NOT = { BuiltIn.NOT, null };

    /**
     * Optimizes an SK expression via pattern matching.
     * 
     * @param e
     *            SK expression
     * @return potentially optimized expression
     */
    public static Expr optimize(final Expr e) {

        // -------------------- Pattern transformations --------------------- //

        final Expr[] s = e.match(S2);
        if (s != null) {
            // e = S f g
            final Expr f = s[1], g = s[2];

            final Expr[] k = f.match(K1);
            if (k != null) {
                // e = S (K f2) g
                final Expr f2 = k[1];

                if (g.equals(BuiltIn.I)) {
                    // S (K f2) I ==> f2
                    return f2;
                }

                final Expr[] k2 = g.match(K1);
                if (k2 != null) {
                    // S (K f2) (K g) ==> K (f2 g)
                    return BuiltIn.K.app(App.create(f2, k2[1]));
                }

                final Expr[] b = g.match(B2);
                if (b != null) {
                    // S (K f) (B g h) ==> B* f g h
                    return BuiltIn.B_STAR.app(f2, b[1], b[2]);
                }

                // S (K f) g ==> B f g
                return BuiltIn.B.app(f2, g);
            }

            final Expr[] k2 = g.match(K1);
            if (k2 != null) {
                // e = S f (K g2)
                final Expr g2 = k2[1];

                final Expr[] b = f.match(B2);
                if (b != null) {
                    // S (B f g) (K h) ==> C' f g h
                    return BuiltIn.C_PRIME.app(b[1], b[2], g2);
                }

                // S f (K g) ==> C f g
                return BuiltIn.C.app(f, g2);
            }

            final Expr[] b = f.match(B2);
            if (b != null) {
                // S (B f g) h ==> S' f g h
                return BuiltIn.S_PRIME.app(b[1], b[2], g);
            }

        }

        final Expr[] c = e.match(C2);
        if (c != null) {
            // e = C f g
            final Expr f = c[1], g = c[2];

            final Expr[] b = f.match(B2);
            if (b != null) {
                // C (B b1 b2) g ==> C' b1 b2 g
                return BuiltIn.C_PRIME.app(b[1], b[2], g);
            }
        }

        final Expr[] b = e.match(B2);
        if (b != null) {
            final Expr f = b[1];
            final Expr g = b[2];
            // B f I ==> f
            if (g == BuiltIn.I) {
                return f;
            }

            final Expr[] bStar = g.match(B2);
            if (bStar != null) {
                // B f (B b1 b2) ==> B* f b1 b2
                return BuiltIn.B_STAR.app(f, bStar[1], bStar[2]);
            }
        }

        // --------------------------- Reductions --------------------------- //

        final Expr[] fb = e.match(B3);
        if (fb != null) {
            // [B f g x] ==> f [g x]
            return App.create(fb[1], optApp(fb[2], fb[3]));
        }

        final Expr[] fbs = e.match(BS);
        if (fbs != null) {
            // [B* c f g x] ==> c [f [g x]]
            return App.create(fbs[1], optApp(fbs[2], optApp(fbs[3], fbs[4])));
        }

        final Expr[] fc = e.match(C3);
        if (fc != null) {
            // [C f g x] ==> [f x] g
            return App.create(optApp(fc[1], fc[3]), fc[2]);
        }

        final Expr[] fcp = e.match(CP);
        if (fcp != null) {
            // [C' c f g x] ==> c [f x] g
            return App.create(fcp[1], optApp(fcp[2], fcp[4]), fcp[3]);
        }

        final Expr[] fk = e.match(K2);
        if (fk != null) {
            // [K x y] ==> x
            return fk[1];
        }

        final Expr[] fi = e.match(I);
        if (fi != null) {
            // [I x] ==> x
            return fi[1];
        }

        final Expr[] fhd = e.match(HD);
        if (fhd != null) {
            final Expr[] xs = fhd[1].match(CONS);
            if (xs != null) {
                // [hd (x:xs)] ==> x
                return xs[1];
            }
        }

        final Expr[] ftl = e.match(TL);
        if (ftl != null) {
            final Expr[] xs = ftl[1].match(CONS);
            if (xs != null) {
                // [tl (x:xs)] ==> xs
                return xs[2];
            }
        }

        final Expr[] fif = e.match(IF);
        if (fif != null && fif[1] instanceof Bool) {
            // [if true then x else y] ==> x
            // [if false then x else y] ==> y
            return fif[1] == Bool.TRUE ? fif[2] : fif[3];
        }

        final Expr[] fn = e.match(NOT);
        if (fn != null && fn[1] instanceof Bool) {
            // [!false] ==> true
            // [!true] ==> false
            return Bool.valueOf(!((Bool) fn[1]).getValue());
        }

        return e;
    }
}
