package xi.sk;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import xi.ast.Node;
import xi.lexer.Lexer;
import xi.parser.Parser;

public class Saslc {

    public void compile(final File in, final File out) throws Exception {
        final Parser p = new Parser(new Lexer(new FileReader(in)));
        final Node n = p.parseValue().unLambda();
        final SKWriter sk = new SKWriter(new PrintWriter(out));
        sk.write(n);
        sk.close();
    }

    public static void main(final String[] args) {
        // TODO:
    }
}
