package xi.linker;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import xi.ast.Expr;
import xi.ast.Module;
import xi.ast.Name;
import xi.ast.Node;
import xi.go.VM;
import xi.go.cst.CstSKParser;
import xi.go.cst.Thunk;
import xi.lexer.Lexer;
import xi.parser.Parser;
import xi.sk.SKWriter;
import xi.util.IOUtils;

/**
 * Reads a SASL makefile and compiles the given files.
 * 
 * @author Joschi
 * 
 */
public class Make {

    /** Keyword for the start symbol. */
    public static final String START = "START:";
    /** Standard start symbol. */
    public static final String MAIN = "main";

    /** Comment delimiter. */
    public static final String COMMENT = "#";

    /** SASL file extension. */
    public static final String SASL = ".sasl";
    /** SKLib extension. */
    public static final String SK_LIB = ".sklib";
    /** SK extension. */
    public static final String SK = ".sk";
    /** SKMake extension. */
    public static final String MAKE = ".smake";

    /** Sharing flag. */
    private final boolean shared;
    /** Flag for forcing recreation of all files. */
    private final boolean force;

    /** Number of compiling threads. */
    private final int jobs;

    /**
     * Constructor.
     * 
     * @param cst
     *            flag for common subexpression elimination
     * @param force
     *            flag for forcing recreation of all files
     * @param jobs
     *            number of compiling threads
     */
    public Make(final boolean cst, final boolean force, final int jobs) {
        this.force = force;
        this.jobs = jobs;
        shared = cst;
    }

    /**
     * Removes the given extension from the given file name, if it exists.
     * 
     * @param file
     *            file name
     * @param ext
     *            extension
     * @return fine name without extension
     */
    private static String removeExt(final String file, final String ext) {
        String f = file;
        if (f.endsWith(ext)) {
            f = f.substring(0, f.length() - ext.length());
        }
        return f;
    }

