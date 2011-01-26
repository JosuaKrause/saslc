package xi.sk;

import java.io.Reader;
import java.math.BigInteger;
import java.util.Stack;

/**
 * Scans an input and constructs a tree.
 * 
 * @author Joschi
 * @author Leo
 * @param <T>
 *            type of the values to construct
 * 
 */
public abstract class SKParser<T> {

    /**
     * Reads from a SKTree.
     * 
     * @param t
     *            The tree to read.
     */
    public void read(final SKTree t) {
        final Stack<T> stack = new Stack<T>();

        t.traverse(new ParserSKVisitor(stack));

        if (!stack.isEmpty()) {
            throw new IllegalStateException("Premature EOF (stack not empty).");
        }
    }

    /**
     * Reads from a Reader.
     * 
     * @param r
     *            The reader to read from.
     */
    public void read(final Reader r) {
        final Stack<T> stack = new Stack<T>();

        final SKReader skr = new SKReader(new ParserSKVisitor(stack));
        skr.read(r);

        if (!stack.isEmpty()) {
            throw new IllegalStateException("Premature EOF (stack not empty).");
        }
    }

    /**
     * String literal.
     * 
     * @param str
     *            the literal
     * @return string node
     */
    protected abstract T string(final String str);

    /**
     * Numeric literal.
     * 
     * @param n
     *            the number
     * @return number node
     */
    protected abstract T num(final BigInteger n);

    /**
     * Character literal.
     * 
     * @param cp
     *            the character's Unicode code point
     * @return character node
     */
    protected abstract T character(final int cp);

    /**
     * Boolean literal.
     * 
     * @param b
     *            value
     * @return boolean node
     */
    protected abstract T bool(final boolean b);

    /**
     * Primitive function.
     * 
     * @param p
     *            the primitive
     * @return primitive node
     */
    protected abstract T prim(SKPrim p);

    /**
     * A list node.
     * 
     * @param hd
     *            head of the list
     * @param tl
     *            tail of the list
     * @return list node
     */
    protected abstract T cons(final T hd, final T tl);

    /**
     * The empty list.
     * 
     * @return empty list node
     */
    protected abstract T nil();

    /**
     * Application node.
     * 
     * @param f
     *            function node
     * @param x
     *            value node
     * @return application node
     */
    protected abstract T app(final T f, final T x);

    /**
     * Global name definition.
     * 
     * @param name
     *            the bound name
     * @param body
     *            bound expression
     */
    protected abstract void def(final String name, final T body);

    /**
     * Reference to a global name.
     * 
     * @param name
     *            referenced name
     * @return reference node
     */
    protected abstract T reference(final String name);

    /**
     * An SKVisitor that uses a stack to reproduce the SK tree.
     * 
     * @author Leo Woerteler
     */
    private final class ParserSKVisitor implements SKVisitor {

        /** The stack holding unconsumed subtrees. */
        private final Stack<T> stack;

        /**
         * Creates a visitor.
         * 
         * @param s
         *            The shared stack.
         */
        public ParserSKVisitor(final Stack<T> s) {
            stack = s;
        }

        @Override
        public void app() {
            final T f = stack.pop(), x = stack.pop();
            stack.push(SKParser.this.app(f, x));
        }

        @Override
        public void bool(final boolean val) {
            stack.push(SKParser.this.bool(val));
        }

        @Override
        public void chr(final int cp) {
            stack.push(SKParser.this.character(cp));
        }

        @Override
        public void cons() {
            final T hd = stack.pop(), tl = stack.pop();
            stack.push(SKParser.this.cons(hd, tl));
        }

        @Override
        public void def(final String name) {
            SKParser.this.def(name, stack.pop());
        }

        @Override
        public void nil() {
            stack.push(SKParser.this.nil());
        }

        @Override
        public void num(final BigInteger n) {
            stack.push(SKParser.this.num(n));
        }

        @Override
        public void prim(final SKPrim p) {
            stack.push(SKParser.this.prim(p));
        }

        @Override
        public void str(final String str) {
            stack.push(SKParser.this.string(str));
        }

        @Override
        public void var(final String fv) {
            stack.push(SKParser.this.reference(fv));
        }

    }
}
