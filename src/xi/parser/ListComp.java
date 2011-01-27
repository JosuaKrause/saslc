package xi.parser;

import java.util.ArrayList;

import xi.compiler.App;
import xi.compiler.Expr;
import xi.compiler.Lambda;
import xi.compiler.Name;

/**
 * Helper class for list comprehension parsing.
 * 
 * @author Leo
 */
class ListComp {

    /** Monadic function 'bind' (>>=). */
    private static final Name BIND = Name.valueOf("bindList");
    /** Monadic function 'return'. */
    private static final Name RETURN = Name.valueOf("returnList");
    /** Monadic function 'sequence' (>>). */
    private static final Name SEQUENCE = Name.valueOf("seqList");
    /** Monadic function 'guard'. */
    private static final Name GUARD = Name.valueOf("guardList");

    /** Enumeration without right bound. */
    private static final Name ENUM_FROM = Name.valueOf("enumFrom");
    /** Enumeration with left and right bound. */
    private static final Name ENUM_FROM_TO = Name.valueOf("enumFromTo");
    /** Enumeration with left bound and step distance. */
    private static final Name ENUM_FROM_THEN = Name.valueOf("enumFromThen");
    /** Enumeration with left and right bound and step distance. */
    private static final Name ENUM_FROM_THEN_TO = Name
            .valueOf("enumFromThenTo");

    /** Components of the list comprehension. */
    private final ArrayList<Part> parts = new ArrayList<Part>();

    /**
     * Constructor taking the last part.
     * 
     * @param p
     *            last part
     */
    public ListComp(final Part p) {
        parts.add(p);
    }

    /**
     * Adds a {@code let} binding for the list comprehension.
     * 
     * @param name
     *            name that's bound
     * @param val
     *            value to bind to the name
     * @return this list comprehension for convenience
     */
    static Part let(final String name, final Expr val) {
        return new CompLet(name, val);
    }

    /**
     * Adds a producer for the list comprehension.
     * 
     * @param name
     *            name for values from this producer
     * @param val
     *            producing expression
     * @return this list comprehension for convenience
     */
    static Part prod(final String name, final Expr val) {
        return new CompProd(name, val);
    }

    /**
     * Adds a guard for the list comprehension.
     * 
     * @param val
     *            boolean expression
     * @return this list comprehension for convenience
     */
    static Part guard(final Expr val) {
        return new CompGuard(val);
    }

    /**
     * Adds a part to this list comprehension.
     * 
     * @param p
     *            part to add
     * @return self reference for convenience
     */
    final ListComp add(final Part p) {
        parts.add(p);
        return this;
    }

    /**
     * Finishes this list comprehension and returns the desugared parse tree.
     * 
     * @param ret
     *            return value of the list comprehension
     * @return parse tree
     */
    final Expr finish(final Expr ret) {
        Expr val = App.create(RETURN, ret);
        for (final Part c : parts) {
            val = c.create(val);
        }
        return val;
    }

    /**
     * Creates the desugared expression for {@code [start (, then)? .. (to)?]}.
     * 
     * @param from
     *            start value
     * @param then
     *            optional next step
     * @param to
     *            optional end value
     * @return desugared expression
     */
    static Expr enumerate(final Expr from, final Expr then, final Expr to) {
        if (then != null) {
            if (to != null) {
                return App.create(ENUM_FROM_THEN_TO, from, then, to);
            }
            return App.create(ENUM_FROM_THEN, from, then);
        }
        if (to != null) {
            return App.create(ENUM_FROM_TO, from, to);
        }
        return App.create(ENUM_FROM, from);
    }

    /**
     * Abstract superclass for all parts of a list comprehension.
     * 
     * @author Leo
     */
    abstract static class Part {

        /** Value of this part. */
        final Expr value;

        /** Name bound. */
        final String name;

        /**
         * Constructor setting name and value.
         * 
         * @param val
         *            value to set
         * @param n
         *            name to set
         */
        Part(final Expr val, final String n) {
            value = val;
            name = n;
        }

        /**
         * Creates a parse tree from the given previous parts and this one.
         * 
         * @param prev
         *            parse tree of the previous parts
         * @return parse tree
         */
        abstract Expr create(final Expr prev);
    }

    /**
     * Variable binding.
     * 
     * @author Leo
     */
    private static class CompLet extends Part {
        /**
         * Constructor.
         * 
         * @param n
         *            name
         * @param val
         *            bound value
         */
        CompLet(final String n, final Expr val) {
            super(val, n);
        }

        @Override
        Expr create(final Expr prev) {
            final Expr ret = App.create(RETURN, value);
            return App.create(App.create(BIND, ret), new Lambda(name, prev));
        }
    }

    /**
     * Producing expression.
     * 
     * @author Leo
     */
    private static class CompProd extends Part {

        /**
         * Constructor.
         * 
         * @param n
         *            name
         * @param val
         *            list of values to be bound
         */
        CompProd(final String n, final Expr val) {
            super(val, n);
        }

        @Override
        Expr create(final Expr prev) {
            final Expr ret = App.create(BIND, value);
            return App.create(ret, new Lambda(name, prev));
        }
    }

    /**
     * Guard expression.
     * 
     * @author Leo
     */
    private static class CompGuard extends Part {

        /**
         * Constructor.
         * 
         * @param val
         *            boolean guard expression
         */
        CompGuard(final Expr val) {
            super(val, null);
        }

        @Override
        Expr create(final Expr prev) {
            final Expr guard = App.create(GUARD, value);
            return App.create(App.create(SEQUENCE, guard), prev);
        }
    }
}
