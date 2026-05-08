package decisionTree;

// Interfaz Visitor (del diagrama)
public interface TreeVisitor<T> {
    void visit(Node<T> node);
    void visit(Leaf leaf);
}
