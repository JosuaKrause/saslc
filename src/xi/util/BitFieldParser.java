/**
 * 
 */
package xi.util;

import com.martiansoftware.jsap.ParseException;
import com.martiansoftware.jsap.StringParser;

/**
 * Parses a bit-field represented as octal integer.
 * 
 * @author Joschi
 * 
 */
public final class BitFieldParser extends StringParser {

    /** The instance. */
    private static final BitFieldParser INSTANCE = new BitFieldParser();

    /**
     * Protected
     */
    private BitFieldParser() {
        // void
    }

    /**
     * @return Returns the Parser.
     */
    public static BitFieldParser getParser() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.martiansoftware.jsap.StringParser#parse(java.lang.String)
     */
    @Override
    public Object parse(final String arg) throws ParseException {
        if (arg.equals("-1")) {
            return -1;
        }
        try {
            return Integer.parseInt(arg, 8);
        } catch (final NumberFormatException e) {
            throw new ParseException("Not an octal number!", e);
        }
    }

}
