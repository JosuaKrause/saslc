package xi.sk;

import java.math.BigInteger;

/**
 * Interface for visitors used in the traversal of an {@link SKTree}.
 * 
 * @author Leo Woerteler
 * @author Joschi
 */
public interface SKVisitor {

    /** Called whenever an application node is encountered. */
    void app();

    /**
     * Called whenever a primitive node is encountered.
     * 
     * @param p
     *            the primitive node
     */
    void prim(final SKPrim p);

    /**
     * Called whenever a free variable is encountered.
     * 
     * @param fv
     *            name of the free variable
     */
    void var(final String fv);

    /**
     * Called whenever a name binding is encountered.
     * 
     * @param name
     *            name
     */
    void def(final String name);

    /**
     * Called whenever a String literal is encountered.
     * 
     * @param str
     *            string
     */
    void str(final String str);

    /**
     * Called whenever a number is encountered.
     * 
     * @param n
     *            number
     */
    void num(final BigInteger n);

    /**
     * Called whenever a character is encountered.
     * 
     * @param cp
     *            Unicode code point
     */
    void chr(final int cp);

    /** Called whenever a list node is encountered. */
    void cons();

    /** Called whenever an empty list is encountered. */
    void nil();

    /**
     * Called whenever a boolean literal is encountered.
     * 
     * @param val
     *            boolean value
     */
    void bool(final boolean val);

    /**
     * Called whenever a tuple is encountered.
     * 
     * @param start
     *            If the tuple starts or ends.
     */
    void tuple(boolean start);

    /** Called when the next tuple element is encountered. */
    void nextInTuple();

}
