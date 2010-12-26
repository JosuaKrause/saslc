package xi.go.cst.prim;

import java.math.BigInteger;

/**
 * Number node.
 * 
 * @author Leo
 * @author Joschi
 */
public class Num extends Value {

    /** Numeric value of this node. */
    protected BigInteger value;

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

}
