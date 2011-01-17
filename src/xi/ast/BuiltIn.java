package xi.ast;

/**
 * Built-in operations.
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public final class BuiltIn extends Value {

    /** Jump table. */
    private static final BuiltIn[] TABLE = new BuiltIn[1 << 7];

    /** List construction. */
    public static final BuiltIn CONS = new BuiltIn(':');
    /** Condition operator. */
    public static final BuiltIn BRANCH = new BuiltIn('?');
    /** Addition. */
    public static final BuiltIn PLUS = new BuiltIn('+');
    /** Subtraction. */
    public static final BuiltIn MINUS = new BuiltIn('-');
    /** Multiplication. */
    public static final BuiltIn TIMES = new BuiltIn('*');
    /** Division. */
    public static final BuiltIn DIV = new BuiltIn('/');
    /** Modulo. */
    public static final BuiltIn MOD = new BuiltIn('%');
    /** Equality. */
    public static final BuiltIn EQ = new BuiltIn('e');
    /** Inequality. */
    public static final BuiltIn NE = new BuiltIn('n');
    /** Less-than. */
    public static final BuiltIn LT = new BuiltIn('L');
    /** Greater-than. */
    public static final BuiltIn GT = new BuiltIn('G');
    /** Less-than-or-equal. */
    public static final BuiltIn LE = new BuiltIn('l');
    /** Greater-than. */
    public static final BuiltIn GE = new BuiltIn('g');
    /** Logical conjunction. */
    public static final BuiltIn AND = new BuiltIn('&');
    /** Logical disjunction. */
    public static final BuiltIn OR = new BuiltIn('|');
    /** Logical negation. */
    public static final BuiltIn NOT = new BuiltIn('!');
    /** Numerical negation. */
    public static final BuiltIn UMINUS = new BuiltIn('~');
    /** Empty list. */
    public static final BuiltIn NIL = new BuiltIn('_');
    /** The substitution combinator. */
    public static final BuiltIn S = new BuiltIn('S');
    /** Another substitution combinator. */
    public static final BuiltIn S_PRIME = new BuiltIn('z');
    /** The left-side substitution combinator. */
    public static final BuiltIn B = new BuiltIn('B');
    /** The double left-side substitution combinator. */
    public static final BuiltIn B_STAR = new BuiltIn('b');
    /** The right-side substitution combinator. */
    public static final BuiltIn C = new BuiltIn('C');
    /** Another right-side substitution combinator. */
    public static final BuiltIn C_PRIME = new BuiltIn('v');
    /** The constant combinator. */
    public static final BuiltIn K = new BuiltIn('K');
    /** The identity combinator. */
    public static final BuiltIn I = new BuiltIn('I');
    /** The recursion combinator. */
    public static final BuiltIn Y = new BuiltIn('Y');
    /** The uncurry combinator. */
    public static final BuiltIn U = new BuiltIn('U');
    /** Head of a list. */
    public static final BuiltIn HD = new BuiltIn('h');
    /** Tail of a list. */
    public static final BuiltIn TL = new BuiltIn('t');
    /** Strict sequence operator. */
    public static final BuiltIn SEQ = new BuiltIn('s');
    /** Character casting operator. */
    public static final BuiltIn CHAR = new BuiltIn('c');

    /** Display name of this operation. */
    private final char name;

    /** Commutativity flag. */
    public final boolean commutative;

    /**
     * Constructor taking the display name of the operation, assuming
     * non-commutativity.
     * 
     * @param n
     *            name
     */
    private BuiltIn(final char n) {
        this(n, false);
    }

    /**
     * Constructor taking the display name of the operation and the
     * commutativity flag.
     * 
     * @param n
     *            name
     * @param comm
     *            commutativity flag
     */
    private BuiltIn(final char n, final boolean comm) {
        name = n;
        TABLE[n] = this;
        commutative = comm;
    }

    @Override
    public String toString() {
        return Character.toString(name);
    }

    /**
     * Retrieves the BuiltIn associated with the given character.
     * 
     * @param c
     * @return
     */
    public static BuiltIn forChar(final char c) {
        final BuiltIn b = TABLE[c];
        if (b == null) {
            throw new IllegalArgumentException("Unknown symbol: '" + c + "'");
        }
        return b;
    }

    /**
     * Applies this operation to the given arguments.
     * 
     * @param es
     *            arguments
     * @return application
     */
    public Expr app(final Expr... es) {
        return App.create(this, es);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
}
