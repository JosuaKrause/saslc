package xi.optimizer;

/**
 * 
 * @author Joschi
 * 
 */
public enum OptLevel {

    /**
     * {x -> a b} -> K (a b) if x is free in a and b
     */
    K_OPT,

    /**
     * 
     */
    BC_OPT,

    /**
     * 
     */
    BC_EXT_OPT,

    /**
     * 
     */
    PATTERN_OPT, ;

    private static int optField = -1;

    public static void setLevel(final int optField) {
        OptLevel.optField = optField;
    }

    public boolean isSet() {
        return isSet(optField);
    }

    public boolean isSet(final int bitfield) {
        return (bitfield & (1 << ordinal())) != 0;
    }

}
