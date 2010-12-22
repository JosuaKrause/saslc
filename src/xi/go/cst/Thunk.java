package xi.go.cst;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import xi.go.cst.prim.Function;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Num;
import xi.go.cst.prim.Value;

/**
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class Thunk {

	private Node node;

	public Thunk(final Node nd) {
		node = nd;
	}

	public Node getNode() {
		return node;
	}

	public static Thunk app(final Thunk f, final Thunk x) {
		return new Thunk(new App(f, x));
	}

	public static Thunk num(final int i) {
		return new Thunk(new Num(i));
	}

	/**
	 * Builds a list of Characters for the given String.
	 * 
	 * @param s
	 *            The String.
	 * @return The list thunk.
	 */
	public static Thunk listFromStr(final String s) {
		Thunk res = List.EMPTY_THUNK;
		for (int i = s.length(); i-- > 0;) {
			final char c = s.charAt(i);
			int cp = c;
			if (Character.isLowSurrogate(c)) {
				cp = Character.toCodePoint(s.charAt(--i), c);
			}
			res = List.get(num(cp), res);
		}
		return res;
	}

	/**
	 * Reduces a node to its weak head normal form. The node in this Thunk will
	 * be exchanged.
	 * 
	 * @return The new node.
	 */
	public Value wHNF() {
		final BlockingNode bln = new BlockingNode();
		final Node old = node;
		final Thread wHNF = new Thread() {
			@Override
			public void run() {
				try {
					final Stack<Thunk> stack = new Stack<Thunk>();
					final Thunk start = new Thunk(old);
					Thunk curr = start;
					while (!curr.isValue()) {
						if (curr.isApp()) {
							stack.push(curr);
							curr = curr.node.getLeft();
						} else {
							final Function.Def funDef = curr.node.getFunction();
							if (stack.size() < funDef.cardinality) {
								throw new IllegalStateException(
										"Not enough arguments for " + funDef);
							}
							final Thunk[] args = new Thunk[funDef.cardinality];
							for (int i = 0; i < funDef.cardinality; i++) {
								curr = stack.pop();
								args[i] = curr.node.getRight();
							}
							curr.node = funDef.apply(args);
						}
					}
					final Node res = curr.node;
					bln.setNode(res);
					node = res;
				} catch (final Error e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		};
		node = bln;
		wHNF.start();
		return bln;
	}

	private boolean isApp() {
		return node.isApp();
	}

	private boolean isValue() {
		return node.isValue();
	}

	@Override
	public String toString() {
		return node.toString();
	}

	public void eval(final Writer w) throws IOException {
		wHNF().eval(w);
	}

	public Thunk link(final Map<String, Thunk> defs) {
		return link(defs, new HashSet<String>());
	}

	public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
		final Thunk res = node.link(defs, linked);
		return res == null ? this : res;
	}

	public boolean eq(final Thunk o) {
		return wHNF().eq(o.wHNF());
	}
}
