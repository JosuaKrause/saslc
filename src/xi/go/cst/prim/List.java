package xi.go.cst.prim;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import xi.go.cst.Node;
import xi.go.cst.Thunk;

/**
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class List extends Value {

	public static final List EMPTY = new List(null, null) {

		@Override
		public Node getHead() {
			throw new IllegalStateException("Empty list has no head.");
		}

		@Override
		public Node getTail() {
			throw new IllegalStateException("Empty list has no tail.");
		}

		@Override
		public String toString() {
			return "[]";
		}

		@Override
		public Thunk link(final Map<String, Thunk> defs, final Set<String> l) {
			return null;
		}

		@Override
		public boolean eq(final Node n) {
			return this == n;
		}
	};

	public static final Thunk EMPTY_THUNK = new Thunk(EMPTY);

	private Thunk head;

	private Thunk tail;

	public List(final Thunk hd, final Thunk tl) {
		head = hd;
		tail = tl;
	}

	@Override
	public Node getHead() {
		return head.getNode();
	}

	@Override
	public Node getTail() {
		return tail.getNode();
	}

	public static Thunk get(final Thunk hd, final Thunk tl) {
		return new Thunk(new List(hd, tl));
	}

	@Override
	public String toString() {
		return head + " : " + tail;
	}

	@Override
	public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
		head = head.link(defs, linked);
		tail = tail.link(defs, linked);
		return null;
	}

	@Override
	public void eval(final Writer w) throws IOException {
		w.append("[");
		List nd = this;
		boolean first = true;
		while (nd.tail != null) {
			if (first) {
				first = false;
			} else {
				w.append(", ");
			}
			nd.head.eval(w);
			final Node tl = nd.tail.wHNF();
			if (!(tl instanceof List)) {
				throw new IllegalArgumentException("Malformed list: " + this);
			}
			nd = (List) tl;
		}
		w.append("]");
	}

	@Override
	public boolean eq(final Node n) {
		if (n == this) {
			return true;
		}
		if (!(n instanceof List) || n == EMPTY) {
			return false;
		}
		final List ol = (List) n;
		return head.eq(ol.head) && tail.eq(ol.tail);
	}

}
