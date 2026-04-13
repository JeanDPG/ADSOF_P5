import java.util.Map;
import java.util.*;
public class DecisionTree<T> {
    private Node<T> root;
    private Map<String, Node<T>> nodes;

    public DecisionTree(){
        this.root= null;
        this.nodes= new HashMap<>();
    }
}