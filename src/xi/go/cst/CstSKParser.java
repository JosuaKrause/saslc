package xi.go.cst;

import java.math.BigInteger;
import java.util.HashMap;

import xi.go.cst.prim.Bool;
import xi.go.cst.prim.Function;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Ref;
import xi.sk.SKParser;
import xi.util.Logging;

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
            if (!val.toString().equals(shd.toString())) {
                System.out.println(val.equals(shd));
            }
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
    public Thunk prim(final char p) {
        final Function.Def fun = Function.forChar(p);
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
