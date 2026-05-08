package decisionTree;

// Interfaz Element (del diagrama)
public interface Visitable {
    void accept(TreeVisitor<?> visitor);
}

