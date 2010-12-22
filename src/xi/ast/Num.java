package xi.ast;

/**
 * Integer literal.
 * 
 * @author Leo
 * @author Joschi
 */
public class Num extends Value {

	/**
	 * The beginning of the cached numbers.
	 */
	private static final int LOW = -256;

	/**
	 * The end of the cached numbers.
	 */
	private static final int HI = 255;

	/**
	 * Lazy initialized Num-Cache. Used to share certain numbers.
	 * 
	 * @author Joschi
	 * 
	 */
	private static class NumCache {
		private NumCache() {
			// will never be initialized
		}

		/**
		 * The number cache.
		 */
		static final Num cache[] = new Num[HI - LOW + 1];

		/**
		 * Fills the number cache. Is loaded when the first such number is
		 * loaded.
		 */
		static {
			int i = cache.length;
			while (i-- > 0) {
				cache[i] = new Num(i + LOW);
			}
		}
	}

	/**
	 * Creates a Number-Node for the given integer. Small values will be cached
	 * and therefor be shared.
	 * 
	 * @param i
	 *            The integer.
	 * @return The number.
	 */
	public static Num valueOf(final int i) {
		if (i >= LOW && i <= HI) { // yeah! cache!
			return NumCache.cache[i - LOW];
		}
		return new Num(i);
	}

	/** Int value. */
	private final int val;

	/**
	 * Constructor
	 * 
	 * @param i
	 *            value
	 */
	private Num(final int i) {
		val = i;
	}

	public int getValue() {
		return val;
	}

	@Override
	public String toString() {
		return "" + val;
	}

}
