package xi.ast;

import static xi.ast.BuiltIn.Y;
import static xi.util.StringUtils.NL;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * 
 * @author Leo
 * @author Joschi
 * 
 */
public class Module extends Node {
	/** Module definitions. */
	private final Map<Name, Expr> defs;

	private final boolean isTopLevel;

	public Module(final boolean isTopLevel) {
		this(isTopLevel, false);
		if (isTopLevel) {
			try {
				addDefinition(Name.valueOf("hd"), BuiltIn.HD);
				addDefinition(Name.valueOf("tl"), BuiltIn.TL);
			} catch (final Exception e) {
				throw new Error("Internal Error", e);
			}
		}
	}

	private Module(final boolean isTopLevel, final boolean copy) {
		this.isTopLevel = isTopLevel;
		defs = new TreeMap<Name, Expr>();
	}

	public void addDefinition(final Name n, final Expr v) throws Exception {
		if (defs.containsKey(n)) {
			throw new Exception("duplicate name: " + n);
		}
		defs.put(n, v);
	}

	public Expr getForName(final Name name) {
		return defs.get(name);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		boolean removeEnd = false;
		for (final Entry<Name, Expr> def : defs.entrySet()) {
			sb.append(def.getKey()).append(" = ").append(def.getValue());
			if (isTopLevel) {
				sb.append(';').append(NL);
			} else {
				sb.append(", ");
				removeEnd = true;
			}
		}
		if (removeEnd) {
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}

	@Override
	public boolean hasFree(final Name var) {
		if (defs.containsKey(var)) {
			return false;
		}
		boolean inDefs = false;
		for (final Entry<Name, Expr> def : defs.entrySet()) {
			if (def.getKey().equals(var)) {
				return false;
			}
			inDefs |= def.getValue().hasFree(var);
		}
		return inDefs;
	}

	public Map<Name, Expr> getMap() {
		return defs;
	}

	@Override
	public Module unLambda() {
		final Module res = new Module(isTopLevel, true);
		for (final Entry<Name, Expr> e : defs.entrySet()) {
			final Name name = e.getKey();
			try {
				Expr nw = e.getValue().unLambda();

				// eliminate simple recursion
				if (nw.hasFree(name)) {
					nw = Y.app(new Lambda(name.toString(), nw).unLambda());
				}
				res.addDefinition(name, nw);
			} catch (final Exception e1) {
				// TODO rethrow exception, not done now for debugging purposes
				e1.printStackTrace();
			}
		}
		return res;
	}
}
