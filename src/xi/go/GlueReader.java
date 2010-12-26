package xi.go;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A {@link Reader} implementation that concatenates the input from multiple
 * readers.
 * 
 * @author Leo
 * @author Joshi
 */
public class GlueReader extends Reader {

    /** Reader queue. */
    private final Queue<Reader> in;

    /** Character that's put between the input from two readers. */
    private final char glue;

    /** Constructor with a newline character '\n' as glue. */
    public GlueReader() {
        this('\n');
    }

    /**
     * Constructor taking the glue character.
     * 
     * @param g
     *            glue character
     */
    public GlueReader(final char g) {
        glue = g;
        in = new LinkedList<Reader>();
    }

    /**
     * Adds the given reader to the reader queue.
     * 
     * @param r
     *            reader to add to the queue
     */
    public final void addReader(final Reader r) {
        in.add(r);
    }

    @Override
    public final void close() throws IOException {
        while (!in.isEmpty()) {
            in.poll().close();
        }
    }

    @Override
    public final int read() throws IOException {
        if (in.isEmpty()) {
            return -1;
        }
        final int res = in.peek().read();
        if (res != -1) {
            return res;
        }
        in.poll().close();
        return glue;
    }

    @Override
    public final int read(final char[] cbuf, final int off, final int len)
            throws IOException {
        if (in.isEmpty()) {
            return -1;
        }
        for (int pos = 0; pos < len; ++pos) {
            final int c = read();
            if (c == -1) {
                return pos;
            }
            cbuf[pos] = (char) c;
        }
        return len;
    }

}
