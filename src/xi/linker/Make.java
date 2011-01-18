package xi.linker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import stefan.CommonNode;
import stefan.Cout;
import xi.ast.Expr;
import xi.ast.Module;
import xi.ast.Name;
import xi.ast.Node;
import xi.ast.stefan.LazyTree;
import xi.go.VM;
import xi.go.cst.CstSKParser;
import xi.go.cst.Thunk;
import xi.lexer.Lexer;
import xi.parser.Parser;

/**
 * Reads a SASL makefile and compiles the given files.
 * 
 * @author Joschi
 * 
 */
public class Make {

    /** UTF-8 charset instance. */
    public static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String START = "START:";

    private static final String MAIN = "main";

    // file names cannot contain #
    private static final String COMMENT = "#";

    private static final String SASL = ".sasl";

    private static final String SK_LIB = ".sklib";

    private static final String SK = ".sk";

    private static final String MAKE = ".smake";

    private final boolean shared;

    public Make(final boolean cst) {
        shared = cst;
    }

    private static String removeExt(String file, final String ext) {
        if (file.endsWith(ext)) {
            file = file.substring(0, file.length() - ext.length());
        }
        return file;
    }

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
            files.add(f);
        }
        return start == null ? MAIN : start;
    }

    protected boolean compile(final File file) throws Exception {
        final File newFile = new File(removeExt(file.getCanonicalPath(), SASL)
                + SK_LIB);
        // only compile when necessary
        if (newFile.exists() && newFile.lastModified() > file.lastModified()) {
            return true;
        }
        final Parser p = new Parser(new Lexer(new FileReader(file)));
        final Node n = p.parseValue().unLambda();
        final CommonNode node = LazyTree.create(n);
        Cout.module(node, new PrintWriter(newFile));
        return false;
    }

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

        Cout.module(LazyTree.create(mod), new FileWriter(out));
    }

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
        }.read(new InputStreamReader(new FileInputStream(in), UTF8));

        if (main[0] == null) {
            throw new IllegalArgumentException("No main method.");
        }

        VM.run(main, new OutputStreamWriter(System.out, UTF8));
    }

    public void make(final File makefile, final boolean run) throws Exception {
        try {
            final List<File> files = new LinkedList<File>();
            System.err.println("Reading make-file: " + makefile);
            final String start = readMake(new Scanner(makefile), files);
            System.err.println("Starting symbol: '" + start + "'");
            System.err.println("Compiling...");
            for (final File f : files) {
                System.err.print(f);
                final boolean already = compile(f);
                System.err.println(already ? " [not modified]" : " [compiled]");
            }
            System.err.println("Linking...");
            final File linked = new File(removeExt(makefile.getCanonicalPath(),
                    MAKE)
                    + SK);
            System.err.println(linked
                    + (linked.exists() ? " [overwriting]" : " [linking]"));
            link(files, start, linked);
            System.err.println("Done!");
            if (run) {
                run(linked);
            }
        } catch (final Exception e) {
            throw new Exception(e);
        }
    }

    public static void usage() {
        System.err.println("Usage: sasl_make [-help] [-r] [-cst] <makefile>");
        System.err.println("-help: Shows this help");
        System.err.println("-r: Runs the program afterwards");
        System.err
                .println("-cst: advises the VM to use shared trees when running");
        System.err.println("<makefile>: the makefile to interpret");
        System.err.println("  The syntax of a makefile is fairly easy.");
        System.err.println("  Every line is a SASL file (" + SASL
                + ") to compile.");
        System.err.println("  The files are linked in the order given by the");
        System.err.println("  makefile. A line starting with '" + START
                + "' defines the");
        System.err
                .println("  starting symbol. When no such line is given, the default");
        System.err.println("  " + MAIN + " is used. Everything after a "
                + COMMENT + " is");
        System.err.println("  interpreted as a comment.");
        System.err.println("Old " + SK_LIB + " files and the <makefile>" + SK
                + " will be");
        System.err
                .println("overwritten. Informations about the progress will be");
        System.err
                .println("printed to STD_ERR. The results of the program when");
        System.err.println("started with -r will be printed to STD_OUT.");
    }

    public static void main(final String[] args) {
        if (args.length == 0 || args.length > 3) {
            usage();
            return;
        }
        boolean run = false;
        boolean shared = false;
        String makeFile = null;
        for (int i = 0; i < args.length; ++i) {
            final String arg = args[i];
            if (arg.equals("-help")) {
                usage();
                return;
            }
            if (arg.equals("-r")) {
                run = true;
            } else if (arg.equals("-cst")) {
                shared = true;
            } else {
                if (makeFile != null || i != args.length - 1) {
                    usage();
                    return;
                }
                makeFile = arg;
            }
        }
        if (makeFile == null) {
            usage();
            return;
        }
        final Make m = new Make(shared);
        try {
            m.make(new File(makeFile), run);
        } catch (final Exception e) {
            System.err.println();
            e.printStackTrace();
        }
    }
}
