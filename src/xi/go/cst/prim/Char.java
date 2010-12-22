package xi.go.cst.prim;

import java.math.BigInteger;

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

}
