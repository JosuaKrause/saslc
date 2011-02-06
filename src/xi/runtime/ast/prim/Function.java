package xi.runtime.ast.prim;

import static xi.runtime.ast.Thunk.app;

import java.math.BigInteger;

import xi.runtime.ast.App;
import xi.runtime.ast.Node;
import xi.runtime.ast.Thunk;
import xi.sk.SKPrim;
import xi.sk.SKVisitor;

/**
 * Built-in functions of the SK interpreter.
 * 
 * @author Leo
 * @author Joschi
 */
public final class Function extends Prim {

    /**
     * Enumeration of all built-in functions.
     * 
     * @author Leo
     */
    public static enum Def {

        /** The substitution combinator. */
        S(SKPrim.S) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk f = args[0], g = args[1], x = args[2];
                return new App(app(f, x), app(g, x));
            }
        },

        /** Another substitution combinator. */
        S_PRIME(SKPrim.S_PRIME) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk c = args[0], f = args[1], g = args[2], x = args[3];
                return new App(app(c, app(f, x)), app(g, x));
            }
        },

        /** The right-side substitution combinator. */
        B(SKPrim.B) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk f = args[0], g = args[1], x = args[2];
                return new App(f, app(g, x));
            }
        },

        /** The right-side substitution combinator. */
        B_STAR(SKPrim.B_STAR) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk c = args[0], f = args[1], g = args[2], x = args[3];
                return new App(c, app(f, app(g, x)));
            }
        },

        /** The left-side substitution combinator. */
        C(SKPrim.C) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk f = args[0], g = args[1], x = args[2];
                return new App(app(f, x), g);
            }
        },

        /** Another left-side substitution combinator. */
        C_PRIME(SKPrim.C_PRIME) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk c = args[0], f = args[1], g = args[2], x = args[3];
                return new App(app(c, app(f, x)), g);
            }
        },

        /** The constant combinator. */
        K(SKPrim.K) {
            @Override
            public Node apply(final Thunk... args) {
                return indirect(args[0]);
            }
        },

        /** The identity combinator. */
        I(SKPrim.I) {
            @Override
            public Node apply(final Thunk... args) {
                throw new IllegalStateException();
                // return args[0].getNode();
            }
        },

        /** The recursion combinator. */
        Y(SKPrim.Y) {
            @Override
            public Node apply(final Thunk... args) {
                return App.loop(args[0]);
            }
        },

        /** The uncurry combinator. */
        U(SKPrim.U) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk f = args[0];
                final Thunk z = args[1];
                return new App(app(f, app(HEAD.thunk, z)), app(TAIL.thunk, z));
            }
        },

        /** The uncurry combinator. */
        U_PRIME(SKPrim.U_PRIME) {
            @Override
            public Node apply(final Thunk... args) {
                final Thunk f = args[0];
                final Value z = args[1].wHNF();
                return z.isList() ? new App(app(f, z.getHead()), z.getTail())
                        : Fail.INSTANCE;
            }
        },

        /** Addition. */
        ADD(SKPrim.ADD) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger a = args[0].wHNF().getNum();
                final BigInteger b = args[1].wHNF().getNum();
                return new Num(a.add(b));
            }
        },

        /** Subtraction. */
        SUB(SKPrim.SUB) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger a = args[0].wHNF().getNum();
                final BigInteger b = args[1].wHNF().getNum();
                return new Num(a.subtract(b));
            }
        },

        /** Multiplication. */
        MUL(SKPrim.MUL) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger a = args[0].wHNF().getNum();
                final BigInteger b = args[1].wHNF().getNum();
                return new Num(a.multiply(b));
            }
        },

        /** Division. */
        DIV(SKPrim.DIV) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger a = args[0].wHNF().getNum();
                final BigInteger b = args[1].wHNF().getNum();
                return new Num(a.divide(b));
            }
        },

        /** Modulo. */
        MOD(SKPrim.MOD) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger a = args[0].wHNF().getNum();
                final BigInteger b = args[1].wHNF().getNum();
                return new Num(a.mod(b));
            }
        },

        /** Logical negation. */
        NOT(SKPrim.NOT) {
            @Override
            public Node apply(final Thunk... args) {
                return Bool.get(!args[0].wHNF().getBool());
            }
        },

        /** Numeric negation. */
        NEG(SKPrim.NEG) {
            @Override
            public Node apply(final Thunk... args) {
                return new Num(args[0].wHNF().getNum().negate());
            }
        },

        /** Conditional operator. */
        COND(SKPrim.COND) {
            @Override
            public Node apply(final Thunk... args) {
                return indirect(args[args[0].wHNF().getBool() ? 1 : 2]);
            }
        },

        /** List construction. */
        CONS(SKPrim.CONS) {
            @Override
            public Node apply(final Thunk... args) {
                return new List(args[0], args[1]);
            }
        },

        /** Head of a list. */
        HEAD(SKPrim.HEAD) {
            @Override
            public Node apply(final Thunk... args) {
                // get the cons node
                return indirect(args[0].wHNF().getHead());
            }
        },

        /** Tail of a list. */
        TAIL(SKPrim.TAIL) {
            @Override
            public Node apply(final Thunk... args) {
                // get the cons node
                return indirect(args[0].wHNF().getTail());
            }
        },

        /** Less-than operator. */
        LT(SKPrim.LT) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger x = args[0].wHNF().getNum();
                final BigInteger y = args[1].wHNF().getNum();
                return Bool.get(x.compareTo(y) < 0);
            }
        },

        /** Less-than-or-equal operator. */
        LTE(SKPrim.LTE) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger x = args[0].wHNF().getNum();
                final BigInteger y = args[1].wHNF().getNum();
                return Bool.get(x.compareTo(y) <= 0);
            }
        },

        /** Equality operator. */
        EQ(SKPrim.EQ) {
            @Override
            public Node apply(final Thunk... args) {
                final Value x = args[0].wHNF();
                final Value y = args[1].wHNF();
                return Bool.get(x.eq(y));
            }
        },

        /** Non-equality operator. */
        NEQ(SKPrim.NEQ) {
            @Override
            public Node apply(final Thunk... args) {
                final Value x = args[0].wHNF();
                final Value y = args[1].wHNF();
                return Bool.get(!x.eq(y));
            }
        },

        /** Greater-than-or-equal operator. */
        GTE(SKPrim.GTE) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger x = args[0].wHNF().getNum();
                final BigInteger y = args[1].wHNF().getNum();
                return Bool.get(x.compareTo(y) >= 0);
            }
        },

        /** Greater-than operator. */
        GT(SKPrim.GT) {
            @Override
            public Node apply(final Thunk... args) {
                final BigInteger x = args[0].wHNF().getNum();
                final BigInteger y = args[1].wHNF().getNum();
                return Bool.get(x.compareTo(y) > 0);
            }
        },

        /** Logical conjunction. */
        AND(SKPrim.AND) {
            @Override
            public Node apply(final Thunk... args) {
                return indirect(args[args[0].wHNF().getBool() ? 1 : 0]);
            }
        },

        /** Logical disjunction. */
        OR(SKPrim.OR) {
            @Override
            public Node apply(final Thunk... args) {
                return indirect(args[args[0].wHNF().getBool() ? 0 : 1]);
            }
        },

        /** Sequence operator. */
        SEQ(SKPrim.SEQ) {
            @Override
            public Node apply(final Thunk... args) {
                args[0].wHNF();
                return indirect(args[1]);
            }
        },

        /** Character casting operator. */
        CHAR(SKPrim.CHAR) {
            @Override
            public Node apply(final Thunk... args) {
                return Thunk.chr(args[0].wHNF().getNum().intValue()).wHNF();
            }
        },

        ;

        /** Jump table from character code (ASCII) to definition. */
        private static final Def[] FUN_MAP = new Def[1 << 7];
        static {
            for (final Def fun : values()) {
                if (FUN_MAP[fun.prim.chr] != null) {
                    throw new IllegalStateException(
                            "duplicate symbol definition! " + fun.prim.chr);
                }
                FUN_MAP[fun.prim.chr] = fun;
            }
        }

        /** The function's surrounding thunk. */
        public final Thunk thunk;

        /** Wrapped primitive. */
        public final SKPrim prim;

        /**
         * Constructor.
         * 
         * @param p
         *            primitive
         */
        private Def(final SKPrim p) {
            prim = p;
            thunk = new Thunk(new Function(this));
        }

        /**
         * Applies this function to the given arguments.
         * 
         * @param args
         *            argument array
         * @return resulting node
         */
        public abstract Node apply(final Thunk... args);

        /**
         * Number of arguments this function consumes.
         * 
         * @return arity of this function
         */
        public final int arity() {
            return prim.arity;
        }

        /**
         * Creates an indirection node to the {@link Thunk} {@code th}, i.e.
         * {@code (I @ th)}.
         * 
         * @param th
         *            Thunk to wrap
         * @return the indirection node
         */
        public static Node indirect(final Thunk th) {
            return new App(I.thunk, th);
        }

        @Override
        public String toString() {
            return prim.name;
        }
    }

    /** Definition of this function. */
    private final Def fun;

    /**
     * Constructor taking the definition of this function.
     * 
     * @param f
     *            function definition
     */
    private Function(final Def f) {
        fun = f;
    }

    /**
     * Getter for the function definition associated with the given character.
     * 
     * @param name
     *            character from the SK code
     * @return corresponding {@link Function.Def definition} of {@code null}, if
     *         no such function exists
     */
    public static Def forChar(final char name) {
        return Def.FUN_MAP[name];
    }

    @Override
    public String toString() {
        return fun.toString();
    }

    @Override
    public Def getFunction() {
        return fun;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Function && fun == ((Function) obj).fun;
    }

    @Override
    public int hashCode() {
        return fun.hashCode();
    }

    @Override
    public void traverse(final SKVisitor v) {
        v.prim(fun.prim);
    }

}
