package xi.ast;

/**
 * Built-in operations.
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class BuiltIn extends Value {

	public static final BuiltIn CONS = new BuiltIn(":");
	public static final BuiltIn BRANCH = new BuiltIn("?");
	public static final BuiltIn PLUS = new BuiltIn("+");
	public static final BuiltIn MINUS = new BuiltIn("-");
	public static final BuiltIn TIMES = new BuiltIn("*");
	public static final BuiltIn DIV = new BuiltIn("/");
	public static final BuiltIn MOD = new BuiltIn("%");
	public static final BuiltIn EQ = new BuiltIn("==");
	public static final BuiltIn NE = new BuiltIn("!=");
	public static final BuiltIn LT = new BuiltIn("<");
	public static final BuiltIn GT = new BuiltIn(">");
	public static final BuiltIn LE = new BuiltIn("<=");
	public static final BuiltIn GE = new BuiltIn(">=");
	public static final BuiltIn AND = new BuiltIn("&");
	public static final BuiltIn OR = new BuiltIn("|");
	public static final BuiltIn NOT = new BuiltIn("!");
	public static final BuiltIn UMINUS = new BuiltIn("~");
	public static final BuiltIn NIL = new BuiltIn("[]");
	public static final BuiltIn S = new BuiltIn("S");
	public static final BuiltIn K = new BuiltIn("K");
	public static final BuiltIn I = new BuiltIn("I");
	public static final BuiltIn Y = new BuiltIn("Y");
	public static final BuiltIn HD = new BuiltIn("HD");
	public static final BuiltIn TL = new BuiltIn("TL");
	public static final BuiltIn SEQ = new BuiltIn("!>");

	/** Display name of this operation. */
	private final String name;

	/**
	 * Constructor taking the display name of the operation.
	 * 
	 * @param n
	 *            name
	 * @param prim
	 *            The primitive type.
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
		Expr res = this;
		for (final Expr e : es) {
			res = App.create(res, e);
		}
		return res;
	}

}
