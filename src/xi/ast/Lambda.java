package xi.ast;

import java.util.Deque;
import java.util.Set;

/**
 * Lambda expression.
 * 
 * @author Leo
 */
public class Lambda extends Expr {

	/** Variable name. */
	private final Name name;

	/**
	 * Constructor.
	 * 
	 * @param n
	 *            variable name
	 * @param expr
	 *            expression
	 */
	public Lambda(final String n, final Expr expr) {
		super(expr);
		name = Name.valueOf(n);
	}

	@Override
	public String toString() {
		return "{ " + name + " -> " + expr[0] + " }";
	}

	@Override
	public boolean hasFree(final Name var) {
		return !name.equals(var) && expr[0].hasFree(var);
	}

	public Expr getBody() {
		return expr[0];
	}

	public String getName() {
		return name.getName();
	}

	@Override
	protected Expr unLambda(final Name n) {
		return expr[0].unLambda(n).unLambda(name);
	}

	@Override
	protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
		bound.push(name);
		getBody().freeVars(bound, free);
		bound.pop();
	}

}
