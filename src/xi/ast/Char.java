package xi.ast;

import xi.sk.SKVisitor;

/**
 * Character literal.
 * 
 * @author Joschi
 * 
 */
public final class Char extends Value {

    /** Size of the char cache. */
    private static final int CACHE_SIZE = 128;

    /**
     * Lazy initialized Char-Cache.
     * 
     * @author Joschi
     * 
     */
    private static final class CharCache {
        /** Hidden default constructor. */
        private CharCache() {
            /* will never be initialized */
        }

        /**
         * The cache.
         */
        static final Char[] CACHE = new Char[CACHE_SIZE];

        /**
         * Fills the cache when the class is loaded i.e. when we get a small
         * value the first time.
         */
        static {
            for (int i = CACHE_SIZE; i-- > 0;) {
                CACHE[i] = new Char(i);
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
            return CharCache.CACHE[cp];
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

    /**
     * Getter for this character's code point.
     * 
     * @return code point
     */
    public int getChar() {
        return cp;
    }

    @Override
    public String toString() {
        return String.valueOf(Character.toChars(cp));
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Char && ((Char) other).cp == cp;
    }

    @Override
    public int hashCode() {
        return cp;
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.chr(cp);
    }
}
