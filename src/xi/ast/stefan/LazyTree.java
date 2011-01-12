package xi.ast.stefan;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import stefan.CommonNode;
import stefan.Kind;
import stefan.Prim;
import xi.ast.App;
import xi.ast.Bool;
import xi.ast.BuiltIn;
import xi.ast.Char;
import xi.ast.Expr;
import xi.ast.Lambda;
import xi.ast.LetIn;
import xi.ast.Module;
import xi.ast.Name;
import xi.ast.Node;
import xi.ast.Num;
import xi.ast.Str;

/**
 * A more readable tree of the nodes of the graph.
 * 
 * @author Joschi
 * 
 */
public final class LazyTree implements CommonNode {

    /**
     * Creates a LazyTree from the given {@link Node node}.
     * 
     * @param n
     *            node
     * @return LazyTree
     */
    public static LazyTree create(final Node n) {
        if (n instanceof App) {
            return new LazyTree((App) n);
        }
        if (n instanceof Num) {
            return new LazyTree((Num) n);
        }
        if (n instanceof Bool) {
            return new LazyTree((Bool) n);
        }
        if (n instanceof Lambda) {
            return new LazyTree((Lambda) n);
        }
        if (n instanceof Name) {
            return new LazyTree((Name) n);
        }
        if (n instanceof Str) {
            return new LazyTree((Str) n);
        }
        if (n instanceof Char) {
            return new LazyTree((Char) n);
        }
        if (n instanceof LetIn) {
            final LetIn l = (LetIn) n;
            final Module m = l.getModule();
            final Map<String, LazyTree> map = new HashMap<String, LazyTree>();
            for (final Entry<Name, Expr> d : m.getMap().entrySet()) {
                map.put(d.getKey().getName(), LazyTree.create(d.getValue()));
            }
            return new LazyTree(l, map);
        }
        if (n instanceof Module) {
            final Module m = (Module) n;
            final Map<String, LazyTree> map = new HashMap<String, LazyTree>();
            for (final Entry<Name, Expr> d : m.getMap().entrySet()) {
                map.put(d.getKey().getName(), LazyTree.create(d.getValue()));
            }
            return new LazyTree(m, map);
        }
        if (n instanceof BuiltIn) {
            final BuiltIn b = (BuiltIn) n;
            if (b == BuiltIn.AND) {
                return new LazyTree(b, Prim.And);
            } else if (b == BuiltIn.BRANCH) {
                return new LazyTree(b, Prim.Cond);
            } else if (b == BuiltIn.CONS) {
                return new LazyTree(b, Prim.Cons);
            } else if (b == BuiltIn.DIV) {
                return new LazyTree(b, Prim.Div);
            } else if (b == BuiltIn.EQ) {
                return new LazyTree(b, Prim.Eq);
            } else if (b == BuiltIn.GE) {
                return new LazyTree(b, Prim.Ge);
            } else if (b == BuiltIn.GT) {
                return new LazyTree(b, Prim.Gt);
            } else if (b == BuiltIn.LE) {
                return new LazyTree(b, Prim.Le);
            } else if (b == BuiltIn.LT) {
                return new LazyTree(b, Prim.Lt);
            } else if (b == BuiltIn.MINUS) {
                return new LazyTree(b, Prim.Sub);
            } else if (b == BuiltIn.MOD) {
                return new LazyTree(b, Prim.Mod);
            } else if (b == BuiltIn.NE) {
                return new LazyTree(b, Prim.Ne);
            } else if (b == BuiltIn.NIL) {
                return new LazyTree(b, true, null);
            } else if (b == BuiltIn.NOT) {
                return new LazyTree(b, Prim.Not);
            } else if (b == BuiltIn.OR) {
                return new LazyTree(b, Prim.Or);
            } else if (b == BuiltIn.PLUS) {
                return new LazyTree(b, Prim.Add);
            } else if (b == BuiltIn.TIMES) {
                return new LazyTree(b, Prim.Mul);
            } else if (b == BuiltIn.UMINUS) {
                return new LazyTree(b, Prim.Neg);
            } else if (b == BuiltIn.S) {
                return new LazyTree(b, Prim.S);
            } else if (b == BuiltIn.B) {
                return new LazyTree(b, Prim.B);
            } else if (b == BuiltIn.C) {
                return new LazyTree(b, Prim.C);
            } else if (b == BuiltIn.K) {
                return new LazyTree(b, Prim.K);
            } else if (b == BuiltIn.I) {
                return new LazyTree(b, Prim.I);
            } else if (b == BuiltIn.Y) {
                return new LazyTree(b, Prim.Y);
            } else if (b == BuiltIn.U) {
                return new LazyTree(b, Prim.U);
            } else if (b == BuiltIn.HD) {
                return new LazyTree(b, Prim.Hd);
            } else if (b == BuiltIn.TL) {
                return new LazyTree(b, Prim.Tl);
            } else if (b == BuiltIn.SEQ) {
                return new LazyTree(b, Prim.Seq);
            } else if (b == BuiltIn.CHAR) {
                return new LazyTree(b, Prim.Char);
            }
        }
        throw new IllegalStateException("Node '" + n + "' not known.");
    }

