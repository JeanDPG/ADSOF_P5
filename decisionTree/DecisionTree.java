package decisionTree;

import java.util.*;
import datasets.Dataset;
import java.util.function.Predicate;

/**
 * DecisionTree.java
 * 
 * Clase principal que gestiona la estructura completa del árbol de decisión.
 * Permite construir el árbol, realizar predicciones y generar predicados complejos.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> El tipo de objeto que el árbol es capaz de clasificar.
 */
public class DecisionTree<T> {
    private Map<String, Node<T>> nodes = new LinkedHashMap<>();
    private String rootName;

    /**
     * Crea un nuevo nodo si no existe aun, y lo devuelve.
     * El primer nodo creado se establece automáticamente como raíz.
     * 
     * @param name Nombre del nodo.
     * @return El objeto Node.
     */
    public Node<T> node(String name) {
        if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("Node name cannot be null or empty.");
        }
        if (rootName == null)
            rootName = name;
        if (!nodes.containsKey(name)) {
            nodes.put(name, new Node<>(name));
        }
        return nodes.get(name);
    }

    /**
     * Predice la etiqueta de clasificación para un objeto de tipo T recorriendo los nodos.
     * La navegación se detiene cuando llegamos a un nombre que no existe como nodo
     * o cuando un nodo no tiene más caminos posibles.
     * 
     * @param element El objeto a clasificar.
     * @return La etiqueta final asignada por el árbol.
     */
    public String predict(T element) {
        if (rootName == null)
            return null;

        Node<T> current = nodes.get(rootName);
        if (current == null) return null;
        while (current != null) {
            String nextName = current.nextNode(element);

            if (nextName == null)
                return current.getName();

            Node<T> nextNode = nodes.get(nextName);
            if (nextNode == null)
                return nextName;

            current = nextNode;
        }
        return null;
    }

    /**
     * Clasifica todos los elementos contenidos en un Dataset. Guardamos los resultados
     * en un mapa con clave la prediccion, y valor una lista de elementos de tipo T. Recorre todos los elementos del 
     * dataset, y para cada uno llama a la prediccion. Hemos usado el metodo computeIfAbsent,
     * que devuelve la lista de la prediccion del elemento si existe, o la crea si no exite, usando una 
     * funcion lambda. En cualquiera de los dos casos añade el elemento a la lista asociada a la prediccion
     * correspondiente.
     * 
     * @param dataset El dataset con los elementos a procesar.
     * @return Un mapa agrupando los elementos por cada etiqueta resultante.
     */
    public Map<String, List<T>> predict(Dataset<T> dataset) {
        if (dataset == null) {
        throw new IllegalArgumentException("Dataset cannot be null.");
        }
        Map<String, List<T>> results = new LinkedHashMap<>();
        dataset.getElements().forEach(item -> {
        String label = predict(item);
        results.computeIfAbsent(label, k -> new ArrayList<>()).add(item);
        });
        return results;
    }

    /**
     * Al igual que la anterior, crea un mapa con etiquetas de prediccion, y los elementos 
     * que pertenecen a dicha prediccion en un array. En este caso el parametro de entrada es un 
     * un conjunto variable de elementos de tipo T, que permite pasar los objetos separados en comas
     * en vez de tener que añadirlos a un dataset antes. Sin embargo esto produce un warning, y para 
     * evitarlo usamos @SafeVarargs. Tiene la misma logica que la funcion anterior.
     *
     * @param elements Objetos individuales a clasificar.
     * @return Un mapa con los resultados agrupados por etiquetas.
     */
    @SafeVarargs
    public final Map<String, List<T>> predict(T... elements) {
        if (elements == null) {
        throw new IllegalArgumentException("Elements array cannot be null.");
        }
        Map<String, List<T>> results = new LinkedHashMap<>();
        for (T element : elements) {
            String label = predict(element);
            results.computeIfAbsent(label, k -> new ArrayList<>()).add(element);
        }
        return results;
    }

    /**
     * Crea un predicado que resuma el camino que es necesario recorrer para llegar a una etiqueta, y que
     * devuelva true si el objeto pertenece a esa etiqueta.
     * Combina todas las condiciones del camino mediante la operación AND.
     * Utiliza la función recursiva findPredicate para recorrer el árbol.
     * 
     * @param label La etiqueta final que queremos comprobar.
     * @return Un Predicate que valida si un objeto pertenece a esa clasificación.
     */
    public Predicate<T> getPredicate(String label) {
        if (label == null || label.isEmpty()) {
        throw new IllegalArgumentException("Label cannot be null or empty.");
        }
        return findPredicate(rootName, label, x -> true);
    }

    /**
     * Método recursivo para buscar una etiqueta y acumular las condiciones del
     * camino. Voy a explicar paso por paso ssu funcioanmiento
     * 
     */
    private Predicate<T> findPredicate(String current, String target, Predicate<T> path) {
        /* Este es el caso base de la recursion, cuando llegamos a la etiqueta que 
        buscabamos. Devolvemos en este caso el Predicate path */
        if (current.equals(target))
            return path;
        /* Si el nodo actual no existe, devolvemos null */
        Node<T> node = nodes.get(current);
        if (node == null)
            return null;

        /* Recrremos dado un nodo válido, todas sus condiciones. en cada una de ellas llamamos a 
        la funcion recursiva cambiando current por el target del nodo, y el path por path.and(c.getPredicate())
        Si alguno de los predicados de esa llamada devuelve algo que no sea null, ese es el path que buscabamos */
        for (Condition<T> c : node.getConditions()) {
            Predicate<T> found = findPredicate(c.getTarget(), target, path.and(c.getPredicate()));
            if (found != null)
                return found;
        }

        /*Si ninguna de las condiciones del nodo nos han llevado a target, tenemos que cambiar el 
        path que enviamos a la funcion sumando la negacion de todas las concidiones del nodo. 
        Entonces llamamos y devolvemos el resultado de la funcion recursiva con el current, otherwiseTarget, y
        path.and(otherwsePred) */
        if (node.getOtherwiseTarget() != null) {
            Predicate<T> otherwisePred = x -> true;
            for (Condition<T> c : node.getConditions()) {
                otherwisePred = otherwisePred.and(c.getPredicate().negate());
            }
            return findPredicate(node.getOtherwiseTarget(), target, path.and(otherwisePred));
        }
        return null;
    }

    /**
     * Obtiene todos los nodos del árbol.
     * @return Mapa de nombres de nodos a objetos Node.
     */
    public Map<String, Node<T>> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    /**
     * Obtiene el nombre del nodo raíz.
     * @return El nombre del nodo raíz.
     */
    public String getRootName() {
        return rootName;
    }

    @Override
public String toString() {
    StringBuilder sb = new StringBuilder("Decision Tree Structure:\n");
    if (rootName != null) {
        sb.append("Root node: ").append(rootName).append("\n");
    }
    for (Node<T> node : nodes.values()) {
        sb.append(node.toString());
    }
    return sb.toString();
}
}
