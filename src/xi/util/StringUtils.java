package xi.util;

/**
 * String processing utilities.
 * 
 * @author Leo
 * @author Joschi
 */
public final class StringUtils {

    /** Hidden default constructor. */
    private StringUtils() {
        // void
    }

    /** New-line string. */
    public static final String NL = System.getProperty("line.separator");

    /**
     * Escapes special characters in the given string.
     * 
     * @param str
     *            string
     * @return escaped string
     */
    public static String escape(final char[] str) {
        final StringBuilder sb = new StringBuilder(str.length);
        for (final char c : str) {
            switch (c) {
            case '\n':
                sb.append("\\n");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '"':
                sb.append("\\\"");
                break;
            case '\'':
                sb.append("\\\'");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Decodes a string literal.
     * 
     * @param str
     *            string literal
     * @return decoded string
     */
    public static String decode(final String str) {
        final StringBuilder sb = new StringBuilder(str.length() - 2);
        for (int i = 1; i < str.length() - 1; i++) {
            final char c = str.charAt(i);
            if (c == '\\') {
                switch (str.charAt(++i)) {
                case 'r':
                    sb.append('\r');
                    break;
                case 'n':
                    sb.append('\n');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case 'b':
                    sb.append('\b');
                    break;
                case '"':
                    sb.append('"');
                    break;
                case '\'':
                    sb.append('\'');
                    break;
                case '\\':
                    sb.append('\\');
                    break;
                default:
                    throw new IllegalArgumentException("Unknown escape "
                            + "sequence '\\" + str.charAt(i) + "'");
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Decodes a character literal.
     * 
     * @param str
     *            character literal
     * @return decoded character
     */
    public static Character charDecode(final String str) {
        if (str.length() == 3) {
            return str.charAt(1);
        }
        char res = str.charAt(2);
        switch (res) {
        case 'r':
            res = '\r';
            break;
        case 'n':
            res = '\n';
            break;
        case 't':
            res = '\t';
            break;
        case 'f':
            res = '\f';
            break;
        case 'b':
            res = '\b';
            break;
        case '"':
            res = '"';
            break;
        case '\'':
            res = '\'';
            break;
        case '\\':
            res = '\\';
            break;
        default:
            throw new IllegalArgumentException("Unknown escape "
                    + "sequence '\\" + res + "'");
        }
        return res;
    }

    /**
     * Escapes only double quotes and backslashes. <br/> {@code '"' => '\"'} <br/> {@code
     * '\' => '\\'}
     * 
     * @param str
     *            The String to escape.
     * @return The escaped String.
     */
    public static String primitiveEscape(final String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
