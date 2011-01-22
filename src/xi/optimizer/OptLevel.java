package xi.optimizer;

/**
 * Optimization level.
 * 
 * @author Joschi
 */
public enum OptLevel {

    /** {x -> a b} -> K (a b) if x is free in a and b. */
    K_OPT,

    /** Creates B and C combinators. */
    BC_OPT,

    /** Creates B*, C' and S' combinators. */
    BC_EXT_OPT,

    /** Uses pattern matching for optimizing SK expressions. */
    PATTERN_OPT,

    ;

    /** Initial optimization bit field, enabling all optimizations. */
    private static int optField = -1;

    /**
     * Sets the current optimization bit field.
     * 
     * @param optField
     *            bit field to set
     */
    public static void setLevel(final int optField) {
        OptLevel.optField = optField;
    }

    /**
     * Checks whether this optimization is set.
     * 
     * @return result of check
     */
    public boolean isSet() {
        return isSet(optField);
    }

    /**
     * Checks whether this optimization is set in the given bit field.
     * 
     * @param bitfield
     *            bit field
     * @return result of check
     */
    public boolean isSet(final int bitfield) {
        return (bitfield & (1 << ordinal())) != 0;
    }
}
