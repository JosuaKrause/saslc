package xi.go.cst.stefan;

import java.math.BigInteger;
import java.util.IdentityHashMap;
import java.util.Map;

import stefan.CommonNode;
import stefan.Kind;
import stefan.Prim;
import xi.go.cst.App;
import xi.go.cst.Node;
import xi.go.cst.Thunk;
import xi.go.cst.prim.Bool;
import xi.go.cst.prim.Char;
import xi.go.cst.prim.Function;
import xi.go.cst.prim.List;
import xi.go.cst.prim.Num;
import xi.go.cst.prim.Str;
import xi.go.cst.prim.Value;

/**
 * Not lazy tree.
 * 
 * @author Joschi
 * 
 */
public class CSTree implements CommonNode {

    public static CSTree cached(final Thunk thunk) {
        final Node node = thunk.getNode();
        if (!ALL.containsKey(node)) {
            create(node);
        }
        return ALL.get(node);
    }

    public static CSTree create(final Thunk thunk) {
        return create(thunk.getNode());
    }

    private static final Map<Node, CSTree> SHARED = new IdentityHashMap<Node, CSTree>();

    private static final Map<Node, CSTree> ALL = new IdentityHashMap<Node, CSTree>();

    public static void clear() {
        SHARED.clear();
        ALL.clear();
        ID_GEN = 0;
    }

    public static CSTree create(final Node node) {
        if (!node.shareNode()) {
            return createNew(node);
        }
        if (!SHARED.containsKey(node)) {
            createNew(node);
        }
        return SHARED.get(node);
    }

    private static CSTree createNew(final Node node) {
        CSTree res;
        if (node instanceof App) {
            res = new CSTree((App) node);
        } else if (node instanceof Function) {
            res = new CSTree((Function) node);
        } else if (node instanceof List) {
            res = new CSTree((List) node);
        } else if (node instanceof Num) {
            res = new CSTree((Num) node);
        } else if (node instanceof Char) {
            res = new CSTree((Char) node);
        } else if (node instanceof Str) {
            res = new CSTree((Str) node);
        } else if (node instanceof Bool) {
            res = new CSTree((Bool) node);
        } else {
            throw new InternalError("u should not be here!");
        }
        ALL.put(node, res);
        return res;
    }

    private static volatile int ID_GEN = 0;

    /** The id */
    private final int id;

    /** Underlying value */
    private final Value val;

    /** Primitive. */
    private final Prim prim;

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

    /** List head. */
    private final CSTree head;

    /** List's tail. */
    private final CSTree tail;

    /** Left child. */
    private final CSTree left;

    /** Right child. */
    private final CSTree right;

    /**
     * Constructor.
     * 
     * @param r
     *            referenced node
     * @param v
     *            The value.
     * @param p
     *            primitive type
     * @param s
     *            string value
     * @param nm
     *            numeric value
     * @param e
     *            empty list flag
     * @param k
     *            node kind
     * @param ch
     *            character code point
     * @param leftRight
     *            If left and right should be set.
     * @param headTail
     *            If head and tail should be set.
     */
    private CSTree(final Node r, final Value v, final Prim p, final String s,
            final BigInteger nm, final boolean e, final Kind k, final int ch,
            final boolean leftRight, final boolean headTail) {
        ALL.put(r, this);
        if (r.shareNode()) {
            SHARED.put(r, this);
        }
        val = v;
        prim = p;
        str = s;
        num = nm;
        isEmpty = e;
        kind = k;
        chr = ch;
        id = ID_GEN++;
        if (leftRight) {
            left = create(r.getLeft());
            right = create(r.getRight());
        } else {
            left = right = null;
        }
        if (headTail && !e) {
            head = create(v.getHead());
            tail = create(v.getTail());
        } else {
            head = tail = null;
        }
    }

    /**
     * Constructor for Bools.
     * 
     * @param b
     *            Bool instance
     */
    private CSTree(final Bool b) {
        this(b, b, null, null, null, false, Kind.bln, 0, false, false);
    }

    /**
     * Constructor for Strings.
     * 
     * @param string
     *            string
     */
    private CSTree(final Str string) {
        this(string, string, null, string.toString(), null, false, Kind.str, 0,
                false, false);
    }

    /**
     * Constructor for chars.
     * 
     * @param c
     *            char
     */
    private CSTree(final Char c) {
        this(c, c, null, c.toString(), null, false, Kind.chr, c.getNum()
                .intValue(), false, false);
    }

    /**
     * Constructor for numbers.
     * 
     * @param numb
     *            number
     */
    private CSTree(final Num numb) {
        this(numb, numb, null, null, numb.getNum(), false, Kind.num, 0, false,
                false);
    }

    private static Prim primForFun(final Function f) {
        switch (f.getFunction()) {
        case ADD:
            return Prim.Add;
        case AND:
            return Prim.And;
        case COND:
            return Prim.Cond;
        case CONS:
            return Prim.Cons;
        case DIV:
            return Prim.Div;
        case EQ:
            return Prim.Eq;
        case GT:
            return Prim.Gt;
        case GTE:
            return Prim.Ge;
        case HEAD:
            return Prim.Hd;
        case I:
            return Prim.I;
        case K:
            return Prim.K;
        case LT:
            return Prim.Lt;
        case LTE:
            return Prim.Le;
        case MOD:
            return Prim.Mod;
        case MUL:
            return Prim.Mul;
        case NEG:
            return Prim.Neg;
        case NEQ:
            return Prim.Ne;
        case NOP:
            throw new IllegalStateException("Unknown primitive");
        case NOT:
            return Prim.Not;
        case OR:
            return Prim.Or;
        case S:
            return Prim.S;
        case SEQ:
            return Prim.Seq;
        case SUB:
            return Prim.Sub;
        case TAIL:
            return Prim.Tl;
        case U:
            return Prim.U;
        case Y:
            return Prim.Y;
        }
        throw new InternalError("should never be reached");
    }

    /**
     * Constructor for primitives.
     * 
     * @param f
     *            The function for the primitive.
     */
    private CSTree(final Function f) {
        this(f, null, primForFun(f), null, null, false, Kind.prim, 0, false,
                false);
    }

    /**
     * Constructor for lists.
     * 
     * @param n
     *            node
     * @param hdtl
     *            head and tail
     */
    private CSTree(final List n) {
        this(n, n, null, null, null, n == List.EMPTY, Kind.lst, 0, false, true);
    }

    /**
     * Constructor for application nodes.
     * 
     * @param a
     *            application
     */
    private CSTree(final App a) {
        this(a, null, null, null, null, false, Kind.app, 0, true, false);
    }

    @Override
    public boolean getBln() {
        return val.getBool();
    }

    @Override
    public CommonNode getBody() {
        throw new IllegalStateException("body? for what?");
    }

    @Override
    public int getChar() {
        return chr;
    }

    @Override
    public Map<String, ? extends CommonNode> getDefs() {
        throw new IllegalStateException("no defs allowed here");
    }

    @Override
    public CommonNode getHead() {
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
        return left;
    }

    @Override
    public String getName() {
        throw new IllegalStateException("we are anonymous!");
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
        return right;
    }

    @Override
    public String getStr() {
        return str;
    }

    @Override
    public CommonNode getTail() {
        return tail;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

}
