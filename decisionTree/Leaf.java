package decisionTree;

public class Leaf implements Visitable {
    private final String label;

    public Leaf(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public void accept(TreeVisitor<?> visitor) {
        visitor.visit(this); // Doble dispatch
    }
}