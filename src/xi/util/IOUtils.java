package xi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Utility methods for input and output.
 * 
 * @author Leo Woerteler
 */
public final class IOUtils {

    /** Hidden default constructor. */
    private IOUtils() {
        // void
    }

    /** UTF-8 charset instance. */
    public static final Charset UTF8 = Charset.forName("UTF-8");

    /** Writer for the standard output. */
    public static final Writer STDOUT = new OutputStreamWriter(System.out);

    /** Reader for the standard input. */
    public static final Reader STDIN = new InputStreamReader(System.in);

    /**
     * Creates a {@link Reader} that reads from the given file, interpreting the
     * read bytes using the UTF-8 charset.
     * 
     * @param f
     *            file to read from
     * @return Reader for the given file
     * @throws IOException
     *             if the file doesn't exist
     */
    public static Reader utf8Reader(final File f) throws IOException {
        return new InputStreamReader(new FileInputStream(f), UTF8);
    }

    /**
     * Creates a {@link Writer} that writes to the given file, converting chars
     * to bytes using the UTF-8 charset.
     * 
     * @param f
     *            file to write to
     * @return Writer for the given file
     * @throws IOException
     *             if the file couldn't be opened for writing
     */
    public static Writer utf8Writer(final File f) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(f), UTF8);
    }
}
