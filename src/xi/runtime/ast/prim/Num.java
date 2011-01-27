package xi.runtime.ast.prim;

import java.math.BigInteger;

import xi.sk.SKVisitor;

/**
 * Number node.
 * 
 * @author Leo
 * @author Joschi
 */
public class Num extends Value {

    /** Numeric value of this node. */
    BigInteger value;

    /**
     * Constructor.
     * 
     * @param val
     *            value of this node
     */
    public Num(final BigInteger val) {
        value = val;
    }

    @Override
    public final BigInteger getNum() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean eq(final Value n) {
        return value.equals(n.getNum());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Num && value.equals(((Num) obj).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.num(value);
    }
}