    /**
     * Reads an SMake-file, setting the options from it.
     * 
     * @param s
     *            Scanner for the SMake file
     * @param files
     *            paths paths that should be included
     * @return start symbol
     * @throws IllegalArgumentException
     * @throws IOException
     */
    protected String readMake(final Scanner s, final List<File> files)
            throws IllegalArgumentException, IOException {
        String start = null;
        while (s.hasNextLine()) {
            String line = s.nextLine().trim();
            if (line.contains(COMMENT)) {
                final String[] arr = line.split(COMMENT);
                if (arr.length == 0) {
                    continue;
                }
                line = arr[0].trim();
            }
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith(START)) {
                final String tmpstart = line.substring(START.length()).trim();
                if (start != null) {
                    throw new IllegalArgumentException(
                            "duplicate start-symbol definition: '" + start
                                    + "' '" + tmpstart + "'");
                }
                if (tmpstart.isEmpty()) {
                    throw new IllegalArgumentException(
                            "illegal start-symbol: '" + tmpstart + "'");
                }
                start = tmpstart;
                continue;
            }
            final File f = new File(line);
            if (!f.exists()) {
                throw new IOException("file: '" + f + "' does not exist!");
            }
            if (f.isDirectory()) {
                addDir(f, files);
            } else {
                files.add(f);
            }
        }
        return start == null ? MAIN : start;
    }

    /**
     * Adds all files in the given directory, recursively.
     * 
     * @param f
     *            directory
     * @param files
     *            list of files to include
     */
    private void addDir(final File f, final List<File> files) {
        if (f.isFile()) {
            if (f.getName().endsWith(SASL)) {
                files.add(f);
            }
            return;
        }
        if (f.isDirectory()) {
            for (final File child : f.listFiles()) {
                addDir(child, files);
            }
        }
    }

    /**
     * Compiles the given SASL file.
     * 
     * @param file
     *            SASL file
     * @return {@code true}, if no compilation was necessary, {@code false}
     *         otherwise
     * @throws Exception
     *             if anything went wrong
     */
    protected boolean compile(final File file) throws Exception {
        final File newFile = new File(removeExt(file.getCanonicalPath(), SASL)
                + SK_LIB);
        // only compile when necessary or when forced...
        if (!force && newFile.exists()
                && newFile.lastModified() > file.lastModified()) {
            return true;
        }
        final Parser p = new Parser(new Lexer(IOUtils.utf8Reader(file)));
        final Node n = p.parseValue().unLambda();

        final SKWriter sk = new SKWriter(IOUtils.utf8Writer(newFile));
        sk.write(n);
        sk.close();
        return false;
    }

    /**
     * Compiles all files in the given list.
     * 
     * @param files
     *            files to compile
     * @throws Exception
     *             if anything went wrong
     */
    private void compileAll(final List<File> files) throws Exception {
        if (jobs <= 1) {
            for (final File f : files) {
                System.err.print(f);
                final boolean already = compile(f);
                System.err.println(already ? " [not modified]" : " [compiled]");
            }
            return;
        }
        System.err.println("Using " + jobs + " Threads...");
        final Queue<File> queue = new ConcurrentLinkedQueue<File>(files);
        final CompilerJob[] j = new CompilerJob[jobs];
        for (int i = 0; i < j.length; ++i) {
            j[i] = new CompilerJob(queue);
        }
        for (final Thread t : j) {
            t.start();
        }
        boolean good = true;
        for (final CompilerJob t : j) {
            t.join();
            good = good && t.good;
        }
        if (!good) {
            throw new Exception("Some errors occured during compilation!");
        }
    }

    /**
     * Links the definitions in the given SASL files, resulting in a single SK
     * expression, starting from the given start symbol.
     * 
     * @param files
     *            SASL files to link
     * @param start
     *            start symbol
     * @param out
     *            SK file to rwite to
     * @throws IOException
     *             I/O exception
     */
    protected void link(final List<File> files, final String start,
            final File out) throws IOException {
        final List<Reader> inputs = new ArrayList<Reader>(files.size());
        for (final File file : files) {
            inputs.add(new FileReader(removeExt(file.getCanonicalPath(), SASL)
                    + SK_LIB));
        }
        final Linker linker = new Linker(inputs);
        final Expr linked = linker.link(start);
        final Module mod = new Module(false);
        mod.addDefinition(Name.valueOf(MAIN), linked);

        final SKWriter sk = new SKWriter(new FileWriter(out));
        sk.write(mod);
        sk.close();
    }

    /**
     * Runs the given SMake file.
     * 
     * @param in
     *            SMake configuration
     * @throws IOException
     *             I/O exception
     */
    protected void run(final File in) throws IOException {
        final Thunk[] main = { null };

        new CstSKParser(shared) {
            @Override
            protected void def(final String name, final Thunk body) {
                if (main[0] != null || !name.equals("main")) {
                    throw new IllegalArgumentException("A linked SK file "
                            + "should contain only one 'main' function.");
                }
                main[0] = body;
            }
        }.read(IOUtils.utf8Reader(in));

        if (main[0] == null) {
            throw new IllegalArgumentException("No main method.");
        }

        VM.run(main, new OutputStreamWriter(System.out));
    }

    /**
     * Executes the given configuration.
     * 
     * @param makefile
     *            SMake configuration
     * @param run
     *            whether the SK expression should be executed
     * @throws Exception
     *             in case of fire
     */
    public void make(final File makefile, final boolean run) throws Exception {
        try {
            final List<File> files = new LinkedList<File>();
            System.err.println("Reading make-file: " + makefile);
            final String start = readMake(new Scanner(makefile), files);
            System.err.println("Starting symbol: '" + start + "'");
            System.err.println("Compiling...");
            compileAll(files);
            System.err.println("Linking...");
            final File linked = new File(removeExt(makefile.getCanonicalPath(),
                    MAKE)
                    + SK);
            System.err.println(linked
                    + (linked.exists() ? " [overwriting]" : " [linking]"));
            link(files, start, linked);
            System.err.println("Done!");
            if (run) {
                System.err.println("Executing...");
                run(linked);
            }
        } catch (final Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Compiler thread.
     * 
     * @author Josua Krause
     */
    private class CompilerJob extends Thread {
        /** Queue of files to compile. */
        private final Queue<File> queue;
        /** Error flag. */
        public boolean good;

        /**
         * Constructor.
         * 
         * @param queue
         *            files to compile
         */
        public CompilerJob(final Queue<File> queue) {
            this.queue = queue;
            good = true;
        }

        @Override
        public void run() {
            File f;
            while ((f = queue.poll()) != null) {
                try {
                    final boolean already = compile(f);
                    System.err.println(f
                            + (already ? " [not modified]" : " [compiled]"));
                } catch (final Exception e) {
                    synchronized (System.err) {
                        System.err.println(f + " [error]");
                        e.printStackTrace();
                    }
                    good = false;
                }
            }
        }
    }

}
