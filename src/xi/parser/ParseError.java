package xi.parser;

import java_cup.runtime.Symbol;
import xi.ast.Expr;
import xi.ast.Name;

public class ParseError {

	public static final Expr expr = Name.valueOf("$ERROR$");

	private final Symbol sym;

	private final String msg;

	public ParseError(final Symbol sym, final String msg) {
		this.sym = sym;
		this.msg = msg;
	}

	public Symbol getSymbol() {
		return sym;
	}

	public String getMessage() {
		return msg;
	}

}
