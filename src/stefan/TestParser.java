package stefan;

import java.io.InputStreamReader;
import java.io.Reader;

import xi.ast.Node;
import xi.ast.stefan.LazyTree;
import xi.lexer.Lexer;
import xi.parser.Parser;

class TestParser {

    public static void main(final String[] args) throws Exception {
        if (args.length > 2) {
            System.err.println("Usage: [<destination>]");
            System.err.println("destination: a file to write to");
            System.err.println("    if no such file is given");
            System.err.println("    the std-output will be used");
            return;
        }
        final Reader r = new InputStreamReader(System.in);
        final Lexer lex = new Lexer(r);
        final Parser p = new Parser(lex);
        CommonNode node;
        final Node n = p.parseValue().unLambda();
        node = LazyTree.create(n);
        Draw.module(node.getDefs(), args.length > 0 ? args[0] : null);
    }

}
