package xi.go.cst;

import java.math.BigInteger;

import xi.go.cst.prim.Bool;
import xi.go.cst.prim.Function;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Ref;
import xi.parser.sk.SKParser;

public abstract class CstSKParser extends SKParser<Thunk> {

    @Override
    public Thunk app(final Thunk f, final Thunk x) {
        return Thunk.app(f, x);
    }

    @Override
    public Thunk bool(final boolean b) {
        return Bool.get(b).thunk;
    }

    @Override
    public Thunk character(final int cp) {
        return Thunk.chr(cp);
    }

    @Override
    public Thunk cons(final Thunk hd, final Thunk tl) {
        return List.get(hd, tl);
    }

    @Override
    public Thunk num(final BigInteger n) {
        return Thunk.num(n);
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
        return Ref.get(name);
    }

    @Override
    public Thunk string(final String str) {
        return Thunk.listFromStr(str);
    }

    @Override
    public Thunk nil() {
        return List.EMPTY.thunk;
    }

}
