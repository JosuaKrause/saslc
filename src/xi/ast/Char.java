package xi.ast;

import xi.util.StringUtils;

/**
 * Character literal.
 * 
 * @author Joschi
 * 
 */
public class Char extends Value {

	/** Size of the char cache. */
	private static final int CACHE_SIZE = 128;

	/**
	 * Lazy initialized Char-Cache.
	 * 
	 * @author Joschi
	 * 
	 */
	private static class CharCache {
		private CharCache() {
			// will never be initialized
		}

		/**
		 * The cache.
		 */
		static final Char cache[] = new Char[CACHE_SIZE];

		/**
		 * Fills the cache when the class is loaded i.e. when we get a small
		 * value the first time.
		 */
		static {
			for (int i = CACHE_SIZE; i-- > 0;) {
				cache[i] = new Char(i);
			}
		}
	}

	/**
	 * @param cp
	 *            The CodePoint of the character.
	 * @return The Char representing the CodePoint.
	 */
	public static Char valueOf(final int cp) {
		if (0 <= cp && cp < CACHE_SIZE) { // yeah! cache!
			return CharCache.cache[cp];
		}
		return new Char(cp);
	}

	/**
	 * Builds a list of Characters for the given String.
	 * 
	 * @param s
	 *            The String.
	 * @return The list node.
	 */
	public static Expr listFromStr(final String s) {
		Expr e = BuiltIn.NIL;
		for (int i = s.length(); i-- > 0;) {
			final char c = s.charAt(i);
			int cp = c;
			if (Character.isLowSurrogate(c)) {
				cp = Character.toCodePoint(s.charAt(--i), c);
			}
			e = BuiltIn.CONS.app(valueOf(cp), e);
		}
		return e;
	}

	/** Code point of this Char. */
	private final int cp;

	/**
	 * Simple constructor.
	 * 
	 * @param c
	 *            The CodePoint.
	 */
	private Char(final int c) {
		cp = c;
	}

	@Override
	public String toString() {
		// TODO: print characters correctly
		return "CHAR: "
				+ StringUtils.escape(String.valueOf(Character.toChars(cp)));
	}

}
