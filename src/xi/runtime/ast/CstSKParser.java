package xi.runtime.ast;

import java.math.BigInteger;
import java.util.HashMap;

import xi.runtime.ast.prim.Bool;
import xi.runtime.ast.prim.Function;
import xi.runtime.ast.prim.List;
import xi.runtime.ast.prim.Ref;
import xi.sk.SKParser;
import xi.sk.SKPrim;
import xi.util.Logging;

/**
 * An abstract SK parser producing SK trees, leaving the decision about what to
 * do with the parsed definition to the sumclass.
 * 
 * @author Leo Woerteler
 */
public abstract class CstSKParser extends SKParser<Thunk> {

    /** Sharing flag. */
    private final HashMap<Thunk, Thunk> shared;

    /**
     * Constructor taking the sharing flag.
     * 
     * @param share
     *            flag for sharing
     */
    public CstSKParser(final boolean share) {
        shared = share ? new HashMap<Thunk, Thunk>() : null;
    }

    /**
     * Sharing method.
     * 
     * @param val
     *            value to share
     * @return shared value
     */
    private Thunk get(final Thunk val) {
        if (shared == null) {
            return val;
        }
        Thunk shd = shared.get(val);
        if (shd == null) {
            shd = val;
            shared.put(val, val);
        } else {
            Logging.getLogger(getClass()).info("sharing expression: " + val);
        }
        return shd;
    }

    @Override
    public Thunk app(final Thunk f, final Thunk x) {
        return get(Thunk.app(f, x));
    }

    @Override
    public Thunk bool(final boolean b) {
        return Bool.get(b).thunk;
    }

    @Override
    public Thunk character(final int cp) {
        return get(Thunk.chr(cp));
    }

    @Override
    public Thunk cons(final Thunk hd, final Thunk tl) {
        return get(List.get(hd, tl));
    }

    @Override
    public Thunk num(final BigInteger n) {
        return get(Thunk.num(n));
    }

    @Override
    public Thunk prim(final SKPrim p) {
        final Function.Def fun = Function.forChar(p.chr);
        if (fun == null) {
            throw new IllegalArgumentException("Function '" + p
                    + "' not defined.");
        }
        return fun.thunk;
    }

    @Override
    public Thunk reference(final String name) {
        return get(Ref.get(name));
    }

    @Override
    public Thunk string(final String str) {
        return get(Thunk.listFromStr(str));
    }

    @Override
    public Thunk nil() {
        return List.EMPTY.thunk;
    }

}
