package xi.ast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Expressions in subSASL.
 * 
 * @author Leo
 * @author Joschi
 */
public abstract class Expr extends Node {

	/** Argument array. */
	protected final Expr[] expr;

	/**
	 * Constructor taking argument expressions.
	 * 
	 * @param args
	 *            arguments
	 */
	public Expr(final Expr... args) {
		expr = args;
	}

	@Override
	public boolean hasFree(final Name var) {
		for (final Expr e : expr) {
			if (e.hasFree(var)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates a new {@link Expr expression} where all occurrences of {@code n}
	 * are replaced by S and K combinators.
	 * 
	 * @param n
	 *            name of the variable to eliminate
	 * @return
	 */
	protected abstract Expr unLambda(final Name n);

	@Override
	public final Expr unLambda() {
		return unLambda(null);
	}

	public Set<Name> freeVars() {
		final Set<Name> free = new HashSet<Name>();
		freeVars(new ArrayDeque<Name>(), free);
		return free;
	}

	protected abstract void freeVars(final Deque<Name> bound,
			final Set<Name> free);

}
