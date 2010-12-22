package xi.go.cst.prim;

import static xi.go.cst.Thunk.app;

import java.io.IOException;
import java.io.Writer;
import java.util.EnumMap;
import java.util.HashMap;

import xi.go.cst.App;
import xi.go.cst.Node;
import xi.go.cst.Thunk;

/**
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class Function extends Prim {

	public static enum Def {

		S('S', 3) {
			@Override
			public Node apply(final Thunk... args) {
				final Thunk f = args[0], g = args[1], x = args[2];
				return new App(app(f, x), app(g, x));
			}
		},

		K('K') {
			@Override
			public Node apply(final Thunk... args) {
				return args[0].getNode();
			}
		},

		I('I', 1) {
			@Override
			public Node apply(final Thunk... args) {
				return args[0].getNode();
			}
		},

		Y('Y', 1) {
			@Override
			public Node apply(final Thunk... args) {
				return new App(args[0], app(getInstance(Y), args[0]));
			}
		},

		ADD('+') {
			@Override
			public Node apply(final Thunk... args) {
				final int a = args[0].wHNF().getNum();
				final int b = args[1].wHNF().getNum();
				return new Num(a + b);
			}
		},

		SUB('-') {
			@Override
			public Node apply(final Thunk... args) {
				final int a = args[0].wHNF().getNum();
				final int b = args[1].wHNF().getNum();
				return new Num(a - b);
			}
		},

		MUL('*') {
			@Override
			public Node apply(final Thunk... args) {
				final int a = args[0].wHNF().getNum();
				final int b = args[1].wHNF().getNum();
				return new Num(a * b);
			}
		},

		DIV('/') {
			@Override
			public Node apply(final Thunk... args) {
				final int a = args[0].wHNF().getNum();
				final int b = args[1].wHNF().getNum();
				return new Num(a / b);
			}
		},

		MOD('%') {
			@Override
			public Node apply(final Thunk... args) {
				final int a = args[0].wHNF().getNum();
				final int b = args[1].wHNF().getNum();
				return new Num(a % b);
			}
		},

		NOT('!', 1) {
			@Override
			public Node apply(final Thunk... args) {
				return Bool.get(!args[0].wHNF().getBool());
			}
		},

		NEG('~', 1) {
			@Override
			public Node apply(final Thunk... args) {
				return new Num(-args[0].wHNF().getNum());
			}
		},

		COND('?', 3) {
			@Override
			public Node apply(final Thunk... args) {
				return args[args[0].wHNF().getBool() ? 1 : 2].getNode();
			}
		},

		CONS(':') {
			@Override
			public Node apply(final Thunk... args) {
				return new List(args[0], args[1]);
			}
		},

		HEAD('h', 1) {
			@Override
			public Node apply(final Thunk... args) {
				// get the cons node
				return args[0].wHNF().getHead();
			}
		},

		TAIL('t', 1) {
			@Override
			public Node apply(final Thunk... args) {
				// get the cons node
				return args[0].wHNF().getTail();
			}
		},

		LT('L') {
			@Override
			public Node apply(final Thunk... args) {
				final int x = args[0].wHNF().getNum();
				final int y = args[1].wHNF().getNum();
				return Bool.get(x < y);
			}
		},

		LTE('l') {
			@Override
			public Node apply(final Thunk... args) {
				final int x = args[0].wHNF().getNum();
				final int y = args[1].wHNF().getNum();
				return Bool.get(x <= y);
			}
		},

		EQ('e') {
			@Override
			public Node apply(final Thunk... args) {
				final Node x = args[0].wHNF();
				final Node y = args[1].wHNF();
				return Bool.get(x.eq(y));
			}
		},

		NEQ('n') {
			@Override
			public Node apply(final Thunk... args) {
				final Node x = args[0].wHNF();
				final Node y = args[1].wHNF();
				return Bool.get(!x.eq(y));
			}
		},

		GTE('g') {
			@Override
			public Node apply(final Thunk... args) {
				final int x = args[0].wHNF().getNum();
				final int y = args[1].wHNF().getNum();
				return Bool.get(x >= y);
			}
		},

		GT('G') {
			@Override
			public Node apply(final Thunk... args) {
				final int x = args[0].wHNF().getNum();
				final int y = args[1].wHNF().getNum();
				return Bool.get(x > y);
			}
		},

		AND('&') {
			@Override
			public Node apply(final Thunk... args) {
				final Node x = args[0].wHNF();
				return (!x.getBool()) ? x : args[1].wHNF();
			}
		},

		OR('|') {
			@Override
			public Node apply(final Thunk... args) {
				final Node x = args[0].wHNF();
				return x.getBool() ? x : args[1].wHNF();
			}
		},

		HD('h', 1) {
			@Override
			public Node apply(final Thunk... args) {
				return args[0].wHNF().getHead();
			}
		},

		TL('t', 1) {
			@Override
			public Node apply(final Thunk... args) {
				return args[0].wHNF().getTail();
			}
		},

		SEQ('s') {
			@Override
			public Node apply(final Thunk... args) {
				args[0].wHNF();
				return args[1].getNode();
			}
		},

		;

		private static final HashMap<Character, Def> funMap = new HashMap<Character, Def>();
		static {
			for (final Def fun : values()) {
				funMap.put(fun.name, fun);
			}
		}

		public final int cardinality;

		public final char name;

		private Def(final char f, final int n) {
			name = f;
			cardinality = n;
		}

		private Def(final char f) {
			this(f, 2);
		}

		public abstract Node apply(final Thunk... args);

	}

	/** Map for function objects. */
	private static final EnumMap<Def, Thunk> funs = new EnumMap<Def, Thunk>(
			Def.class);

	public final Def fun;

	private Function(final Def f) {
		fun = f;
	}

	public static Thunk getInstance(final Def f) {
		if (f == null) {
			return null;
		}
		if (!funs.containsKey(f)) {
			funs.put(f, new Thunk(new Function(f)));
		}
		return funs.get(f);
	}

	public static Thunk forChar(final char name) {
		return getInstance(Def.funMap.get(name));
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
	public void eval(final Writer w) throws IOException {
		throw new IllegalStateException("Function '" + fun
				+ "' cannot be printed.");
	}

	@Override
	public boolean eq(final Node n) {
		throw new IllegalArgumentException("Can't compare functions.");
	}

}
