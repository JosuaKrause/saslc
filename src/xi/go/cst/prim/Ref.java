package xi.go.cst.prim;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import xi.go.cst.Node;
import xi.go.cst.Thunk;

public class Ref extends Value {

	private final String fun;

	private Ref(final String name) {
		fun = name;
	}

	private static final HashMap<String, Thunk> map = new HashMap<String, Thunk>();

	public static Thunk get(final String name) {
		if (!map.containsKey(name)) {
			map.put(name, new Thunk(new Ref(name)));
		}
		return map.get(name);
	}

	@Override
	public String toString() {
		return fun;
	}

	@Override
	public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
		final Thunk funDef = defs.get(fun);
		if (funDef == null) {
			throw new IllegalStateException("Name '" + fun + "' not defined.");
		}
		if (linked.add(fun)) {
			funDef.link(defs, linked);
		}
		return funDef;
	}

	@Override
	public boolean eq(final Node n) {
		return n instanceof Ref && ((Ref) n).fun.equals(fun);
	}

}
