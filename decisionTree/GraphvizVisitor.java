package decisionTree;

public class GraphvizVisitor<T> implements TreeVisitor<T> {
    private final DecisionTree<T> tree;
    private final StringBuilder sb = new StringBuilder();

    public GraphvizVisitor(DecisionTree<T> tree) {
        this.tree = tree;
        sb.append("digraph G {\n");
    }

    @Override
    public void visit(Node<T> node) {
        sb.append(String.format("  \"%s\" [shape=ellipse];\n", node.getName()));

        for (Condition<T> c : node.getConditions()) {
            sb.append(String.format("  \"%s\" -> \"%s\" [label=\"cond\"];\n", node.getName(), c.getTarget()));
            visitTarget(c.getTarget());
        }

        if (node.getOtherwiseTarget() != null) {
            sb.append(String.format("  \"%s\" -> \"%s\" [style=dashed, label=\"else\"];\n",
                    node.getName(), node.getOtherwiseTarget()));
            visitTarget(node.getOtherwiseTarget());
        }
    }

    @Override
    public void visit(Leaf leaf) {
        sb.append(String.format("  \"%s\" [shape=box, color=blue];\n", leaf.getLabel()));
    }

    private void visitTarget(String target) {
        Node<T> next = tree.getNodes().get(target);
        if (next != null) next.accept(this);
        else new Leaf(target).accept(this);
    }

    public String getDotCode() {
        return sb.toString() + "}";
    }
}
