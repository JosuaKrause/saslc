package xi.sk;

import java.math.BigInteger;

/**
 * Interface for visitors used in the traversal of an {@link SKTree}.
 * 
 * @author Leo Woerteler
 */
public interface SKVisitor {

    /** Called whenever an application node is encountered. */
    public void app();

    /**
     * Called whenever a primitive node is encountered.
     * 
     * @param p
     *            the primitive node
     */
    public void prim(final SKPrim p);

    /**
     * Called whenever a free variable is encountered.
     * 
     * @param fv
     *            name of the free variable
     */
    public void var(final String fv);

    /**
     * Called whenever a name binding is encountered.
     * 
     * @param name
     *            name
     */
    public void def(final String name);

    /**
     * Called whenever a String literal is encountered.
     * 
     * @param str
     *            string
     */
    public void str(final String str);

    /**
     * Called whenever a number is encountered.
     * 
     * @param n
     *            number
     */
    public void num(final BigInteger n);

    /**
     * Called whenever a character is encountered.
     * 
     * @param cp
     *            Unicode code point
     */
    public void chr(final int cp);

    /** Called whenever a list node is encountered. */
    public void cons();

    /** Called whenever an empty list is encountered. */
    public void nil();

    /**
     * Called whenever a boolean literal is encountered.
     * 
     * @param val
     *            boolean value
     */
    public void bool(final boolean val);
}
