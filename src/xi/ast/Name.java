package xi.ast;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Name literal.
 * 
 * @author Leo
 * @author Joschi
 */
public class Name extends Value implements Comparable<Name> {

	/** Counter for creating new unique names. */
	private static int nameCounter;

	/**
	 * A map holding all created names. Used for name sharing.
	 */
	private static Map<String, Name> CACHE = new HashMap<String, Name>();

	/**
	 * Returns the name-node given by the string. Names are always shared.
	 * 
	 * @param name
	 *            The name.
	 * @return The name-node.
	 */
	public static Name valueOf(final String name) {
		if (!CACHE.containsKey(name)) {
			CACHE.put(name, new Name(name));
		}
		return CACHE.get(name);
	}

	/** The name's string value. */
	private final String name;

	/**
	 * Constructor.
	 * 
	 * @param n
	 *            name
	 */
	private Name(final String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

	public static Name createName() {
		return Name.valueOf("$" + nameCounter++);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean hasFree(final Name var) {
		return equals(var);
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Name)) {
			return false;
		}
		return name.equals(((Name) obj).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public int compareTo(final Name o) {
		return name.compareTo(o.name);
	}

	@Override
	protected void freeVars(final Deque<Name> bound, final Set<Name> free) {
		if (!bound.contains(this)) {
			free.add(this);
		}
	}

}
