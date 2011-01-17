package xi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * Thread for concurrently producing DOT graphs.
 * 
 * @author Leo
 */
public class DOTThread extends Thread {

    /** Output directory. */
    private final File outDir;

    /** Command line. */
    private final String[] cmd;

    /** Read buffer. */
    private final char[] buffer = new char[1024];

    /** Queue of incoming DOT documents. */
    private final LinkedBlockingQueue<Task> queue;

    /**
     * Constructor for a DOTThread writing to the given output directory.
     * 
     * @param dot
     *            path to the DOT executable
     * @param fmt
     *            output format
     * @param out
     *            directory
     * @throws IOException
     *             in case of problems with the output directory
     */
    public DOTThread(final String dot, final String fmt, final File out)
            throws IOException {
        if (out.exists()) {
            if (!out.isDirectory()) {
                throw new FileNotFoundException(out + "is no directory!");
            }
        } else if (!out.mkdirs()) {
            throw new IOException("Couldn't create " + out);
        }

        outDir = out;
        cmd = new String[] { dot, "-T" + fmt, "-o", null };
        queue = new LinkedBlockingQueue<Task>();
    }

    @Override
    public final void run() {
        while (true) {
            try {
                final Task t = queue.take();

                if (t == Task.EOI || interrupted()) {
                    break;
                }

                processDot(t);
            } catch (final InterruptedException e) {
                break;
            } catch (final IOException e) {
                Logging.getLogger(getClass())
                        .log(Level.WARNING, e.getMessage());
            }
        }
    }

    /**
     * Creates the argument array for the DOT process.
     * 
     * @param name
     *            name of the output file
     * @return argument array
     */
    private String[] makeArgs(final String name) {
        final String[] c = cmd.clone();
        c[3] = new File(outDir, name).getAbsolutePath();
        return c;
    }

    /**
     * Processes a DOT document.
     * 
     * @param doc
     *            document to process
     * @throws IOException
     *             if execution fails
     * @throws InterruptedException
     *             if the thread is interrupted while waiting for the DOT
     *             process
     */
    private void processDot(final Task doc) throws IOException,
            InterruptedException {
        final String[] args = makeArgs(doc.name);
        final Process proc = Runtime.getRuntime().exec(args);

        final Writer out = new OutputStreamWriter(proc.getOutputStream(),
                "UTF-8");
        out.write(doc.dot);
        out.close();

        final StringBuilder sb = new StringBuilder();
        final Reader in = new InputStreamReader(proc.getInputStream(), "UTF-8");
        for (int len; (len = in.read(buffer)) > 0;) {
            sb.append(buffer, 0, len);
        }
        final String outStr = sb.toString().trim();
        if (!outStr.isEmpty()) {
            System.out.println(outStr);
        }

        sb.setLength(0);
        final Reader err = new InputStreamReader(proc.getErrorStream());
        for (int len; (len = err.read(buffer)) > 0;) {
            sb.append(buffer, 0, len);
        }
        final String errStr = sb.toString().trim();
        if (!errStr.isEmpty()) {
            System.err.println(errStr);
        }

        proc.waitFor();
    }

    /**
     * Adds a DOT document to the thread's internal work queue.
     * 
     * @param name
     *            name of the file
     * @param dotDoc
     *            document to add
     * @throws InterruptedException
     *             if the offering thread was interrupted while waiting for the
     *             document to be accepted
     */
    public final void addDot(final String name, final String dotDoc)
            throws InterruptedException {
        queue.put(new Task(name, dotDoc));
    }

    /**
     * Signals that there are no new documents to be suspected.
     * 
     * @throws InterruptedException
     *             if the offering thread was interrupted while waiting for the
     *             task to be completed
     */
    public final void finished() throws InterruptedException {
        queue.put(Task.EOI);
    }

    /**
     * Test main method.
     * 
     * @param args
     *            unused
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final DOTThread dt = new DOTThread("dot", "png", new File(
                "C:\\Users\\Leo\\Desktop\\bla blub"));
        dt.start();
        for (int i = 0; i < 10; i++) {
            dt.addDot("test" + i + ".png", "digraph Bla { a -> b -> c -> a; }");
        }
        dt.finished();
    }

    /**
     * Class for tasks to be processed, having a special end-of-input symbol.
     * 
     * @author Leo
     */
    private static final class Task {

        /** Name of the document. */
        private final String name;

        /** DOT document to be processed. */
        private final String dot;

        /** End-of-Input marker. */
        private static final Task EOI = new Task(null, null);

        /**
         * Constructor for new tasks.
         * 
         * @param nm
         *            document name
         * @param dt
         *            dot document
         */
        private Task(final String nm, final String dt) {
            name = nm;
            dot = dt;
        }

    }

}
