package xi.ast;

import xi.util.StringUtils;

/**
 * Very lazy String implementation. The list is created when needed or when the
 * length is too short. Once a lazy String is split up (via hd and tl) it is
 * unrecoverable and the internal String will eventually be garbage collected.
 * 
 * @author Joschi
 * 
 */
public class Str extends Value {

	/**
	 * The minimum length where the String is not unfolded to a list.
	 */
	private static final int MIN_LENGTH = 0;// 5;

	// TODO: we do not unfold the string cause we cannot print characters

	/**
	 * Returns a node representing the String i.e. the list of Characters. Very
	 * short Strings are automatically unfolded to lists.
	 * 
	 * @param s
	 *            The String.
	 * @return The node.
	 */
	public static Expr fromString(final String s) {
		if (s.length() < MIN_LENGTH) {
			return Char.listFromStr(s);
		}
		return new Str(s);
	}

	/**
	 * The internal String.
	 */
	private final String str;

	/**
	 * Creates a String-Node out of a String with no list unfolding.
	 * 
	 * @param str
	 *            The String.
	 */
	private Str(final String str) {
		this.str = str;
	}

	/**
	 * Example implementation for the head function.
	 * 
	 * @return The head of the String.
	 */
	public Expr head() {
		return Char.valueOf(str.codePointAt(0));
	}

	/**
	 * Example implementation for the tail function.
	 * 
	 * @return The tail of the String.
	 */
	public Expr tail() {
		return fromString(str
				.substring(Character.charCount(str.codePointAt(0))));
	}

	@Override
	public String toString() {
		return StringUtils.escape(str);
	}

}
