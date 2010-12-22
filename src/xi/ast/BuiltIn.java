package xi.ast;

/**
 * Built-in operations.
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public final class BuiltIn extends Value {

    /** List construction. */
    public static final BuiltIn CONS = new BuiltIn(":");
    /** Condition operator. */
    public static final BuiltIn BRANCH = new BuiltIn("?");
    /** Addition. */
    public static final BuiltIn PLUS = new BuiltIn("+");
    /** Subtraction. */
    public static final BuiltIn MINUS = new BuiltIn("-");
    /** Multiplication. */
    public static final BuiltIn TIMES = new BuiltIn("*");
    /** Division. */
    public static final BuiltIn DIV = new BuiltIn("/");
    /** Modulo. */
    public static final BuiltIn MOD = new BuiltIn("%");
    /** Equality. */
    public static final BuiltIn EQ = new BuiltIn("==");
    /** Inequality. */
    public static final BuiltIn NE = new BuiltIn("!=");
    /** Less-than. */
    public static final BuiltIn LT = new BuiltIn("<");
    /** Greater-than. */
    public static final BuiltIn GT = new BuiltIn(">");
    /** Less-than-or-equal. */
    public static final BuiltIn LE = new BuiltIn("<=");
    /** Greater-than. */
    public static final BuiltIn GE = new BuiltIn(">=");
    /** Logical conjunction. */
    public static final BuiltIn AND = new BuiltIn("&");
    /** Logical disjunction. */
    public static final BuiltIn OR = new BuiltIn("|");
    /** Logical negation. */
    public static final BuiltIn NOT = new BuiltIn("!");
    /** Numerical negation. */
    public static final BuiltIn UMINUS = new BuiltIn("~");
    /** Empty list. */
    public static final BuiltIn NIL = new BuiltIn("[]");
    /** The substitution combinator. */
    public static final BuiltIn S = new BuiltIn("S");
    /** The constant combinator. */
    public static final BuiltIn K = new BuiltIn("K");
    /** The identity combinator. */
    public static final BuiltIn I = new BuiltIn("I");
    /** The recursion combinator. */
    public static final BuiltIn Y = new BuiltIn("Y");
    /** Head of a list. */
    public static final BuiltIn HD = new BuiltIn("HD");
    /** Tail of a list. */
    public static final BuiltIn TL = new BuiltIn("TL");
    /** Strict sequence operator. */
    public static final BuiltIn SEQ = new BuiltIn("!>");

    /** Display name of this operation. */
    private final String name;

    /**
     * Constructor taking the display name of the operation.
     * 
     * @param n
     *            name
     */
    private BuiltIn(final String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
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
}