    /** ID generator. */
    private static volatile int idGen = 0;

    /** Node's ID. */
    private final int id;

    /** Underlying node. */
    private final Node ref;

    /** Boolean value. */
    private final boolean bln;

    /** Primitive. */
    private final Prim prim;

    /** Name. */
    private final String name;

    /** String value. */
    private final String str;

    /** Numeric value. */
    private final BigInteger num;

    /** Character code point. */
    private final int chr;

    /** Empty list flag. */
    private final boolean isEmpty;

    /** Node kind. */
    private final Kind kind;

    /** Expressions. */
    private final Expr[] expr;

    /** Definition map. */
    private Map<String, LazyTree> map;

    /** List's tail. */
    private LazyTree tail;

    /** Body expression. */
    private LazyTree body;

    /** Left child. */
    private LazyTree left;

    /** Right child. */
    private LazyTree right;

    /** List head. */
    private LazyTree head;

    /**
     * Constructor.
     * 
     * @param r
     *            referenced node
     * @param b
     *            boolean value
     * @param p
     *            primitive type
     * @param n
     *            name
     * @param s
     *            string value
     * @param nm
     *            numeric value
     * @param e
     *            empty list flag
     * @param k
     *            node kind
     * @param ex
     *            expression array
     * @param ch
     *            character code point
     */
    private LazyTree(final Node r, final boolean b, final Prim p,
            final String n, final String s, final BigInteger nm,
            final boolean e, final Kind k, final Expr[] ex, final int ch) {
        ref = r;
        bln = b;
        prim = p;
        name = n;
        str = s;
        num = nm;
        isEmpty = e;
        kind = k;
        expr = ex;
        chr = ch;
        id = idGen++;
    }

    /**
     * Constructor for Bools.
     * 
     * @param b
     *            Bool instance
     */
    private LazyTree(final Bool b) {
        this(b, b.getValue(), null, null, null, null, false, Kind.bln, null, 0);
    }

    /**
     * Constructor for names.
     * 
     * @param nam
     *            name
     */
    private LazyTree(final Name nam) {
        this(nam, false, null, nam.getName(), null, null, false, Kind.name,
                null, 0);
    }

    /**
     * Constructor for Strings.
     * 
     * @param string
     *            string
     */
    private LazyTree(final Str string) {
        this(string, false, null, null, string.toString(), null, false,
                Kind.str, null, 0);
    }

    /**
     * Constructor for chars.
     * 
     * @param c
     *            char
     */
    private LazyTree(final Char c) {
        this(c, false, null, null, c.toString(), null, false, Kind.chr, null, c
                .getChar());
    }

    /**
     * Constructor for lambdas.
     * 
     * @param l
     *            lambda
     */
    private LazyTree(final Lambda l) {
        this(l, false, null, l.getName(), null, null, false, Kind.lam,
                new Expr[] { l.getBody() }, 0);
    }

