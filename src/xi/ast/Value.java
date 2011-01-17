package xi.ast;

import java.util.Deque;
import java.util.Set;

/**
 * SASL literal values.
 * 
 * @author Leo
 */
public abstract class Value extends Expr {

    @Override
    public boolean hasFree(final Name var) {
        return false;
    }

    @Override
    protected final Expr unLambda(final Name n) {
        return n == null ? this : equals(n) ? BuiltIn.I : BuiltIn.K.app(this);
    }

    @Override
    protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
        /* void */
    }

    @Override
    public int numOfUses(final Name n) {
        return 0;
    }

    @Override
    public Expr inline(final Name name, final Expr val) {
        return this;
    }

    @Override
    public boolean match(final Expr[] pat, final int l, final int r) {
        if (r - l != 1) {
            return false;
        }
        if (pat[l] != null) {
            return pat[l].equals(this);
        }
        pat[l] = this;
        return true;
    }
}
