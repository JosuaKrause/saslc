package xi.go.cst.prim;

import xi.sk.SKPrim;
import xi.sk.SKVisitor;

/**
 * Pattern matching failure.
 * 
 * @author Leo Woerteler
 */
public class Fail extends Value {

    /** The singleton instance. */
    public static Fail INSTANCE = new Fail();

    /** Hidden default constructor. */
    private Fail() {
        // void
    }

    @Override
    public boolean eq(final Value n) {
        return equals(n);
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "FAIL";
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.prim(SKPrim.FAIL);
    }

}
