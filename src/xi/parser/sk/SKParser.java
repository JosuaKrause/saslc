package xi.parser.sk;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.util.Stack;

/**
 * Scans an input and constructs a tree.
 * 
 * @author Joschi
 * @author Leo
 * @param <T>
 *            type of the values to construct
 * 
 */
public abstract class SKParser<T> {

    /** Input reader. */
    private Reader in;

    /** Last character that was read. */
    private int lastChar;

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
     * @return the functin name
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
        final Stack<T> stack = new Stack<T>();
        while (hasNext()) {
            final char c = current();
            switch (c) {
            case 'i':
                next();
                final BigInteger bi = nextInt();
                stack.push(num(bi));
                continue;
            case '=':
                final String name = getDef();
                def(name, stack.pop());
                continue;
            case '"':
                final String str = getString();
                stack.push(string(str));
                continue;
            case '<':
                final String ref = getName();
                stack.push(reference(ref));
                continue;
            case '\'':
                final char chr = next();
                int cp;
                if (Character.isHighSurrogate(chr)) {
                    cp = Character.toCodePoint(chr, next());
                } else {
                    cp = chr;
                }
                stack.push(character(cp));
                break;
            case '@':
                final T f = stack.pop();
                final T x = stack.pop();
                stack.push(app(f, x));
                break;
            case 'T':
            case 'F':
                stack.push(bool(c == 'T'));
                break;
            case '_':
                stack.push(nil());
                break;
            case ',':
                final T hd = stack.pop();
                final T tl = stack.pop();
                stack.push(cons(hd, tl));
                break;
            case ' ':
            case '\r':
            case '\n':
            case '\t':
                // ignore new-lines or white-spaces
                break;
            default:
                stack.push(prim(c));
            }
            next();
        }
        if (!stack.isEmpty()) {
            throw new IllegalStateException("Premature EOF (stack not empty).");
        }
    }

    /**
     * String literal.
     * 
     * @param str
     *            the literal
     * @return string node
     */
    protected abstract T string(final String str);

    /**
     * Numeric literal.
     * 
     * @param n
     *            the number
     * @return number node
     */
    protected abstract T num(final BigInteger n);

    /**
     * Character literal.
     * 
     * @param cp
     *            the character's Unicode code point
     * @return character node
     */
    protected abstract T character(final int cp);

    /**
     * Boolean literal.
     * 
     * @param b
     *            value
     * @return boolean node
     */
    protected abstract T bool(final boolean b);

    /**
     * Primitive function.
     * 
     * @param p
     *            the primitive
     * @return primitive node
     */
    protected abstract T prim(final char p);

    /**
     * A list node.
     * 
     * @param hd
     *            head of the list
     * @param tl
     *            tail of the list
     * @return list node
     */
    protected abstract T cons(final T hd, final T tl);

    /**
     * The empty list.
     * 
     * @return empty list node
     */
    protected abstract T nil();

    /**
     * Application node.
     * 
     * @param f
     *            function node
     * @param x
     *            value node
     * @return application node
     */
    protected abstract T app(final T f, final T x);

    /**
     * Global name definition.
     * 
     * @param name
     *            the bound name
     * @param body
     *            bound expression
     */
    protected abstract void def(final String name, final T body);

    /**
     * Reference to a global name.
     * 
     * @param name
     *            referenced name
     * @return reference node
     */
    protected abstract T reference(final String name);
}
