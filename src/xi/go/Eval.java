package xi.go;

import static xi.go.cst.Thunk.num;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Stack;
import java.util.Map.Entry;

import stefan.CommonNode;
import stefan.Cout;
import xi.ast.Node;
import xi.ast.stefan.LazyTree;
import xi.go.cst.Thunk;
import xi.go.cst.prim.Bool;
import xi.go.cst.prim.Function;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Ref;
import xi.go.cst.stefan.Outputter;
import xi.lexer.Lexer;
import xi.parser.Parser;

/**
 * Evaluator.
 * 
 * @author Leo
 */
public class Eval extends AbstractInterpreter {

    /** Main thunk. */
    private Thunk main = null;

    @Override
    protected final void read() {
        final Stack<Thunk> stack = new Stack<Thunk>();
        while (hasNext()) {
            final char c = current();
            switch (c) {
            case 'i':
                next();
                stack.push(num(nextInt()));
                continue;
            case '=':
                final String function = getDef();
                getFunctionTable().put(function, stack.pop());
                continue;
            case '"':
                stack.push(Thunk.listFromStr(getString()));
                continue;
            case '<':
                stack.push(Ref.get(getName()));
                continue;
            case '\'':
                final char chr = next();
                int cp;
                if (Character.isHighSurrogate(chr)) {
                    cp = Character.toCodePoint(chr, next());
                } else {
                    cp = chr;
                }
                stack.push(Thunk.chr(cp));
                break;
            case '@':
                final Thunk left = stack.pop();
                final Thunk right = stack.pop();
                stack.push(Thunk.app(left, right));
                break;
            case 'T':
                stack.push(Bool.TRUE.thunk);
                break;
            case 'F':
                stack.push(Bool.FALSE.thunk);
                break;
            case '_':
                stack.push(List.EMPTY.thunk);
                break;
            case ',':
                final Thunk x = stack.pop();
                final Thunk xs = stack.pop();
                stack.push(List.get(x, xs));
                break;
            case ' ':
            case '\r':
            case '\n':
            case '\t':
                // ignore new-lines or white-spaces
                break;
            default:
                final Function.Def fun = Function.forChar(c);
                if (fun == null) {
                    throw new IllegalStateException("Illegal character: " + c);
                }
                stack.push(fun.thunk);
            }
            next();
        }
        if (!getFunctionTable().containsKey("main")) {
            throw new IllegalArgumentException("No main method found.");
        }

        for (final Entry<String, Thunk> e : getFunctionTable().entrySet()) {
            final Thunk th = e.getValue();
            if (th.isRef()) {
                final String name = th.toString();
                if (!getFunctionTable().containsKey(name)) {
                    throw new IllegalArgumentException("Function '" + name
                            + "' not found.");
                }
                if (!name.equals(e.getKey())) {
                    e.setValue(getFunctionTable().get(name));
                }
            }
        }
        main = getFunctionTable().get("main");
        System.out.println("SK code: " + main);
        main = main.link(getFunctionTable());
        getFunctionTable().clear();
    }

    /**
     * Getter for the main node.
     * 
     * @return the main node
     */
    public final Thunk getMain() {
        return main;
    }

    /**
     * Main method for the interpreter.
     * 
     * @param args
     *            currently unused
     * @throws Exception
     *             if anything goes wrong
     */
    public static void main(final String[] args) throws Exception {
        final Reader r;
        String out = null;
        if (args.length == 0) {
            r = new InputStreamReader(System.in, "UTF-8");
        } else {
            final GlueReader g = new GlueReader();
            int output = 0;
            for (final String arg : args) {
                if (output == 0) {
                    if (arg.equals("-out")) {
                        output = 1;
                        continue;
                    }
                    output = 2;
                } else if (output == 1) {
                    out = arg;
                    output = 2;
                    continue;
                }
                final File f = new File(arg);
                if (f.exists()) {
                    g.addReader(new FileReader(f));
                } else {
                    g.addReader(new StringReader(arg));
                }
            }
            r = g;
        }
        final Lexer lex = new Lexer(r);
        final Parser p = new Parser(lex);
        final StringWriter writer = new StringWriter();

        final Node mod = p.parseValue();
        final Node n = mod.unLambda();
        final CommonNode node = LazyTree.create(n);
        Cout.module(node, writer);

        Eval e = new Eval();
        e.read(new StringReader(writer.toString()));
        final Thunk main = e.getMain();
        if (out != null) {
            Outputter.setVerboseMode(main, out);
        }
        e = null;

        final PrintWriter w = new PrintWriter(System.out);
        w.append("Result: ");
        try {
            main.eval(w);
            w.append("\nReductions: " + Thunk.reductions).flush();
        } catch (final Exception io) {
            io.printStackTrace();
        }
        if (out != null) {
            Outputter.runDotty(out);
        }
    }

}
