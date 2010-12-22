package xi.ast;

import java.util.Deque;
import java.util.Properties;
import java.util.Set;

/**
 * SASL let-in expression.
 * 
 * @author Leo
 */
public class LetIn extends Expr {

	/** Name bindings. */
	private final Module defs;

	/**
	 * Constructor.
	 * 
	 * @param let
	 *            name bindings
	 * @param in
	 *            expression
	 */
	public LetIn(final Module let, final Expr in) {
		super(in);
		defs = let;
	}

	@Override
	public String toString() {
		return "let " + defs + " in " + expr[0];
	}

	/** Properties for the let-edges. */
	private static final Properties IN = new Properties();
	static {
		IN.put("label", "in");
	}

	@Override
	public boolean hasFree(final Name var) {
		return defs.hasFree(var) || expr[0].hasFree(var);
	}

	public Expr getBody() {
		return expr[0];
	}

	public Module getModule() {
		return defs;
	}

	@Override
	protected Expr unLambda(final Name n) {
		final Module mod = defs.unLambda();
		// topoSort(mod.getMap());
		// TODO unLambda for let bindings
		return new LetIn(mod, getBody().unLambda(n));
	}

	@Override
	protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
		int n = defs.getMap().size();
		for (final Name name : defs.getMap().keySet()) {
			bound.push(name);
		}
		getBody().freeVars(bound, free);
		while (n-- > 0) {
			bound.pop();
		}
	}
}
