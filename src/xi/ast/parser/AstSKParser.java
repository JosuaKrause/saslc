package xi.ast.parser;

import java.math.BigInteger;
import java.util.Map;

import xi.ast.App;
import xi.ast.Bool;
import xi.ast.BuiltIn;
import xi.ast.Char;
import xi.ast.Expr;
import xi.ast.Name;
import xi.ast.Num;
import xi.ast.Str;
import xi.sk.SKParser;

public class AstSKParser extends SKParser<Expr> {

    /** Definition map. */
    private Map<String, Expr> defs;

    /**
     * Constructor.
     * 
     * @param map
     *            definition map
     */
    public AstSKParser(final Map<String, Expr> map) {
        setMap(map);
    }

    /**
     * Sets the definition map.
     * 
     * @param map
     *            definition map
     */
    public void setMap(final Map<String, Expr> map) {
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
    protected Expr prim(final char p) {
        return BuiltIn.forChar(p);
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
