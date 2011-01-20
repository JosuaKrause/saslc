package xi.sk;

/**
 * Enumeration of all combinators known to the SK machine.
 * 
 * @author Leo Woerteler
 */
public enum SKPrim {

    /** The substitution combinator. */
    S('S', 3),

    /** Another substitution combinator. */
    S_PRIME("S'", 'R', 4),

    /** The right-side substitution combinator. */
    B('B', 3),

    /** The right-side substitution combinator. */
    B_STAR("B*", 'b', 4),

    /** The left-side substitution combinator. */
    C('C', 3),

    /** Another left-side substitution combinator. */
    C_PRIME("C'", 'c', 4),

    /** The constant combinator. */
    K('K'),

    /** The identity combinator. */
    I('I', 1),

    /** The recursion combinator. */
    Y('Y', 1),

    /** The uncurry combinator. */
    U('U'),

    /** Addition. */
    ADD('+'),

    /** Subtraction. */
    SUB('-'),

    /** Multiplication. */
    MUL('*'),

    /** Division. */
    DIV('/'),

    /** Modulo. */
    MOD('%'),

    /** Logical negation. */
    NOT('!', 1),

    /** Numeric negation. */
    NEG('~', 1),

    /** Conditional operator. */
    COND('?', 3),

    /** List construction. */
    CONS(':'),

    /** Head of a list. */
    HEAD('h', 1),

    /** Tail of a list. */
    TAIL('t', 1),

    /** Less-than operator. */
    LT('L'),

    /** Less-than-or-equal operator. */
    LTE('l'),

    /** Equality operator. */
    EQ('e'),

    /** Non-equality operator. */
    NEQ('n'),

    /** Greater-than-or-equal operator. */
    GTE('g'),

    /** Greater-than operator. */
    GT('G'),

    /** Logical conjunction. */
    AND('&'),

    /** Logical disjunction. */
    OR('|'),

    /** Sequence operator. */
    SEQ('s'),

    /** Character casting operator. */
    CHAR('v', 1),

    /** Dummy operator. */
    NOP('\0', Integer.MAX_VALUE),

    NIL("[]", '_', 0);

    /** The function's cardinality. */
    public final int arity;

    public final String name;

    /** the function's name. */
    public final char chr;

    /** Hash table for characters. */
    private static SKPrim[] TABLE = new SKPrim[1 << 7];
    static {
        for (final SKPrim p : values()) {
            TABLE[p.chr] = p;
        }
    }

    /**
     * Constructor taking the function's name and cardinality.
     * 
     * @param n
     *            function name
     * @param c
     *            associated char
     * @param a
     *            function arity
     */
    private SKPrim(final String n, final char c, final int a) {
        name = n;
        chr = c;
        arity = a;
    }

    /**
     * Constructor taking the function's name and cardinality.
     * 
     * @param c
     *            associated char
     * @param a
     *            function arity
     */
    private SKPrim(final char c, final int a) {
        this(Character.toString(c), c, a);
    }

    /**
     * Constructor taking the function's name and cardinality.
     * 
     * @param c
     *            associated char
     */
    private SKPrim(final char c) {
        this(Character.toString(c), c);
    }

    /**
     * Constructor taking the function's name, the arity is assumed to be 2.
     * 
     * @param n
     *            function name
     * @param c
     *            associated char
     */
    private SKPrim(final String n, final char c) {
        this(n, c, 2);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns the SKPrim value associated with this character or {@code null},
     * if none exists.
     * 
     * @param c
     *            character
     * 
     * @return associated SKPrim or {@code null}
     */
    public static SKPrim get(final char c) {
        return c < TABLE.length ? TABLE[c] : null;
    }
}
