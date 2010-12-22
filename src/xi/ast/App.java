package xi.ast;

import static xi.ast.BuiltIn.K;
import static xi.ast.BuiltIn.S;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SASL function application.
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class App extends Expr {

	/**
	 * Pair-class holding two expressions. Is needed for application-sharing.
	 * Two pairs are equal iff the corresponding expressions are equal.
	 * 
	 * @author Joschi
	 * 
	 */
	private static class Expr2 {
		/**
		 * The left node.
		 */
		Expr f;

		/**
		 * The right node.
		 */
		Expr x;

		/**
		 * Constructs a Expression-Pair.
		 * 
		 * @param f
		 *            The left node.
		 * @param x
		 *            The right node.
		 */
		public Expr2(final Expr f, final Expr x) {
			this.f = f;
			this.x = x;
		}

		@Override
		public int hashCode() {
			return f.hashCode() ^ x.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Expr2)) {
				return false;
			}
			final Expr2 that = (Expr2) obj;
			return f.equals(that.f) && x.equals(that.x);
		}
	}

	/**
	 * A map holding the cached values for application-sharing.
	 */
	private static Map<Expr2, App> CACHE = new HashMap<Expr2, App>();

	/**
	 * Creates an application-node for the given expressions. If it is possible
	 * already created applications are reused.
	 * 
	 * @param f
	 *            The left node.
	 * @param x
	 *            The right node.
	 * @return The application-node.
	 */
	public static App create(final Expr f, final Expr x) {
		final Expr2 e = new Expr2(f, x);
		if (!CACHE.containsKey(e)) {
			CACHE.put(e, new App(f, x));
		}
		return CACHE.get(e);
	}

	/**
	 * Constructor taking a function and an argument.
	 * 
	 * @param f
	 *            function expression
	 * @param x
	 *            argument expression
	 */
	private App(final Expr f, final Expr x) {
		super(f, x);
	}

	@Override
	public String toString() {
		return "(" + expr[0] + " " + expr[1] + ')';
	}

	public Expr getLeft() {
		return expr[0];
	}

	public Expr getRight() {
		return expr[1];
	}

	@Override
	protected Expr unLambda(final Name n) {
		final Expr f = getLeft(), x = getRight();
		if (n == null) {
			return App.create(f.unLambda(), x.unLambda());
		}
		final boolean nf = !f.hasFree(n), nx = !x.hasFree(n);

		// (\x . a b) => K (a b), if x is free in neither a nor b
		if (nf && nx) {
			return K.app(this);
		}

		// (\x . f x) => f, if x isn't free in f
		if (nf && !nx && x.equals(n)) {
			return nf ? f : f.unLambda(n);
		}

		return S.app(nf ? K.app(f) : f.unLambda(n), nx ? K.app(x) : x
				.unLambda(n));
	}

	@Override
	protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
		getLeft().freeVars(bound, free);
		getRight().freeVars(bound, free);
	}
}
