package xi.ast;

import java.math.BigInteger;

/**
 * Integer literal.
 * 
 * @author Leo
 * @author Joschi
 */
public final class Num extends Value {

    /**
     * The beginning of the cached numbers.
     */
    private static final BigInteger LOW = BigInteger.valueOf(-256);

    /**
     * The end of the cached numbers.
     */
    private static final BigInteger HI = BigInteger.valueOf(255);

    /**
     * Lazy initialized Num-Cache. Used to share certain numbers.
     * 
     * @author Joschi
     */
    private static final class NumCache {
        /** Hidden default constructor. */
        private NumCache() {
            // will never be initialized
        }

        /**
         * The number cache.
         */
        static final Num[] CACHE = new Num[HI.intValue() - LOW.intValue() + 1];

        /**
         * Fills the number cache. Is loaded when the first such number is
         * loaded.
         */
        static {
            int i = CACHE.length;
            while (i-- > 0) {
                CACHE[i] = new Num(LOW.add(BigInteger.valueOf(i)));
            }
        }
    }

    /**
     * Creates a Number-Node for the given integer. Small values will be cached
     * and therefore be shared.
     * 
     * @param i
     *            The integer.
     * @return The number.
     */
    public static Num valueOf(final BigInteger i) {
        if (i.compareTo(LOW) >= 0 && i.compareTo(HI) <= 0) { // yeah! cache!
            return NumCache.CACHE[i.intValue() - LOW.intValue()];
        }
        return new Num(i);
    }

    /** Int value. */
    private final BigInteger val;

    /**
     * Constructor.
     * 
     * @param i
     *            value
     */
    private Num(final BigInteger i) {
        val = i;
    }

    /**
     * Getter for this number's BigInteger value.
     * 
     * @return BigInteger value
     */
    public BigInteger getValue() {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Num && val.equals(((Num) other).val);
    }

    @Override
    public int hashCode() {
        return val.hashCode();
    }

}
