package xi.go.cst;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import xi.go.cst.prim.Function;
import xi.go.cst.prim.Value;

public class BlockingNode extends Value {

    private volatile Value realNode;

    public BlockingNode() {
        realNode = null;
    }

    public synchronized void setNode(final Value node) {
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
    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        ensureNode();
        return realNode.link(defs, linked);
    }

    @Override
    public String toString() {
        ensureNode();
        return realNode.toString();
    }

    @Override
    public boolean eq(final Value n) {
        ensureNode();
        return realNode.eq(n);
    }

    @Override
    public void eval(final Writer w) throws IOException {
        ensureNode();
        realNode.eval(w);
    }

    @Override
    public boolean getBool() {
        ensureNode();
        return realNode.getBool();
    }

    @Override
    public Thunk getHead() {
        ensureNode();
        return realNode.getHead();
    }

    @Override
    public BigInteger getNum() {
        ensureNode();
        return realNode.getNum();
    }

    @Override
    public Thunk getTail() {
        ensureNode();
        return realNode.getTail();
    }

    @Override
    public boolean isChar() {
        ensureNode();
        return realNode.isChar();
    }

    @Override
    public boolean isRef() {
        ensureNode();
        return realNode.isRef();
    }

    @Override
    public boolean equals(final Object obj) {
        ensureNode();
        return realNode.equals(obj);
    }

    @Override
    public int hashCode() {
        ensureNode();
        return realNode.hashCode();
    }
}
