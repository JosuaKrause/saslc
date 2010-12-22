package xi.go;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Queue;

public class GlueReader extends Reader {

	private final Queue<Reader> in;

	private final char glue;

	public GlueReader() {
		this('\n');
	}

	public GlueReader(final char glue) {
		this.glue = glue;
		in = new LinkedList<Reader>();
	}

	public void addReader(final Reader r) {
		in.add(r);
	}

	@Override
	public void close() throws IOException {
		while (!in.isEmpty()) {
			in.poll().close();
		}
	}

	@Override
	public int read() throws IOException {
		if (in.isEmpty()) {
			return -1;
		}
		final int res = in.peek().read();
		if (res != -1) {
			return res;
		}
		in.poll().close();
		return glue;
	}

	@Override
	public int read(final char[] cbuf, final int off, final int len)
			throws IOException {
		if (in.isEmpty()) {
			return -1;
		}
		for (int pos = 0; pos < len; ++pos) {
			final int c = read();
			if (c == -1) {
				return pos;
			}
			cbuf[pos] = (char) c;
		}
		return len;
	}

}
