package xi.go;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
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

    /** Input reader. */
    private Reader in;

    /** Last character that was read. */
    private int lastChar;

    /** Function table. */
    private final Map<String, Thunk> fTable;

    /** Default constructor. */
    public AbstractInterpreter() {
        fTable = new HashMap<String, Thunk>();
    }

    /**
     * Getter for the fuction table.
     * 
     * @return function table
     */
    protected final Map<String, Thunk> getFunctionTable() {
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

    /**
     * Checks whether there are characters left.
     * 
     * @return {@code true}, if there are more characters to read, {@code false}
     *         otherwise.
     */
    protected final boolean hasNext() {
        return lastChar != -1;
    }

    /**
     * Advances the character pointer and returns the next character.
     * 
     * @return The next character.
     */
    protected final char next() {
        fillNext();
        return current();
    }

    /**
     * @return The next character w/o advancing the position.
     */
    protected final char current() {
        return (char) lastChar;
    }

    /**
     * Reads and returns a {@link BigInteger}.
     * 
     * @return the {@link BigInteger} that was read
     */
    protected final BigInteger nextInt() {
        char c;
        BigInteger res = null;
        while ((c = current()) >= '0' && c <= '9') {
            final BigInteger digit = BigInteger.valueOf(c - '0');
            if (res == null) {
                res = digit;
            } else {
                res = res.multiply(BigInteger.TEN).add(digit);
            }
            next();
        }
        if (res == null) {
            throw new IllegalStateException("Expected number got " + current());
        }
        return res;
    }

    /**
     * Reads all characters between {@code start} and {@code end}.
     * 
     * @param start
     *            first character
     * @param end
     *            last character
     * @return the characters in between
     */
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

    /**
     * Reads a function reference from the input.
     * 
     * @return the functin name
     */
    protected final String getName() {
        return capture('<', '>');
    }

    /**
     * Reads a function name from the input.
     * 
     * @return the function name
     */
    protected final String getDef() {
        return capture('=', ';');
    }

    /**
     * Reads a string from the input.
     * 
     * @return the string
     */
    protected final String getString() {
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
        return sb.toString();
    }

    /**
     * Reads the contents of the given {@link Reader}.
     * 
     * @param r
     *            reader
     */
    public final void read(final Reader r) {
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

    /**
     * Reads the SK code.
     */
    protected abstract void read();

}
