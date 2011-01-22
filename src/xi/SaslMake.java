package xi;

import static xi.linker.Make.COMMENT;
import static xi.linker.Make.MAIN;
import static xi.linker.Make.SASL;
import static xi.linker.Make.SK;
import static xi.linker.Make.SK_LIB;
import static xi.linker.Make.START;

import java.io.File;

import xi.linker.Make;

/**
 * The sasl_make command-line tool, making automatic builds possible.
 * 
 * @author Joschi
 */
public class SaslMake {

    /**
     * Printing the usage message.
     */
    public static void usage() {
        System.err
                .println("Usage: sasl_make [-help] [-r] [-f] [-cst] [-j <jobs>] <makefile>");
        System.err.println("-help: Shows this help");
        System.err.println("-r: Runs the program afterwards");
        System.err.println("-f: Forces a complete rebuild");
        System.err
                .println("-cst: advises the VM to use shared trees when running");
        System.err.println("-j: use concurrent compilation");
        System.err
                .println("<jobs>: the number of threads to use when compiling (default is 1)");
        System.err.println("<makefile>: the makefile to interpret");
        System.err.println("  The syntax of a makefile is fairly easy.");
        System.err.println("  Every line is a SASL file (" + SASL
                + ") or a directory containing");
        System.err.println("  them (will be scanned recursively) to compile.");
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

    /**
     * Main method.
     * 
     * @param args
     *            command-line arguments
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        int jobs = 1;
        boolean run = false;
        boolean force = false;
        boolean shared = false;
        String makeFile = null;
        try {
            for (int i = 0; i < args.length; ++i) {
                final String arg = args[i];
                if (arg.equals("-help")) {
                    usage();
                    return;
                }
                if (arg.equals("-r")) {
                    run = true;
                } else if (arg.equals("-f")) {
                    force = true;
                } else if (arg.equals("-cst")) {
                    shared = true;
                } else if (arg.equals("-j")) {
                    if (i >= args.length - 1) {
                        usage();
                        return;
                    }
                    jobs = Integer.valueOf(args[++i].trim());
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
        } catch (final Exception e) {
            e.printStackTrace();
            usage();
            return;
        }
        final Make m = new Make(shared, force, jobs);
        try {
            m.make(new File(makeFile), run);
        } catch (final Exception e) {
            System.err.println();
            e.printStackTrace();
        }
    }

}
