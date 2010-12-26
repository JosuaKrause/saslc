package xi.go.cst.prim;

import java.math.BigInteger;

import xi.go.cst.Thunk;

/**
 * Value node.
 * 
 * @author Leo
 * @author Joschi
 */
public abstract class Value extends Prim {

    @Override
    public final boolean isValue() {
        return true;
    }

    /**
     * Getter for the numeric value of this node.
     * 
     * @return numeric value
     */
    public BigInteger getNum() {
        throw new UnsupportedOperationException("No number.");
    }

    /**
     * Getter for the boolean value of this node.
     * 
     * @return boolean value
     */
    public boolean getBool() {
        throw new UnsupportedOperationException("No boolean.");
    }

    /**
     * Getter for the head of this list node.
     * 
     * @return head
     */
    public Thunk getHead() {
        throw new UnsupportedOperationException("No list.");
    }

    /**
     * Getter for the tail of this list node.
     * 
     * @return tail
     */
    public Thunk getTail() {
        throw new UnsupportedOperationException("No list.");
    }

    /**
     * Checks whether two nodes are equal.
     * 
     * @param n
     *            node to compare to.
     * @return {@code true}, if both nodes are equal, {@code false} otherwise
     */
    public abstract boolean eq(final Value n);

    @Override
    public boolean shareNode() {
        return true;
    }

    /**
     * Checks whether this values behaves like a list.
     * 
     * @return result of the check
     */
    public boolean isList() {
        return false;
    }

}
