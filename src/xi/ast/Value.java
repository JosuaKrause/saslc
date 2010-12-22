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

}
