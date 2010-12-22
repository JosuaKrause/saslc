package xi.go;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import xi.go.cst.Thunk;

/**
 * Scans an input and constructs a tree.
 * 
 * @author Joschi
 * 
 */
public abstract class AbstractInterpreter {

	private Reader in;

	private int lastChar;

	protected final Map<String, Thunk> fTable;

	public AbstractInterpreter() {
		fTable = new HashMap<String, Thunk>();
	}

	public Map<String, Thunk> getFunctionTable() {
		return fTable;
	}

	/**
	 * Helper for filling {@link #lastChar} with content.
	 */
	private void fillNext() {
		try {
			lastChar = in.read();
		} catch (final IOException e) {
			e.printStackTrace();
			lastChar = -1;
		}
	}

	protected boolean hasNext() {
		return lastChar != -1;
	}

	/**
	 * @return The next character.
	 * @throws IllegalStateException
	 *             When the end of the stream has been reached.
	 */
	protected char next() {
		fillNext();
		return current();
	}

	/**
	 * @return The next character w/o advancing the position.
	 */
	protected char current() {
		return (char) lastChar;
	}

	protected int nextInt() {
		char c;
		int res = -1;
		while ((c = current()) >= '0' && c < '9') {
			final int digit = c - '0';
			if (res == -1) {
				res = digit;
			} else {
				res = res * 10 + digit;
			}
			next();
		}
		if (res == -1) {
			throw new IllegalStateException("Expected number got " + current());
		}
		return res;
	}

	private String capture(final char start, final char end) {
		if (current() != start) {
			throw new IllegalStateException("Expected " + start + " got "
					+ current());
		}
		char c;
		final StringBuilder sb = new StringBuilder();
		while ((c = next()) != end) {
			sb.append(c);
		}
		next();
		return sb.toString();
	}

	protected String getName() {
		return capture('<', '>');
	}

	protected String getDef() {
		return capture('=', ';');
	}

	protected String getString() {
		if (current() != '"') {
			throw new IllegalStateException("Expected '<' got " + current());
		}
		char c;
		final StringBuilder sb = new StringBuilder();
		while ((c = next()) != '"') {
			if (c == '\\') {
				c = next();
			}
			sb.append(c);
		}
		next();
		System.out.println("STR:" + sb.toString());
		return sb.toString();
	}

	public void read(final Reader r) {
		in = r;
		fillNext();
		read();
		try {
			in.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		in = null;
	}

	protected abstract void read();

}
