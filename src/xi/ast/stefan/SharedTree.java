package xi.ast.stefan;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import stefan.CommonNode;
import stefan.Kind;
import stefan.Prim;
import xi.ast.Node;

/**
 * The real tree given by the graph of nodes.
 * 
 * @author Joschi
 * 
 */
public class SharedTree implements CommonNode {

    /** Shared nodes. */
    private static final Map<Node, SharedTree> MAP = new HashMap<Node, SharedTree>();

    /**
     * Retrieves the shared tree node for the given node.
     * 
     * @param n
     *            node
     * @return shared tree node
     */
    public static SharedTree get(final Node n) {
        if (!MAP.containsKey(n)) {
            MAP.put(n, new SharedTree(LazyTree.create(n)));
        }
        return MAP.get(n);
    }

    /** Unique ID generator. */
    private static int idGen = 0;

    /** Lazy tree instance. */
    private final LazyTree tree;
    /** Definition map. */
    private Map<String, SharedTree> map;
    /** List tail. */
    private SharedTree tail;
    /** Definition body. */
    private SharedTree body;
    /** Left child. */
    private SharedTree left;
    /** Right child. */
    private SharedTree right;
    /** List head. */
    private SharedTree head;
    /** Node ID. */
    private final int id;

    /**
     * Constructor.
     * 
     * @param t
     *            tree
     */
    public SharedTree(final LazyTree t) {
        this.tree = t;
        id = idGen++;
    }

    @Override
    public final int getId() {
        return id;
    }

    @Override
    public final CommonNode getBody() {
        if (body == null) {
            body = get(tree.getBodyNode().getRef());
        }
        return body;
    }

    @Override
    public final Map<String, ? extends CommonNode> getDefs() {
        if (map == null) {
            map = new HashMap<String, SharedTree>();
            final Map<String, LazyTree> treeMap = tree.getNodeMap();
            for (final String s : treeMap.keySet()) {
                map.put(s, get(treeMap.get(s).getRef()));
            }
        }
        return map;
    }

    @Override
    public final CommonNode getHead() {
        if (head == null) {
            head = get(tree.getHeadNode().getRef());
        }
        return head;
    }

    @Override
    public final CommonNode getLeft() {
        if (left == null) {
            left = get(tree.getLeftNode().getRef());
        }
        return left;
    }

    @Override
    public final CommonNode getRight() {
        if (right == null) {
            right = get(tree.getRightNode().getRef());
        }
        return right;
    }

    @Override
    public final CommonNode getTail() {
        if (tail == null) {
            tail = get(tree.getTailNode().getRef());
        }
        return tail;
    }

    @Override
    public final boolean getBln() {
        return tree.getBln();
    }

    @Override
    public final Kind getKind() {
        return tree.getKind();
    }

    @Override
    public final String getName() {
        return tree.getName();
    }

    @Override
    public final BigInteger getNum() {
        return tree.getNum();
    }

    @Override
    public final Prim getPrim() {
        return tree.getPrim();
    }

    @Override
    public final String getStr() {
        return tree.getStr();
    }

    @Override
    public final boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public final int getChar() {
        return tree.getChar();
    }
}
