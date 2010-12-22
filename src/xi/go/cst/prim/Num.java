package xi.go.cst.prim;

import xi.go.cst.Node;

public class Num extends Value {

	private final int value;

	public Num(final int value) {
		this.value = value;
	}

	@Override
	public int getNum() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public boolean eq(final Node n) {
		return value == n.getNum();
	}
}
