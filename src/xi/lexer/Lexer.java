/* The following code was generated by JFlex 1.4.3 on 20.01.11 14:23 */

package xi.lexer;

import java_cup.runtime.Symbol;
import xi.parser.Terminal;
import static xi.util.StringUtils.*;
import java.math.BigInteger;

/*
 * WARNING: This file is auto generated! Do NOT edit this file!
 * Edit flex/subsasl.flex instead.
 */

@SuppressWarnings("all")

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 20.01.11 14:23 from the specification file
 * <tt>flex/subsasl.flex</tt>
 */
public final class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\3\1\2\1\0\1\3\1\1\22\0\1\3\1\33\1\21"+
    "\1\4\1\0\1\31\1\36\1\17\1\46\1\47\1\27\1\25\1\54"+
    "\1\26\1\42\1\30\12\5\1\40\1\55\1\34\1\32\1\35\1\0"+
    "\1\41\32\16\1\44\1\22\1\45\1\0\1\16\1\20\1\13\1\23"+
    "\2\16\1\11\1\12\1\16\1\53\1\52\2\16\1\14\1\16\1\24"+
    "\3\16\1\7\1\15\1\6\1\10\5\16\1\50\1\37\1\51\1\43"+
    "\77\0\1\16\17\0\1\16\uff31\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\3\2\1\3\5\4\3\1\1\5\1\6"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\1\4\1\32\1\33\5\4\6\0"+
    "\1\34\1\0\1\35\1\36\1\37\1\40\1\41\1\42"+
    "\1\43\1\44\1\45\1\46\4\4\1\47\1\50\1\1"+
    "\1\51\1\1\1\52\1\53\1\54\2\0";

  private static int [] zzUnpackAction() {
    int [] result = new int[75];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\56\0\134\0\56\0\212\0\270\0\346\0\u0114"+
    "\0\u0142\0\u0170\0\u019e\0\u01cc\0\u01fa\0\u0228\0\56\0\u0256"+
    "\0\56\0\56\0\56\0\u0284\0\u02b2\0\u02e0\0\u030e\0\56"+
    "\0\56\0\56\0\56\0\u033c\0\56\0\56\0\56\0\56"+
    "\0\56\0\56\0\56\0\u036a\0\56\0\56\0\u0398\0\u03c6"+
    "\0\u03f4\0\u0422\0\u0450\0\u047e\0\u04ac\0\u04da\0\u0508\0\u0228"+
    "\0\u0536\0\56\0\u0564\0\56\0\56\0\56\0\56\0\56"+
    "\0\56\0\56\0\56\0\u0114\0\u0114\0\u0592\0\u05c0\0\u05ee"+
    "\0\u061c\0\u0114\0\56\0\u064a\0\56\0\u0678\0\u0114\0\u0114"+
    "\0\u0114\0\u0678\0\u06a6";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[75];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\2\4\1\5\1\6\1\7\2\10\1\11"+
    "\1\12\1\10\1\13\2\10\1\14\1\15\1\16\1\2"+
    "\2\10\1\17\1\20\1\21\1\22\1\23\1\24\1\25"+
    "\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35"+
    "\1\36\1\37\1\40\1\41\1\42\1\43\1\44\1\10"+
    "\1\45\1\46\60\0\1\4\53\0\1\5\2\0\53\5"+
    "\5\0\1\6\55\0\2\10\1\47\10\10\3\0\2\10"+
    "\25\0\1\10\1\50\7\0\13\10\3\0\2\10\25\0"+
    "\2\10\7\0\7\10\1\51\3\10\3\0\2\10\25\0"+
    "\2\10\7\0\6\10\1\52\4\10\3\0\2\10\25\0"+
    "\2\10\7\0\4\10\1\53\6\10\3\0\2\10\25\0"+
    "\2\10\2\0\1\54\2\55\14\54\1\0\2\54\1\56"+
    "\33\54\6\0\11\57\4\0\2\57\25\0\2\57\2\0"+
    "\1\60\2\61\16\60\1\62\1\63\33\60\35\0\1\64"+
    "\52\0\1\65\55\0\1\66\2\0\1\67\46\0\1\70"+
    "\3\0\1\71\55\0\1\72\65\0\1\73\20\0\5\10"+
    "\1\74\5\10\3\0\1\10\1\75\25\0\2\10\7\0"+
    "\3\10\1\76\7\10\3\0\2\10\25\0\2\10\7\0"+
    "\4\10\1\77\6\10\3\0\2\10\25\0\2\10\7\0"+
    "\10\10\1\100\2\10\3\0\2\10\25\0\2\10\7\0"+
    "\7\10\1\101\3\10\3\0\2\10\25\0\2\10\7\0"+
    "\1\10\1\102\11\10\3\0\2\10\25\0\2\10\2\0"+
    "\17\55\1\103\55\55\1\2\44\55\2\54\2\55\1\54"+
    "\4\55\1\104\1\55\4\54\31\55\5\0\13\57\1\105"+
    "\2\0\2\57\25\0\2\57\2\0\21\61\1\2\42\61"+
    "\2\60\2\61\1\60\4\61\1\60\1\61\1\106\3\60"+
    "\31\61\5\0\4\10\1\107\6\10\3\0\2\10\25\0"+
    "\2\10\7\0\13\10\3\0\1\10\1\110\25\0\2\10"+
    "\7\0\4\10\1\111\6\10\3\0\2\10\25\0\2\10"+
    "\7\0\10\10\1\76\2\10\3\0\2\10\25\0\2\10"+
    "\21\0\1\103\36\0\1\112\2\0\16\112\1\62\1\113"+
    "\33\112\6\0\2\112\2\0\1\112\4\0\1\112\1\0"+
    "\4\112\31\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1748];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\1\1\1\11\12\1\1\11\1\1\3\11"+
    "\4\1\4\11\1\1\7\11\1\1\2\11\5\1\6\0"+
    "\1\11\1\0\10\11\7\1\1\11\1\1\1\11\4\1"+
    "\2\0";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[75];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */

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
    


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Lexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 130) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 4: 
          { return symbol(Terminal.NAME, yytext());
          }
        case 45: break;
        case 34: 
          { return symbol(Terminal.LE);
          }
        case 46: break;
        case 2: 
          { break;
          }
        case 47: break;
        case 29: 
          { return symbol(Terminal.TO);
          }
        case 48: break;
        case 6: 
          { return symbol(Terminal.MINUS);
          }
        case 49: break;
        case 23: 
          { return symbol(Terminal.PAR_R);
          }
        case 50: break;
        case 32: 
          { return symbol(Terminal.SEQ);
          }
        case 51: break;
        case 25: 
          { return symbol(Terminal.BRACE_R);
          }
        case 52: break;
        case 13: 
          { return symbol(Terminal.GT);
          }
        case 53: break;
        case 20: 
          { return symbol(Terminal.BRACK_L);
          }
        case 54: break;
        case 12: 
          { return symbol(Terminal.LT);
          }
        case 55: break;
        case 5: 
          { return symbol(Terminal.PLUS);
          }
        case 56: break;
        case 11: 
          { return symbol(Terminal.NOT);
          }
        case 57: break;
        case 14: 
          { return symbol(Terminal.AND);
          }
        case 58: break;
        case 26: 
          { return symbol(Terminal.COMMA);
          }
        case 59: break;
        case 8: 
          { return symbol(Terminal.DIV);
          }
        case 60: break;
        case 44: 
          { return symbol(Terminal.ELSE);
          }
        case 61: break;
        case 41: 
          { final String s = yytext();
                      return symbol(Terminal.INAME, s.substring(1, s.length() - 1));
          }
        case 62: break;
        case 9: 
          { return symbol(Terminal.MOD);
          }
        case 63: break;
        case 28: 
          { return symbol(Terminal.STR, decode(yytext()));
          }
        case 64: break;
        case 43: 
          { return symbol(Terminal.THEN);
          }
        case 65: break;
        case 3: 
          { return symbol(Terminal.NUM, new BigInteger(yytext()));
          }
        case 66: break;
        case 10: 
          { return symbol(Terminal.DEF);
          }
        case 67: break;
        case 21: 
          { return symbol(Terminal.BRACK_R);
          }
        case 68: break;
        case 19: 
          { return symbol(Terminal.UMINUS);
          }
        case 69: break;
        case 27: 
          { return symbol(Terminal.SEMI);
          }
        case 70: break;
        case 37: 
          { return symbol(Terminal.IF);
          }
        case 71: break;
        case 1: 
          { return symbol(Terminal.error, yytext());
          }
        case 72: break;
        case 36: 
          { return symbol(Terminal.ELLIPSE);
          }
        case 73: break;
        case 40: 
          { return symbol(Terminal.CHAR, charDecode(yytext()));
          }
        case 74: break;
        case 33: 
          { return symbol(Terminal.FROM);
          }
        case 75: break;
        case 17: 
          { return symbol(Terminal.APP);
          }
        case 76: break;
        case 39: 
          { return symbol(Terminal.LET);
          }
        case 77: break;
        case 38: 
          { return symbol(Terminal.IN);
          }
        case 78: break;
        case 15: 
          { return symbol(Terminal.OR);
          }
        case 79: break;
        case 30: 
          { return symbol(Terminal.EQ);
          }
        case 80: break;
        case 18: 
          { return symbol(Terminal.COMP);
          }
        case 81: break;
        case 31: 
          { return symbol(Terminal.NE);
          }
        case 82: break;
        case 22: 
          { return symbol(Terminal.PAR_L);
          }
        case 83: break;
        case 16: 
          { return symbol(Terminal.CONS);
          }
        case 84: break;
        case 7: 
          { return symbol(Terminal.TIMES);
          }
        case 85: break;
        case 24: 
          { return symbol(Terminal.BRACE_L);
          }
        case 86: break;
        case 42: 
          { return symbol(Terminal.BLN, Boolean.valueOf(yytext()));
          }
        case 87: break;
        case 35: 
          { return symbol(Terminal.GE);
          }
        case 88: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              {     return symbol(Terminal.EOF);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
