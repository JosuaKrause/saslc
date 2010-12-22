package xi.go.cst;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import xi.go.cst.prim.Function;

/**
 * 
 * 
 * @author Leo
 * @author Joschi
 */
public abstract class Node {

	public Thunk getLeft() {
		throw new UnsupportedOperationException("No Application node.");
	}

	public Thunk getRight() {
		throw new UnsupportedOperationException("No Application node.");
	}

	public int getNum() {
		throw new UnsupportedOperationException("No number.");
	}

	public int getChar() {
		throw new UnsupportedOperationException("No character.");
	}

	public boolean getBool() {
		throw new UnsupportedOperationException("No boolean.");
	}

	public Node getHead() {
		throw new UnsupportedOperationException("No list.");
	}

	public Node getTail() {
		throw new UnsupportedOperationException("No list.");
	}

	public Function.Def getFunction() {
		throw new UnsupportedOperationException("No Function.");
	}

	public boolean isApp() {
		return false;
	}

	@Override
	public abstract String toString();

	public abstract Thunk link(Map<String, Thunk> defs, Set<String> linked);

	public boolean isValue() {
		return false;
	}

	public abstract void eval(final Writer w) throws IOException;

	/**
	 * Checks whether two nodes are equal.
	 * 
	 * @param n
	 *            node to compare to.
	 * @return {@code true}, if both nodes are equal, {@code false} otherwise
	 */
	public abstract boolean eq(final Node n);

}
