package xi.compiler;

import java.math.BigInteger;
import java.util.Map;

import xi.sk.SKParser;
import xi.sk.SKPrim;

/**
 * An SK parser that writes the parsed definition into the given map.
 * 
 * @author Leo Woerteler
 */
public class AstSKParser extends SKParser<Expr> {

    /** Definition map. */
    private final Map<String, Expr> defs;

    /**
     * Constructor.
     * 
     * @param map
     *            definition map
     */
    public AstSKParser(final Map<String, Expr> map) {
        defs = map;
    }

    @Override
    protected Expr app(final Expr f, final Expr x) {
        return App.create(f, x);
    }

    @Override
    protected Expr bool(final boolean b) {
        return Bool.valueOf(b);
    }

    @Override
    protected Expr character(final int cp) {
        return Char.valueOf(cp);
    }

    @Override
    protected Expr cons(final Expr hd, final Expr tl) {
        return BuiltIn.CONS.app(hd, tl);
    }

    @Override
    protected void def(final String name, final Expr body) {
        if (defs.put(name, body) != null) {
            throw new IllegalStateException("Duplicate definition: '" + name
                    + "'");
        }
    }

    @Override
    protected Expr nil() {
        return BuiltIn.NIL;
    }

    @Override
    protected Expr num(final BigInteger n) {
        return Num.valueOf(n);
    }

    @Override
    protected Expr prim(final SKPrim p) {
        return BuiltIn.forChar(p.chr);
    }

    @Override
    protected Expr reference(final String name) {
        return Name.valueOf(name);
    }

    @Override
    protected Expr string(final String str) {
        return Str.fromString(str);
    }

}
