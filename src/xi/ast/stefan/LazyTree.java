package xi.ast.stefan;

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
public class LazyTree implements CommonNode {

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
			} else if (b == BuiltIn.K) {
				return new LazyTree(b, Prim.K);
			} else if (b == BuiltIn.I) {
				return new LazyTree(b, Prim.I);
			} else if (b == BuiltIn.Y) {
				return new LazyTree(b, Prim.Y);
			} else if (b == BuiltIn.HD) {
				return new LazyTree(b, Prim.Hd);
			} else if (b == BuiltIn.TL) {
				return new LazyTree(b, Prim.Tl);
			} else if (b == BuiltIn.SEQ) {
				return new LazyTree(b, Prim.Seq);
			}
		}
		return new LazyTree(n, false, null, null, "not yet implemented", 0,
				false, Kind.str, null);
	}

	private static volatile int ID = 0;

	private final int id;

	private final Node ref;

	private final boolean bln;

	private final Prim prim;

	private final String name;

	private final String str;

	private final int num;

	private final boolean isEmpty;

	private final Kind kind;

	private final Expr[] expr;

	private Map<String, LazyTree> map;

	private LazyTree tail;

	private LazyTree body;

	private LazyTree left;

	private LazyTree right;

	private LazyTree head;

	private LazyTree(final Node ref, final boolean bln, final Prim prim,
			final String name, final String str, final int num,
			final boolean isEmpty, final Kind kind, final Expr[] expr) {
		this.ref = ref;
		this.bln = bln;
		this.prim = prim;
		this.name = name;
		this.str = str;
		this.num = num;
		this.isEmpty = isEmpty;
		this.kind = kind;
		this.expr = expr;
		id = ID++;
		body = null;
		left = null;
		right = null;
		head = null;
		tail = null;
		map = null;
	}

	private LazyTree(final Bool b) {
		this(b, b.getValue(), null, null, null, 0, false, Kind.bln, null);
	}

	private LazyTree(final Name n) {
		this(n, false, null, n.getName(), null, 0, false, Kind.name, null);
	}

	private LazyTree(final Str s) {
		this(s, false, null, null, s.toString(), 0, false, Kind.str, null);
	}

	private LazyTree(final Char c) {
		this(c, false, null, null, c.toString(), 0, false, Kind.str, null);
	}

	private LazyTree(final Lambda l) {
		this(l, false, null, l.getName(), null, 0, false, Kind.lam,
				new Expr[] { l.getBody() });
	}

	private LazyTree(final Num n) {
		this(n, false, null, null, null, n.getValue(), false, Kind.num, null);
	}

	private LazyTree(final Module m, final Map<String, LazyTree> map) {
		this(m, false, null, null, null, 0, false, Kind.module, null);
		this.map = map;
	}

	private LazyTree(final LetIn m, final Map<String, LazyTree> map) {
		this(m, false, null, null, null, 0, false, Kind.let, new Expr[] { m
				.getBody() });
		this.map = map;
	}

	private LazyTree(final Node n, final Prim prim) {
		this(n, false, prim, null, null, 0, false, Kind.prim, null);
	}

	private LazyTree(final Node n, final boolean isEmpty, final Expr[] hdtl) {
		this(n, false, null, null, null, 0, isEmpty, Kind.lst, hdtl);
	}

	private LazyTree(final App a) {
		this(a, false, null, null, null, 0, false, Kind.app, new Expr[] {
				a.getLeft(), a.getRight() });
	}

	public Node getRef() {
		return ref;
	}

	public LazyTree getLeftNode() {
		if (left == null) {
			getLeft();
		}
		return left;
	}

	public LazyTree getRightNode() {
		if (right == null) {
			getRight();
		}
		return right;
	}

	public LazyTree getHeadNode() {
		if (head == null) {
			getHead();
		}
		return head;
	}

	public LazyTree getTailNode() {
		if (tail == null) {
			getTail();
		}
		return tail;
	}

	public LazyTree getBodyNode() {
		if (body == null) {
			getBody();
		}
		return body;
	}

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
	public int getNum() {
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

}
