package xi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import xi.ast.Expr;
import xi.ast.Module;
import xi.ast.Name;
import xi.linker.Linker;
import xi.linker.Make;
import xi.sk.SKWriter;

/**
 * 
 * @author Joschi
 * 
 */
public class Sasln {

    public static void main(final String[] args) throws Exception {
        String start = "main";
        final ArrayList<Reader> inputs = new ArrayList<Reader>();
        Writer out = new OutputStreamWriter(System.out);
        boolean invalid = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-help")) {
                invalid = true;
                break;
            }
            if (args[i].equals("-start")) {
                if (i == args.length - 1) {
                    invalid = true;
                    break;
                }
                start = args[++i];
            } else if ("-out".equals(args[i])) {
                if (i == args.length - 1) {
                    invalid = true;
                    break;
                }
                out = new OutputStreamWriter(new FileOutputStream(args[++i]),
                        Make.UTF8);
            } else if ("-".equals(args[i])) {
                inputs.add(new InputStreamReader(System.in));
            } else {
                final File f = new File(args[i]);
                if (!f.isFile()) {
                    invalid = true;
                    break;
                }
                inputs.add(new InputStreamReader(new FileInputStream(f),
                        Make.UTF8));
            }
        }
        if (invalid) {
            System.err.println("Usage: sasln [-help] [-start <start_sym>] "
                    + "[-out <dest_file>] <sklib>...\n"
                    + "\t<start_sym>: function name used as entry point,"
                    + " default is 'main'.\n"
                    + "\t<dest_file>: File to write to, default is STDOUT.\n"
                    + "\t<sklib>: a file containing an SK library,"
                    + " '-' means STDIN.\n");
            return;
        }
        if (inputs.isEmpty()) {
            inputs.add(new InputStreamReader(System.in));
        }
        final Linker linker = new Linker(inputs);
        final Expr linked = linker.link(start);
        final Module mod = new Module(false);
        mod.addDefinition(Name.valueOf("main"), linked);

        final SKWriter sk = new SKWriter(out);
        sk.write(mod);
        sk.close();
    }

}
