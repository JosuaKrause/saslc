package xi.sk;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;

import xi.util.StringUtils;

/**
 * Writes SK code to an output stream.
 * 
 * @author Leo Woerteler
 * @author Joschi
 */
public class SKWriter implements SKVisitor, Closeable {

    /** Wrapped Writer instance. */
    private final Writer writer;

    /**
     * Constructor.
     * 
     * @param w
     *            the wrapped writer
     */
    public SKWriter(final Writer w) {
        writer = w;
    }

    /** String builder. */
    private final StringBuilder sb = new StringBuilder();

    /**
     * Writes the SK encoding of the given {@link SKTree}.
     * 
     * @param t
     *            SK tree
     * @throws IOException
     *             If an I/O error occurs
     */
    public void write(final SKTree t) throws IOException {
        String s;
        synchronized (sb) {
            sb.setLength(0);
            t.traverse(this);
            s = sb.toString();
        }
        writer.write(s);
    }

    @Override
    public void app() {
        sb.append('@');
    }

    @Override
    public void bool(final boolean val) {
        sb.append(val ? 'T' : 'F');
    }

    @Override
    public void chr(final int cp) {
        sb.append('\'').append(Character.toChars(cp));
    }

    @Override
    public void cons() {
        sb.append(',');
    }

    @Override
    public void def(final String name) {
        sb.append('=').append(name).append(";\n");
    }

    @Override
    public void nil() {
        sb.append('_');
    }

    @Override
    public void num(final BigInteger n) {
        sb.append('i').append(n);
    }

    @Override
    public void prim(final SKPrim p) {
        sb.append(p.chr);
    }

    @Override
    public void str(final String str) {
        sb.append('"').append(StringUtils.primitiveEscape(str)).append('"');
    }

    @Override
    public void var(final String fv) {
        sb.append('<').append(fv).append('>');
    }

    @Override
    public void nextInTuple() {
        sb.append('.');
    }

    @Override
    public void tuple(final boolean start) {
        sb.append(start ? '(' : ')');
    }

    /**
     * Serializes the given {@link SKTree} to a String.
     * 
     * @param sk
     *            SKTree
     * @return serialization result
     */
    public static String toString(final SKTree sk) {
        final StringWriter w = new StringWriter();
        final SKWriter skw = new SKWriter(w);
        try {
            skw.write(sk);
            w.close();
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        return w.toString();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
