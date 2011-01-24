package xi.ast;

import xi.sk.SKPrim;
import xi.sk.SKVisitor;

/**
 * Built-in operations.
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class BuiltIn extends Value {

    /** Jump table. */
    private static final BuiltIn[] TABLE = new BuiltIn[1 << 7];

    /** List construction. */
    public static final BuiltIn CONS = new BuiltIn(SKPrim.CONS);
    /** Condition operator. */
    public static final BuiltIn BRANCH = new BuiltIn(SKPrim.COND);
    /** Addition. */
    public static final BuiltIn PLUS = new BuiltIn(SKPrim.ADD);
    /** Subtraction. */
    public static final BuiltIn MINUS = new BuiltIn(SKPrim.SUB);
    /** Multiplication. */
    public static final BuiltIn TIMES = new BuiltIn(SKPrim.MUL);
    /** Division. */
    public static final BuiltIn DIV = new BuiltIn(SKPrim.DIV);
    /** Modulo. */
    public static final BuiltIn MOD = new BuiltIn(SKPrim.MOD);
    /** Equality. */
    public static final BuiltIn EQ = new BuiltIn(SKPrim.EQ);
    /** Inequality. */
    public static final BuiltIn NE = new BuiltIn(SKPrim.NEQ);
    /** Less-than. */
    public static final BuiltIn LT = new BuiltIn(SKPrim.LT);
    /** Greater-than. */
    public static final BuiltIn GT = new BuiltIn(SKPrim.GT);
    /** Less-than-or-equal. */
    public static final BuiltIn LE = new BuiltIn(SKPrim.LTE);
    /** Greater-than. */
    public static final BuiltIn GE = new BuiltIn(SKPrim.GTE);
    /** Logical conjunction. */
    public static final BuiltIn AND = new BuiltIn(SKPrim.AND);
    /** Logical disjunction. */
    public static final BuiltIn OR = new BuiltIn(SKPrim.OR);
    /** Logical negation. */
    public static final BuiltIn NOT = new BuiltIn(SKPrim.NOT);
    /** Numerical negation. */
    public static final BuiltIn UMINUS = new BuiltIn(SKPrim.NEG);
    /** Empty list. */
    public static final BuiltIn NIL = new BuiltIn(SKPrim.NIL) {
        @Override
        public void traverse(final SKVisitor v) {
            v.nil();
        }
    };
    /** The substitution combinator. */
    public static final BuiltIn S = new BuiltIn(SKPrim.S);
    /** Another substitution combinator. */
    public static final BuiltIn S_PRIME = new BuiltIn(SKPrim.S_PRIME);
    /** The left-side substitution combinator. */
    public static final BuiltIn B = new BuiltIn(SKPrim.B);
    /** The double left-side substitution combinator. */
    public static final BuiltIn B_STAR = new BuiltIn(SKPrim.B_STAR);
    /** The right-side substitution combinator. */
    public static final BuiltIn C = new BuiltIn(SKPrim.C);
    /** Another right-side substitution combinator. */
    public static final BuiltIn C_PRIME = new BuiltIn(SKPrim.C_PRIME);
    /** The constant combinator. */
    public static final BuiltIn K = new BuiltIn(SKPrim.K);
    /** The identity combinator. */
    public static final BuiltIn I = new BuiltIn(SKPrim.I);
    /** The recursion combinator. */
    public static final BuiltIn Y = new BuiltIn(SKPrim.Y);
    /** The uncurry combinator. */
    public static final BuiltIn U = new BuiltIn(SKPrim.U);
    /** The strict uncurry combinator. */
    public static final BuiltIn U_PRIME = new BuiltIn(SKPrim.U_PRIME);
    /** Head of a list. */
    public static final BuiltIn HD = new BuiltIn(SKPrim.HEAD);
    /** Tail of a list. */
    public static final BuiltIn TL = new BuiltIn(SKPrim.TAIL);
    /** Strict sequence operator. */
    public static final BuiltIn SEQ = new BuiltIn(SKPrim.SEQ);
    /** Character casting operator. */
    public static final BuiltIn CHAR = new BuiltIn(SKPrim.CHAR);
    /** Pattern-match failure operator. */
    public static final BuiltIn FAIL = new BuiltIn(SKPrim.FAIL);

    /** Display name of this operation. */
    private final SKPrim prim;

    /**
     * Constructor.
     * 
     * @param p
     *            wrapped primitive
     */
    private BuiltIn(final SKPrim p) {
        prim = p;
        TABLE[p.chr] = this;
    }

    @Override
    public String toString() {
        return prim.toString();
    }

    /**
     * Retrieves the BuiltIn associated with the given character.
     * 
     * @param c
     *            character for the BuiltIn
     * @return associated BuiltIn if existent, {@code null} otherwise
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

    @Override
    public void traverse(final SKVisitor v) {
        v.prim(prim);
    }
}
