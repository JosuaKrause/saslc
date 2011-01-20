package xi.go;

import java.io.IOException;
import java.io.Writer;
import java.util.BitSet;
import java.util.Stack;
import java.util.logging.Logger;

import xi.go.cst.Thunk;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Value;
import xi.util.Logging;

/**
 * Front end of the virtual machine.
 * 
 * @author Leo
 * 
 */
public final class VM {

    /** Logger. */
    static final Logger log = Logging.getLogger(VM.class);

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
            out.flush();
        } while (!stack.isEmpty());

        out.append("\nPushes:     " + Thunk.pushes);
        out.append("\nReductions: " + Thunk.reductions + "\n");
        out.flush();
    }

}
