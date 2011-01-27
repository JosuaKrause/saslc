package xi.runtime.ast.prim;

import java.math.BigInteger;

import xi.sk.SKVisitor;
import xi.util.StringUtils;

/**
 * Character node.
 * 
 * @author Leo
 * @author Joschi
 */
public class Char extends Num {

    /**
     * Constructor taking the represented code point.
     * 
     * @param cp
     *            code point
     */
    public Char(final int cp) {
        super(BigInteger.valueOf(cp));
    }

    @Override
    public final String toString() {
        return StringUtils.escape(Character.toChars(value.intValue()));
    }

    @Override
    public final boolean isChar() {
        return true;
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.chr(value.intValue());
    }
}
