package xi.go.cst;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import xi.go.cst.prim.Function;
import xi.go.cst.prim.Value;

public class BlockingNode extends Value {

	private volatile Node realNode;

	public BlockingNode() {
		realNode = null;
	}

	public synchronized void setNode(final Node node) {
		realNode = node;
		notifyAll();
	}

	private synchronized void ensureNode() {
		try {
			while (realNode == null) {
				wait();
			}
		} catch (final InterruptedException e) {
			// interrupted
		}
	}

	protected Node getNode() {
		ensureNode();
		return realNode;
	}

	@Override
	public Thunk getLeft() {
		ensureNode();
		return realNode.getLeft();
	}

	@Override
	public Thunk getRight() {
		ensureNode();
		return realNode.getRight();
	}

	@Override
	public int getNum() {
		ensureNode();
		return realNode.getNum();
	}

	@Override
	public int getChar() {
		ensureNode();
		return realNode.getChar();
	}

	@Override
	public boolean getBool() {
		ensureNode();
		return realNode.getBool();
	}

	@Override
	public Node getHead() {
		ensureNode();
		return realNode.getHead();
	}

	@Override
	public Node getTail() {
		ensureNode();
		return realNode.getTail();
	}

	@Override
	public Function.Def getFunction() {
		ensureNode();
		return realNode.getFunction();
	}

	@Override
	public boolean isApp() {
		ensureNode();
		return realNode.isApp();
	}

	@Override
	public boolean eq(Node n) {
		ensureNode();
		if (n instanceof BlockingNode) {
			n = ((BlockingNode) n).getNode();
		}
		return realNode.eq(n);
	}

	@Override
	public void eval(final Writer w) throws IOException {
		ensureNode();
		realNode.eval(w);
	}

	@Override
	public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
		ensureNode();
		return realNode.link(defs, linked);
	}

	@Override
	public boolean isValue() {
		ensureNode();
		return realNode.isValue();
	}

	@Override
	public String toString() {
		ensureNode();
		return realNode.toString();
	}

}
