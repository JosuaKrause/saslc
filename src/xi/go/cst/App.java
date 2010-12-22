package xi.go.cst;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class App extends Node {

	private Thunk left;

	private Thunk right;

	public App(final Thunk l, final Thunk r) {
		left = l;
		right = r;
	}

	@Override
	public Thunk getLeft() {
		return left;
	}

	@Override
	public Thunk getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "(" + left + " " + right + ")";
	}

	@Override
	public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
		left = left.link(defs, linked);
		right = right.link(defs, linked);
		return null;
	}

	@Override
	public boolean isApp() {
		return true;
	}

	@Override
	public void eval(final Writer w) throws IOException {
		throw new IllegalStateException("Can't print unevaluated expression '"
				+ this + "'");
	}

	@Override
	public boolean eq(final Node o) {
		throw new IllegalStateException(
				"Can't compare with unevaluated expression '" + this + "'");
	}

}
