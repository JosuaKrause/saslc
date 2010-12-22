package xi.go.cst.prim;

import xi.go.cst.Node;
import xi.go.cst.Thunk;

public class Bool extends Value {

	public static final Bool TRUE = new Bool();

	public static final Bool FALSE = new Bool();

	public static final Thunk TRUE_THUNK = new Thunk(TRUE);

	public static final Thunk FALSE_THUNK = new Thunk(FALSE);

	private Bool() {
		/* void */
	}

	@Override
	public boolean getBool() {
		return this == TRUE;
	}

	public static Bool get(final boolean b) {
		return b ? TRUE : FALSE;
	}

	@Override
	public String toString() {
		return Boolean.toString(getBool());
	}

	@Override
	public boolean eq(final Node n) {
		return this == n;
	}

}