    /**
     * Constructor for numbers.
     * 
     * @param numb
     *            number
     */
    private LazyTree(final Num numb) {
        this(numb, false, null, null, null, numb.getValue(), false, Kind.num,
                null, 0);
    }

    /**
     * Constructor for modules.
     * 
     * @param m
     *            module
     * @param mp
     *            lookup map
     */
    private LazyTree(final Module m, final Map<String, LazyTree> mp) {
        this(m, false, null, null, null, null, false, Kind.module, null, 0);
        map = mp;
    }

    /**
     * Constructor for let expressions.
     * 
     * @param m
     *            let
     * @param mp
     *            lookup map
     */
    private LazyTree(final LetIn m, final Map<String, LazyTree> mp) {
        this(m, false, null, null, null, null, false, Kind.let, new Expr[] { m
                .getBody() }, 0);
        map = mp;
    }

    /**
     * Constructor for primitives.
     * 
     * @param n
     *            node
     * @param pr
     *            primitive
     */
    private LazyTree(final Node n, final Prim pr) {
        this(n, false, pr, null, null, null, false, Kind.prim, null, 0);
    }

    /**
     * Constructor for lists.
     * 
     * @param n
     *            node
     * @param empty
     *            empty list flag
     * @param hdtl
     *            head and tail
     */
    private LazyTree(final Node n, final boolean empty, final Expr[] hdtl) {
        this(n, false, null, null, null, null, empty, Kind.lst, hdtl, 0);
    }

    /**
     * Constructor for application nodes.
     * 
     * @param a
     *            application
     */
    private LazyTree(final App a) {
        this(a, false, null, null, null, null, false, Kind.app, new Expr[] {
                a.getLeft(), a.getRight() }, 0);
    }

    /**
     * Getter for the referenced node.
     * 
     * @return node
     */
    public Node getRef() {
        return ref;
    }

    /**
     * Getter for the left child node.
     * 
     * @return left child
     */
    public LazyTree getLeftNode() {
        if (left == null) {
            getLeft();
        }
        return left;
    }

    /**
     * Getter for the right child node.
     * 
     * @return right child
     */
    public LazyTree getRightNode() {
        if (right == null) {
            getRight();
        }
        return right;
    }

    /**
     * Getter for the list head.
     * 
     * @return list head
     */
    public LazyTree getHeadNode() {
        if (head == null) {
            getHead();
        }
        return head;
    }

    /**
     * Getter for the list tail.
     * 
     * @return list tail
     */
    public LazyTree getTailNode() {
        if (tail == null) {
            getTail();
        }
        return tail;
    }

    /**
     * Getter for the body.
     * 
     * @return body
     */
    public LazyTree getBodyNode() {
        if (body == null) {
            getBody();
        }
        return body;
    }

    /**
     * Getter for the definition map.
     * 
     * @return definition map
     */
    public Map<String, LazyTree> getNodeMap() {
        if (map == null) {
            getDefs();
        }
        return map;
    }

    @Override
    public boolean getBln() {
        return bln;
    }

    @Override
    public CommonNode getBody() {
        if (body == null) {
            body = LazyTree.create(expr[0]);
        }
        return body;
    }

    @Override
    public Map<String, ? extends CommonNode> getDefs() {
        return map;
    }

    @Override
    public CommonNode getHead() {
        if (head == null) {
            head = LazyTree.create(expr[0]);
        }
        return head;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public CommonNode getLeft() {
        if (left == null) {
            left = LazyTree.create(expr[0]);
        }
        return left;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigInteger getNum() {
        return num;
    }

    @Override
    public Prim getPrim() {
        return prim;
    }

    @Override
    public CommonNode getRight() {
        if (right == null) {
            right = LazyTree.create(expr[1]);
        }
        return right;
    }

    @Override
    public String getStr() {
        return str;
    }

    @Override
    public CommonNode getTail() {
        if (tail == null) {
            tail = LazyTree.create(expr[1]);
        }
        return tail;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public int getChar() {
        return chr;
    }

}
