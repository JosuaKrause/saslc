package xi.go;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import xi.go.cst.CstSKParser;
import xi.go.cst.Thunk;

/**
 * Front end of the virtual machine.
 * 
 * @author Leo
 * 
 */
public class VM {

    /** UTF-8 charset instance. */
    public static final Charset UTF8;
    static {
        UTF8 = Charset.forName("UTF-8");
    }

    /**
     * Runs the given SK program on the VM and writes the output to the given
     * {@link Writer}.
     * 
     * @param prog
     *            program to run
     * @param out
     *            output writer
     * @throws IOException
     *             from the Writer
     */
    public static void run(final Thunk prog, final Writer out)
            throws IOException {
        Thunk.pushes = Thunk.reductions = 0;
        out.append("Result: ");
        prog.eval(out);
        out.append("\nPushes:     " + Thunk.pushes);
        out.append("\nReductions: " + Thunk.reductions);
        out.flush();
    }

    /**
     * Test method.
     * 
     * @param args
     *            command line arguments
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        if (args.length == 0) {
            usage();
        }

        final File f = new File(args[0]);
        if (!f.canRead()) {
            usage();
        }

        final Thunk[] main = { null };

        new CstSKParser() {
            @Override
            protected void def(final String name, final Thunk body) {
                if (main[0] != null || !name.equals("main")) {
                    throw new IllegalArgumentException("A linked SK file "
                            + "should only contain one 'main' function.");
                }
                main[0] = body;
            }
        }.read(new InputStreamReader(new FileInputStream(f), UTF8));

        if (main[0] == null) {
            throw new IllegalArgumentException("No main method.");
        }

        VM.run(main[0], new OutputStreamWriter(System.out, UTF8));
    }

    /**
     * Prints a usage message and exits.
     */
    private static final void usage() {
        System.err.println("Usage: sk <sk_file> <arg>...");
        System.exit(1);
    }

}
