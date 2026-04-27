import java.util.Map;
import java.util.*;

public class DecisionTree<T> {
    private Map<String, Node<T>> nodes = new LinkedHashMap<>();
    private String rootName;

    
    public Node<T> node(String name) {
        if (rootName == null) rootName = name;
        if (!nodes.containsKey(name)) {
            nodes.put(name, new Node<>(name));
        }
        return nodes.get(name);
    }

    
    public String predict(T element) {
        if (rootName == null) return null;
        
        Node<T> current = nodes.get(rootName);
        // Seguimos navegando mientras el nodo tenga condiciones o un otherwise
        while (current != null && !current.getConditions().isEmpty() || current.getOtherwiseTarget() != null) {
            String nextName = current.nextNode(element);
            current = nodes.get(nextName);
            
            // Si el siguiente nodo es una hoja (resultado final), salimos
            if (current != null && current.getConditions().isEmpty() && current.getOtherwiseTarget() == null) {
                return current.getName();
            }
        }
        return current != null ? current.getName() : null;
    }

   
    public Map<String, List<T>> predict(Dataset<T> dataset) {
        Map<String, List<T>> results = new LinkedHashMap<>();
        for (T item : dataset.getElements()) {
            String label = predict(item);
            results.computeIfAbsent(label, k -> new ArrayList<>()).add(item);
        }
        return results;
    }
}
