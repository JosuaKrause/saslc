package stefan;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class Cout {

	private static void write(final HashSet<CommonNode> drawn, final Writer w,
			final CommonNode n) throws IOException {

		if (drawn.add(n)) {
			switch (n.getKind()) {

			case prim:
				switch (n.getPrim()) {
				case S:
					w.append('S');
					break;
				case K:
					w.append('K');
					break;
				case Y:
					w.append('Y');
					break;
				case C:
					w.append('C');
					break;
				case I:
					w.append('I');
					break;
				case U:
					w.append('U');
					break;
				case Neg:
					w.append('~');
					break;
				case Add:
					w.append('+');
					break;
				case Sub:
					w.append('-');
					break;
				case Mul:
					w.append('*');
					break;
				case Div:
					w.append('/');
					break;
				case Mod:
					w.append('%');
					break;
				case Not:
					w.append('!');
					break;
				case And:
					w.append('&');
					break;
				case Or:
					w.append('|');
					break;
				case Cond:
					w.append('?');
					break;
				case Hd:
					w.append('h');
					break;
				case Tl:
					w.append('t');
					break;
				case Cons:
					w.append(':');
					break;
				case Lt:
					w.append("L");
					break;
				case Gt:
					w.append("G");
					break;
				case Le:
					w.append("l");
					break;
				case Ge:
					w.append("g");
					break;
				case Ne:
					w.append("n");
					break;
				case Eq:
					w.append("e");
					break;
				case Seq:
					w.append("s");
					break;
				default:
					throw new UnsupportedOperationException(
							"Unknown primitive: " + n.getPrim() + ".");
				}
				break;

			case num:
				w.append('i').append(n.getNum().toString());
				break;

			case str:
				w.append('"').append(
						n.getStr().replace("\\", "\\\\").replace("\"", "\\\""))
						.append('"');
				break;

			case chr:
				w.append('\'').append(
						String.valueOf(Character.toChars(n.getChar())));
				break;

			case name:
				w.append('<').append(n.getName()).append('>');
				break;

			case bln:
				w.append(n.getBln() ? 'T' : 'F');
				break;

			case lst: {
				w.append("_");
				final Stack<CommonNode> s = new Stack<CommonNode>();
				for (CommonNode i = n; !i.isEmpty(); i = i.getTail()) {
					s.push(i.getHead());
				}
				while (!s.empty()) {
					write(drawn, w, s.pop());
					w.append(',');
				}
				break;
			}

			case app:
				write(drawn, w, n.getRight());
				write(drawn, w, n.getLeft());
				w.append('@');
				break;

			default:
				throw new UnsupportedOperationException("Cannot write node("
						+ n.getId() + ") of kind " + n.getKind() + ".");
			}
		} else {
			throw new UnsupportedOperationException("Graph has cycle at "
					+ n.getId() + ".");
		}

	}

	public static void module(final CommonNode n, final Writer w)
			throws IOException {

		if (n.getKind() != Kind.module) {
			throw new UnsupportedOperationException("Root node is "
					+ n.getKind() + ", expected module instead.");
		}

		final HashSet<CommonNode> drawn = new HashSet<CommonNode>();
		for (final Map.Entry<String, ? extends CommonNode> e : (new TreeMap<String, CommonNode>(
				n.getDefs())).entrySet()) {
			write(drawn, w, e.getValue());
			w.write("=" + e.getKey() + ";");
		}
		w.append("\n").close();
	}

}