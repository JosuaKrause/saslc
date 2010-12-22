package xi.ast.stefan;

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

	private static final Map<Node, SharedTree> MAP = new HashMap<Node, SharedTree>();

	static {
		// MAP.put(Def.HD, get(Def.HD));
		// weird but works fine only if this line is commented
		// MAP.put(Def.TL, get(Def.TL));
	}

	public static SharedTree get(final Node n) {
		if (!MAP.containsKey(n)) {
			MAP.put(n, new SharedTree(LazyTree.create(n)));
		}
		return MAP.get(n);
	}

	private static int ID = 0;

	private final LazyTree tree;

	private Map<String, SharedTree> map;

	private SharedTree tail;

	private SharedTree body;

	private SharedTree left;

	private SharedTree right;

	private SharedTree head;

	private final int id;

	public SharedTree(final LazyTree tree) {
		this.tree = tree;
		id = ID++;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public CommonNode getBody() {
		if (body == null) {
			body = get(tree.getBodyNode().getRef());
		}
		return body;
	}

	@Override
	public Map<String, ? extends CommonNode> getDefs() {
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
	public CommonNode getHead() {
		if (head == null) {
			head = get(tree.getHeadNode().getRef());
		}
		return head;
	}

	@Override
	public CommonNode getLeft() {
		if (left == null) {
			left = get(tree.getLeftNode().getRef());
		}
		return left;
	}

	@Override
	public CommonNode getRight() {
		if (right == null) {
			right = get(tree.getRightNode().getRef());
		}
		return right;
	}

	@Override
	public CommonNode getTail() {
		if (tail == null) {
			tail = get(tree.getTailNode().getRef());
		}
		return tail;
	}

	@Override
	public boolean getBln() {
		return tree.getBln();
	}

	@Override
	public Kind getKind() {
		return tree.getKind();
	}

	@Override
	public String getName() {
		return tree.getName();
	}

	@Override
	public int getNum() {
		return tree.getNum();
	}

	@Override
	public Prim getPrim() {
		return tree.getPrim();
	}

	@Override
	public String getStr() {
		return tree.getStr();
	}

	@Override
	public boolean isEmpty() {
		return tree.isEmpty();
	}

}
