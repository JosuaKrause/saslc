package xi.parser;

import java_cup.runtime.Symbol;
import xi.ast.Expr;
import xi.ast.Name;

/**
 * Thrown when a parse error occurs.
 * 
 * @author Leo Woerteler
 */
public class ParseError {

    /** Pseudo expression for error output. */
    public static final Expr expr = Name.valueOf("$ERROR$");

    /** Symbol that caused the error. */
    private final Symbol sym;
    /** Error message. */
    private final String msg;

    /**
     * Constructor.
     * 
     * @param sym
     *            offending symbol
     * @param msg
     *            error message
     */
    public ParseError(final Symbol sym, final String msg) {
        this.sym = sym;
        this.msg = msg;
    }

    /**
     * Getter for this error's symbol.
     * 
     * @return symbol
     */
    public Symbol getSymbol() {
        return sym;
    }

    /**
     * Getter for this error's message.
     * 
     * @return message
     */
    public String getMessage() {
        return msg;
    }

}
