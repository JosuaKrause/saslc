package xi.sk;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;

/**
 * 
 * @author Leo Woerteler
 */
public class SKReader {

    /** The visitor of this SKReader. */
    private final SKVisitor visitor;

    /** Input reader. */
    private Reader in;

    /** Last character that was read. */
    private int lastChar;

    public SKReader(final SKVisitor v) {
        visitor = v;
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
    private final boolean hasNext() {
        return lastChar != -1;
    }

    /**
     * Advances the character pointer and returns the next character.
     * 
     * @return The next character.
     */
    private final char next() {
        fillNext();
        return current();
    }

    /**
     * @return The next character w/o advancing the position.
     */
    private final char current() {
        return (char) lastChar;
    }

    /**
     * Reads and returns a {@link BigInteger}.
     * 
     * @return the {@link BigInteger} that was read
     */
    private final BigInteger nextInt() {
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
     * @return the function name
     */
    private final String getName() {
        return capture('<', '>');
    }

    /**
     * Reads a function name from the input.
     * 
     * @return the function name
     */
    private final String getDef() {
        return capture('=', ';');
    }

    /**
     * Reads a string from the input.
     * 
     * @return the string
     */
    private final String getString() {
        if (current() != '"') {
            throw new IllegalStateException("Expected '\"' got " + current());
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
     * @return resulting data structure
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

    private void read() {
        while (hasNext()) {
            final char c = current();
            switch (c) {
            case 'i':
                next();
                final BigInteger bi = nextInt();
                visitor.num(bi);
                continue;
            case '=':
                final String name = getDef();
                visitor.def(name);
                continue;
            case '"':
                final String str = getString();
                visitor.str(str);
                continue;
            case '<':
                final String ref = getName();
                visitor.var(ref);
                continue;
            case '\'':
                final char chr = next();
                int cp;
                if (Character.isHighSurrogate(chr)) {
                    cp = Character.toCodePoint(chr, next());
                } else {
                    cp = chr;
                }
                visitor.chr(cp);
                break;
            case '@':
                visitor.app();
                break;
            case 'T':
            case 'F':
                visitor.bool(c == 'T');
                break;
            case '_':
                visitor.nil();
                break;
            case ',':
                visitor.cons();
                break;
            case ' ':
            case '\r':
            case '\n':
            case '\t':
                // ignore new-lines or white-spaces
                break;
            default:
                final SKPrim p = SKPrim.get(c);
                if (p == null) {
                    throw new IllegalStateException("Unknown primitive: " + c);
                }
                visitor.prim(p);
            }
            next();
        }
    }

}
