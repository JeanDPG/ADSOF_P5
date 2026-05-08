package decisionTree;

public class PlainTextVisitor<T> implements TreeVisitor<T> {
    private final DecisionTree<T> tree;
    private final StringBuilder sb = new StringBuilder();
    private int depth = 0;

    public PlainTextVisitor(DecisionTree<T> tree) {
        this.tree = tree;
    }

    @Override
    public void visit(Node<T> node) {
        appendIndent();
        sb.append("Node: ").append(node.getName()).append("\n");
        depth++;

        for (Condition<T> c : node.getConditions()) {
            appendIndent();
            sb.append("|-- ").append(c.getTarget()).append("\n");
            visitTarget(c.getTarget());
        }

        if (node.getOtherwiseTarget() != null) {
            appendIndent();
            sb.append("L-- [otherwise] ").append(node.getOtherwiseTarget()).append("\n");
            visitTarget(node.getOtherwiseTarget());
        }
        depth--;
    }

    @Override
    public void visit(Leaf leaf) {
        appendIndent();
        sb.append("Result -> ").append(leaf.getLabel()).append("\n");
    }

    private void visitTarget(String targetName) {
        Node<T> next = tree.getNodes().get(targetName);
        if (next != null) {
            next.accept(this);
        } else {
            new Leaf(targetName).accept(this);
        }
    }

    private void appendIndent() {
        for (int i = 0; i < depth; i++) sb.append("  ");
    }

    public String getResult() {
        return sb.toString();
    }
}

