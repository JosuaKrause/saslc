package xi.go;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Stack;

import xi.go.cst.CstSKParser;
import xi.go.cst.Thunk;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Value;

/**
 * Front end of the virtual machine.
 * 
 * @author Leo
 * 
 */
public final class VM {

    /** UTF-8 charset instance. */
    public static final Charset UTF8 = Charset.forName("UTF-8");

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
    public static void run(final Thunk[] prog, final Writer out)
            throws IOException {
        Thunk.pushes = Thunk.reductions = 0;
        out.append("Result: ");

        final Stack<Thunk> stack = new Stack<Thunk>();
        final BitSet inStr = new BitSet();
        int first = -1;
        stack.push(prog[0]);
        prog[0] = null; // no more reference to the root node

        do {
            final Value val = stack.pop().wHNF();
            final int depth = stack.size();
            if (!val.isList()) {
                // atomic value
                out.append(val.toString());
            } else if (val != List.EMPTY) {
                // CONS cell
                final Thunk head = val.getHead();
                final boolean str = head.wHNF().isChar(), old = inStr
                        .get(depth);
                inStr.set(depth, str);

                if (first < depth) {
                    // first element
                    first++;
                    out.append(str ? '"' : '[');

                } else if (!old) {
                    out.append(str ? "] ++ \"" : ",");
                } else if (!str) {
                    out.append("\" ++ [");
                }
                stack.push(val.getTail());
                stack.push(head);
            } else {
                // empty list
                if (first < depth) {
                    out.append("[]");
                } else {
                    out.append(inStr.get(depth) ? '"' : ']');
                    first--;
                }
            }
        } while (!stack.isEmpty());

        out.append("\nPushes:     " + Thunk.pushes);
        out.append("\nReductions: " + Thunk.reductions + "\n");
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

        VM.run(main, new OutputStreamWriter(System.out, UTF8));
    }

    /**
     * Prints a usage message and exits.
     */
    private static final void usage() {
        System.err.println("Usage: sk <sk_file> <arg>...");
        System.exit(1);
    }

}
