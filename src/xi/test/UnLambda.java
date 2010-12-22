package xi.test;

import java.io.FileInputStream;

import xi.ast.Module;
import xi.lexer.Lexer;
import xi.parser.Parser;

public class UnLambda {

	public static void main(final String[] args) throws Exception {
		final Module nd = new Parser(new Lexer(new FileInputStream(
				"test/prelude.sasl"))).parseValue();
		System.out.println(nd);
		System.out.println(nd.unLambda());
	}
}
