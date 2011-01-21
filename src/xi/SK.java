package xi;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import xi.go.VM;
import xi.go.cst.CstSKParser;
import xi.go.cst.Thunk;
import xi.util.IOUtils;
import xi.util.Logging;

public class SK {

    /**
     * Test method.
     * 
     * @param args
     *            command line arguments
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {

        if (args.length == 0) {
            usage("");
        }

        int pos = -1;
        boolean shared = false;
        while (++pos < args.length - 1) {
            final String arg = args[pos];
            if ("-cse".equals(arg)) {
                shared = true;
            } else if ("-h".equals(arg) || "--help".equals(arg)) {
                usage("");
            } else if ("-v".equals(arg)) {
                Logging.setLevel(Level.INFO);
            } else {
                usage("Unknown parameter: '" + arg + "'\n");
            }
        }

        final File f = new File(args[pos]);
        if (!f.canRead()) {
            usage("File doesn't exist!\n");
        }

        final Thunk[] main = { null };

        new CstSKParser(shared) {
            @Override
            protected void def(final String name, final Thunk body) {
                if (main[0] != null || !name.equals("main")) {
                    throw new IllegalArgumentException("A linked SK file "
                            + "should only contain one 'main' function.");
                }
                main[0] = body;
            }
        }.read(IOUtils.utf8Reader(f));

        if (main[0] == null) {
            throw new IllegalArgumentException("No main method.");
        }

        System.out.println("SK code: " + main[0]);
        VM.run(main, IOUtils.STDOUT);
    }

    /**
     * Prints a usage message and exits.
     * 
     * @param desc
     *            error description
     */
    private static final void usage(final String desc) {
        System.err.println(desc + "Usage: sk [-cse] [-v] <sk_file> <arg>...\n"
                + "\t-cse: perform common subexpression elimination\n"
                + "\t-v:   verbose mode, prints info output to STDERR");
        System.exit(1);
    }

}
