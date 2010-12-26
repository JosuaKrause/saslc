package xi.go.cst.stefan;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import stefan.CommonNode;
import stefan.Draw;
import xi.go.cst.Thunk;

public class Outputter {

    private static Thunk MAIN = null;

    private static String prefix = null;

    /**
     * Generates dotty output, when main is non <code>null</code>.
     * 
     * @param main
     *            The main Thunk.
     * @param namePrefix
     *            The name prefix for the generated output.
     */
    public static void setVerboseMode(final Thunk main, final String namePrefix) {
        prefix = namePrefix;
        MAIN = main;
    }

    public static boolean isVerboseMode() {
        return MAIN != null;
    }

    private static int draws = 0;

    public static void draw(final Thunk curr, final Stack<Thunk> stack) {
        if (!isVerboseMode()) {
            return;
        }
        CSTree.clear();
        final CommonNode main = CSTree.cached(MAIN);
        final Stack<CommonNode> s = new Stack<CommonNode>();
        s.push(CSTree.cached(curr));
        // reverse and no main
        for (int i = stack.size() - 1; i >= 0; --i) {
            final Thunk t = stack.elementAt(i);
            if (t == MAIN) {
                continue;
            }
            s.push(CSTree.cached(t));
        }
        try {
            Draw.state(main, s, prefix + (draws++) + ".dot");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void runDotty(final String out) throws IOException,
            InterruptedException {
        System.out.println();
        final File dir = new File(out).getParentFile();
        final FilenameFilter ff = new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".dot");
            }
        };
        for (final File file : dir.listFiles(ff)) {
            execDotty(file, dir);
        }
    }

    private static void execDotty(final File file, final File dir)
            throws IOException, InterruptedException {
        final String s = file.getName();
        final Process proc = Runtime.getRuntime().exec(
                new String[] { "dot.exe", "-Gcharset=utf8", "-Tpdf", s }, null,
                dir);
        final BufferedInputStream b = new BufferedInputStream(proc
                .getInputStream());
        int c;
        final OutputStream fw = new BufferedOutputStream(new FileOutputStream(
                new File(file.toString() + ".pdf")));
        while ((c = b.read()) != -1) {
            fw.write(c);
        }
        fw.close();
        b.close();
        final InputStream err = proc.getErrorStream();
        int chr;
        while ((chr = err.read()) != -1) {
            System.err.write(chr);
        }
        System.err.flush();
        System.out.println(s + " " + proc.waitFor());
    }
}
