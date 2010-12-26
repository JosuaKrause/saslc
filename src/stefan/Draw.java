package stefan;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Draw {

    private static void draw(final Set<CommonNode> drawn, final Writer w,
            final CommonNode n) throws IOException {

        if (drawn.add(n)) {
            w.append(Integer.toString(n.getId()));
            switch (n.getKind()) {

            case prim:
                w.append(" [label=\"" + n.getPrim()
                        + "\" shape=box, color=\"#002dd6\", "
                        + "style=filled, fillcolor=\"#002dd620\"];\n");
                break;

            case num:
                w.append(" [label=\"" + n.getNum()).append(
                        "\", color=\"#d61e00\", style=filled, "
                                + "fillcolor=\"#d61e0020\"];\n");
                break;

            case bln:
                w.append(" [label=\"" + Boolean.toString(n.getBln())).append(
                        "\", color=\"#ffbf00\", style=filled, "
                                + "fillcolor=\"#ffbf0020\"];\n");
                break;

            case str:
                w.append(" [label=\"\\\"").append(n.getStr()).append(
                        "\\\"\", color=\"#8baeb8\"];\n");
                break;

            /*
             * case chr: { char c = n.getChr(); w.append(" [label=\"'" + c +
             * "'\", color=\"#0000cc\"];\n"); break;}
             */

            case lst:
                if (n.isEmpty()) {
                    w.append(" [label=\"nil\", color=\"#2caa00\", "
                            + "style=filled, fillcolor=\"#2caa0040\"];\n");
                } else {
                    w.append(" [label=\"cons\", color=\"#2caa00\", "
                            + "style=filled, fillcolor=\"#2caa0040\"];\n");
                    w.append(n.getId() + " -> " + n.getHead().getId()
                            + " [arrowhead=odot, color=\"#00880c\"];\n");
                    w.append(n.getId() + " -> " + n.getTail().getId()
                            + " [arrowhead=dot, color=\"#00880c\"];\n");
                    draw(drawn, w, n.getHead());
                    draw(drawn, w, n.getTail());
                }
                break;

            case app:
                w.write(" [label=\""
                        + "@\", fontsize=10, color=\"#888888\"];\n");
                w.append(n.getId() + " -> " + n.getLeft().getId()
                        + " [arrowhead=onormal];\n");
                w.append(n.getId() + " -> " + n.getRight().getId()
                        + " [arrowhead=normal];\n");
                draw(drawn, w, n.getLeft());
                draw(drawn, w, n.getRight());
                break;

            case name:
                w.write(" [label=\"" + n.getName()
                        + "\", shape=diamond, style=filled, "
                        + "fillcolor=\"#ffbf00\"];\n");
                break;

            case lam:
                w.write(" [label=\"λ" + n.getName()
                        + "\", shape=diamond, style=filled, "
                        + "fillcolor=\"#ffbf00\"]");
                w.append(n.getId() + " -> " + n.getBody().getId()
                        + " [arrowhead=odot];\n");
                draw(drawn, w, n.getBody());
                break;

            case let:

                w.write(" [shape=record, label=\"");
                for (final Map.Entry<String, ? extends CommonNode> e : n
                        .getDefs().entrySet()) {
                    w.write("<" + e.getValue().getId() + "> " + e.getKey()
                            + " | ");
                }

                w.write("<in> ⋄\", style=filled, fillcolor=\"#ffbf00\"];\n");

                for (final Map.Entry<String, ? extends CommonNode> e : n
                        .getDefs().entrySet()) {
                    w.write(n.getId() + ":" + e.getValue().getId() + " -> "
                            + e.getValue().getId() + "[arrowhead=odot];\n");
                    draw(drawn, w, e.getValue());
                }

                w.write(n.getId() + ":<in> -> " + n.getBody().getId()
                        + "[arrowhead=dot];\n");
                draw(drawn, w, n.getBody());
                break;

            default:
                throw new UnsupportedOperationException(
                        "Cannot draw node of kind " + n.getKind() + ".");
            }
        }

    }

    public static void module(final Map<String, ? extends CommonNode> ds,
            final String name) throws IOException {

        final Set<CommonNode> drawn = new HashSet<CommonNode>();
        final Writer w = name != null ? new OutputStreamWriter(
                new FileOutputStream(name), "UTF-8") : new OutputStreamWriter(
                System.out, "UTF-8");
        w.write("digraph P {\n");

        for (final Map.Entry<String, ? extends CommonNode> e : ds.entrySet()) {
            w.write(e.getKey() + " [style=filled, fillcolor=\"#ffbf00\"];\n"
                    + e.getKey() + " -> " + e.getValue().getId()
                    + " [arrowhead=odot];\n");
            draw(drawn, w, e.getValue());
        }

        w.write("}\n");
        w.close();

    }

    public static void state(final CommonNode top,
            final Stack<? extends CommonNode> stack, final String name)
            throws IOException {

        final Set<CommonNode> drawn = new HashSet<CommonNode>();
        final Writer w = new OutputStreamWriter(new FileOutputStream(name),
                "UTF-8");
        w.write("digraph P {\n");

        draw(drawn, w, top);

        int last = -1;
        for (final CommonNode n : Collections.list(stack.elements())) {
            draw(drawn, w, n);
            if (last > -1) {
                w.write(n.getId() + ":e -> " + last
                        + " [penwidth=2.5, color=\"#c60aa8\", "
                        + "arrowhead=vee];\n");
            }
            last = n.getId();
        }

        w.write("stack [shape=point, color=\"#c60aa8\"];\n");
        w.write("stack -> " + top.getId()
                + " [penwidth=2.5, color=\"#c60aa8\", arrowhead=vee];\n");
        if (last > -1) {
            w.write(top.getId() + " -> " + last
                    + " [penwidth=2.5, color=\"#c60aa8\", arrowhead=vee];\n");
        }

        w.write("}\n");
        w.close();

    }

}
