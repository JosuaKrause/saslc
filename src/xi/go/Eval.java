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
import xi.lexer.Lexer;
import xi.parser.Parser;

public class Eval extends AbstractInterpreter {

	private Thunk main = null;

	@Override
	protected void read() {
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
				fTable.put(function, stack.pop());
				continue;
			case '"':
				stack.push(Thunk.listFromStr(getString()));
				continue;
			case '<':
				stack.push(Ref.get(getName()));
				continue;
			case '@':
				final Thunk left = stack.pop();
				final Thunk right = stack.pop();
				stack.push(Thunk.app(left, right));
				break;
			case 'T':
				stack.push(Bool.TRUE_THUNK);
				break;
			case 'F':
				stack.push(Bool.FALSE_THUNK);
				break;
			case '_':
				stack.push(List.EMPTY_THUNK);
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
				final Thunk fun = Function.forChar(c);
				if (fun == null) {
					throw new IllegalStateException("Illegal character: " + c);
				}
				stack.push(fun);
			}
			next();
		}
		if (!fTable.containsKey("main")) {
			throw new IllegalArgumentException("No main method found.");
		}

		for (final Entry<String, Thunk> e : fTable.entrySet()) {
			final Thunk th = e.getValue();
			if (th.getNode() instanceof Ref) {
				final String name = th.getNode().toString();
				if (!fTable.containsKey(name)) {
					throw new IllegalArgumentException("Function '" + name
							+ "not found.");
				}
				if (!name.equals(e.getKey())) {
					e.setValue(fTable.get(name));
				}
			}
		}
		main = fTable.get("main");
		System.out.println(main);
		main = main.link(fTable);
		fTable.clear();
	}

	public Thunk getMain() {
		return main;
	}

	public static void main(final String[] args) throws Exception {
		final Reader r;
		if (args.length == 0) {
			r = new InputStreamReader(System.in, "UTF-8");
		} else {
			final GlueReader g = new GlueReader();
			for (final String arg : args) {
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

		final Node n = p.parseValue().unLambda();
		final CommonNode node = LazyTree.create(n);
		Cout.module(node, writer);

		final Eval e = new Eval();
		e.read(new StringReader(writer.toString()));
		final Thunk main = e.getMain();
		final PrintWriter w = new PrintWriter(System.out);
		try {
			main.eval(w);
			w.flush();
		} catch (final Exception io) {
			io.printStackTrace();
		} finally {
			w.close();
		}
	}

}
