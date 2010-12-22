package stefan;

import java.io.InputStreamReader;
import java.io.Reader;

import xi.ast.Node;
import xi.ast.stefan.LazyTree;
import xi.ast.stefan.SharedTree;
import xi.lexer.Lexer;
import xi.parser.Parser;

class TestParser {

	public static void main(final String[] args) throws Exception {
		if (args.length > 2) {
			System.err.println("Usage: [--shared]  [<destination>]");
			System.err.println("shared: reveal shared nodes");
			System.err.println("destination: a file to write to");
			System.err.println("    if no such file is given");
			System.err.println("    the std-output will be used");
			return;
		}
		int pos = 0;
		final Reader r = new InputStreamReader(System.in);
		final Lexer lex = new Lexer(r);
		final Parser p = new Parser(lex);
		CommonNode node;
		final Node n = p.parseValue().unLambda();
		if (args.length > 0 && args[0].equals("--shared")) {
			node = SharedTree.get(n);
			++pos;
		} else {
			node = LazyTree.create(n);
		}
		Draw.module(node.getDefs(), args.length > pos ? args[pos] : null);
	}

}