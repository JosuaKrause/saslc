package xi.lexer;

import java_cup.runtime.Symbol;
import xi.parser.Terminal;
import static xi.util.StringUtils.*;

/*
 * WARNING: This file is auto generated! Do NOT edit this file!
 * Edit flex/subsasl.flex instead.
 */

@SuppressWarnings("all")
%%
   
/* -----------------Options and Declarations Section----------------- */
   
/* 
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java. 
*/

%public
%final
%class Lexer

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column

%unicode
    
/* 
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup

%eofval{
    return symbol(Terminal.EOF);
%eofval}
   
/*
  Declarations
   
  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.  
*/
%{

    // the symbol table - will be created when needed
    private static java.util.Map<Integer, String> symTable = null;

    /**
     * Gets the name of the given symbol.
     *
     * @param sym
     *            The symbols value.
     * @return The name of the symbol.
     */
    public static String getSymbolName(final int sym) {
    	if (sym == Terminal.error) {
			return "unrecognized token";
		}
        if (symTable == null) {
            symTable = new java.util.HashMap<Integer, String>();
            final java.lang.reflect.Field fs[] = Terminal.class.getFields();
            for (final java.lang.reflect.Field f : fs) {
                try {
                    symTable.put(f.getInt(null), f.getName());
                } catch (final Exception e) {
                    // ignore access restrictions...
                }
            }
        }
        return symTable.get(sym);
    }

	public static String getPrettySymbol(final Symbol s) {
		final String name = getSymbolName(s.sym);
		final Object value = s.value;
		return name + (value != null ? "[" + value.toString() + "]" : "");
	}

	public static String getPosition(final Symbol s) {
		return "line " + (s.left + 1) + " column " + (s.right + 1);
	}

	/**
	 * Runs the scanner on input files.
	 * 
	 * This main method is the debugging routine for the scanner. It prints
	 * debugging information about each returned token to System.out until the
	 * end of file is reached, or an error occured.
	 * 
	 * @param argv
	 *            the command line, contains the filenames to run the scanner
	 *            on.
	 */
	public static void main(final String argv[]) {
		if (argv.length == 0) {
			System.out.println("Usage : java Lexer <inputfile>");
			return;
		}
		for (final String element : argv) {
			Lexer scanner = null;
			try {
				scanner = new Lexer(new java.io.FileReader(element));
				while (!scanner.zzAtEOF) {
					final Symbol sym = scanner.next_token();
					System.out.println(getPrettySymbol(sym));
				}
			} catch (final java.io.FileNotFoundException e) {
				System.out.println("File not found : \"" + element + "\"");
			} catch (final java.io.IOException e) {
				System.out
						.println("IO error scanning file \"" + element + "\"");
				System.out.println(e);
			} catch (final Exception e) {
				System.out.println("Unexpected exception:");
				e.printStackTrace();
			}
		}
	}

	/*
	 * To create a new java_cup.runtime.Symbol with information about the
	 * current token, the token will have no value in this case.
	 */
	private Symbol symbol(final int type) {
		return new Symbol(type, yyline, yycolumn);
	}

    /*
     * Also creates a new java_cup.runtime.Symbol with information about the
     * current token, but this object has a value.
     */
    private Symbol symbol(final int type, final Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
    
%}
   

/*
  Macro Declarations
  
  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.  
*/
   
/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or line feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment        = #[^\r\n]*

/* literals */
num             = [0-9]+
bool            = true|false
id              = [a-zA-Z_ξ][a-zA-Z_0-9ξ']*
string          = \"([^\"\\\r\n]|\\\'|\\\"|\\\\|\\t|\\f|\\b|\\r|\\n)*\"
character		= \'([^\'\\\r\n]|\\\'|\\\"|\\\\|\\t|\\f|\\b|\\r|\\n)\'
errstr          = \"[^\"]+\"|\'[^\']+\'

%%
/* ------------------------Lexical Rules Section---------------------- */
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */
   
   /* YYINITIAL is the state at which the lexer begins scanning.  So
   these regular expressions will only be matched if the scanner is in
   the start state YYINITIAL. */
   
<YYINITIAL> {
    {WhiteSpace}    { break; }
    {Comment}       { break; }

    /* infix operators */
    "+"             { return symbol(Terminal.PLUS); }
    "-"             { return symbol(Terminal.MINUS); }
    "*"             { return symbol(Terminal.TIMES); }
    "/"             { return symbol(Terminal.DIV); }
    "%"             { return symbol(Terminal.MOD); }
    "=="            { return symbol(Terminal.EQ); }
    "!="            { return symbol(Terminal.NE); }
    "<"             { return symbol(Terminal.LT); }
    ">"             { return symbol(Terminal.GT); }
    "<="            { return symbol(Terminal.LE); }
    ">="            { return symbol(Terminal.GE); }
    "&"             { return symbol(Terminal.AND); }
    "|"             { return symbol(Terminal.OR); }
    ":"             { return symbol(Terminal.CONS); }
    "@"             { return symbol(Terminal.APP); }
    "!>"            { return symbol(Terminal.SEQ); }
    "."             { return symbol(Terminal.COMP); }
    /* prefix operators */
    "!"             { return symbol(Terminal.NOT); }    
    "~"             { return symbol(Terminal.UMINUS); }
    /* parenthesis */
    "["             { return symbol(Terminal.BRACK_L); }    
    "]"             { return symbol(Terminal.BRACK_R); }
    "("             { return symbol(Terminal.PAR_L); }
    ")"             { return symbol(Terminal.PAR_R); }
    "{"             { return symbol(Terminal.BRACE_L); }
    "}"             { return symbol(Terminal.BRACE_R); }

    /* keywords */
    "if"            { return symbol(Terminal.IF); }    
    "then"          { return symbol(Terminal.THEN); }
    "else"          { return symbol(Terminal.ELSE); }
    "let"           { return symbol(Terminal.LET); }
    "in"            { return symbol(Terminal.IN); }
    ","             { return symbol(Terminal.COMMA); }
    "="             { return symbol(Terminal.DEF); }
    ";"             { return symbol(Terminal.SEMI); }
    "->"            { return symbol(Terminal.TO); }

    /* literals */
    {num}           { return symbol(Terminal.NUM, Integer.valueOf(yytext())); }
    {bool}          { return symbol(Terminal.BLN, Boolean.valueOf(yytext())); }
    {id}            { return symbol(Terminal.NAME, yytext()); } 
    {string}        { return symbol(Terminal.STR, decode(yytext())); }
    {character}		{ return symbol(Terminal.CHAR, charDecode(yytext())); }
    {errstr}        { return symbol(Terminal.error, yytext()); }
    
}


/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                 { return symbol(Terminal.error, yytext()); }
