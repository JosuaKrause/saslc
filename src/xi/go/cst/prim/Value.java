package xi.go.cst.prim;

import java.io.IOException;
import java.io.Writer;

public abstract class Value extends Prim {

	@Override
	public boolean isValue() {
		return true;
	}

	@Override
	public void eval(final Writer w) throws IOException {
		w.append(toString());
	}

}
