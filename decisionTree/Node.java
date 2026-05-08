package decisionTree;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.Collections;

/**
 * Node.java
 * 
 * Representa un punto de decisión en el árbol. 
 * Contiene una lista de condiciones que se evalúan en orden y un 
 * camino por defecto (otherwise) en caso de que ninguna se cumpla, para evitar bloqueos.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> El tipo de objeto que procesa el nodo.
 */
public class Node<T> implements Visitable {
    /** Condiciones para saber hacia donde ir */
    private List<Condition<T>> conditions;
    /** Nombre del nodo que lo identifica */
    private String name;
    /**Evita que se quede atrapado, si no se cumple ninguna condicion */
    private String otherwise;

    /**
     * Constructor del nodo.
     * @param name Nombre identificador del nodo.
     */
    public Node(String name) {
        if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("Node name cannot be null or empty.");
    }
        this.name = name;
        this.conditions = new ArrayList<>();
        this.otherwise = null;
    }

    /**
     * Añade una nueva rama de decisión al nodo, creando una nueva condicion y alamacenandola 
     * en la lista de condiciones.
     * @param targetNode Nombre del nodo al que ir si se cumple.
     * @param predicate Lógica que debe cumplir el objeto.
     * @return El propio nodo. Nos permite encadenar llamadas.
     */
    public Node<T> withCondition(String targetNode, Predicate<T> predicate) {
        if (targetNode == null || targetNode.isEmpty()) {
        throw new IllegalArgumentException("Target node name cannot be null or empty.");
    }
    if (predicate == null) {
        throw new IllegalArgumentException("Condition predicate cannot be null.");
    }
        this.conditions.add(new Condition<>(targetNode, predicate));
        return this;
    }

     /**
     * Define el camino a seguir si no se cumple ninguna condición anterior.
     * @param target Nombre del nodo o etiqueta de destino.
     * @return El propio nodo para permitir encadenar llamadas.
     */
    public Node<T> otherwise(String target) {
        if (target == null || target.isEmpty()) {
        throw new IllegalArgumentException("Otherwise target cannot be null or empty.");
    }
        this.otherwise = target;
        return this;
    }

     /**
     * Determina cuál es el siguiente nodo para un elemento concreto.
     * Evalúa las condiciones en el orden en que fueron añadidas.
     * No lanzamos una excepcion si no hay otherwise, ya que consideramos que esto significa
     * que el nodo es una hoja, cuando se llame desde el metodo predict.
     * @param element El objeto que esta recorriendo el arbol
     * @return El nombre del siguiente nodo, o el valor de otherwise, o null si se detiene.
     */
    public String nextNode(T element){
        if (element == null) return otherwise; 
        for (Condition<T> c : conditions){
            if(c.test(element)){
                return c.getTarget();
            }
        }
        return otherwise; 
        }

    /**
     * Obtiene el nombre del nodo.
     * @return El nombre identificador.
     */
    public String getName(){
        return name;
    }

     /**
     * Obtiene el destino por defecto.
     * @return El nombre del destino otherwise
     */
    public String getOtherwiseTarget(){
        return otherwise;
    }

    /**
     * Devuelve la lista de condiciones de forma inmutable.
     * @return Lista de objetos Condition.
     */
    public List<Condition<T>> getConditions(){
        return Collections.unmodifiableList(this.conditions);
    }

    /**
     * Permite que un visitante procese este nodo.
     * @param visitor El visitante que procesara el nodo.
     */
    public void accept(TreeVisitor<?> visitor) {
        @SuppressWarnings("unchecked")
        TreeVisitor<T> v = (TreeVisitor<T>) visitor;
        v.visit(this);
    }

    @Override
public String toString() {
    StringBuilder sb = new StringBuilder("  Node [" + name + "]:\n");
    for (Condition<T> c : conditions) {
        sb.append("    |-- IF (condition) -> GO TO: ").append(c.getTarget()).append("\n");
    }
    if (otherwise != null) {
        sb.append("    |-- OTHERWISE -> GO TO: ").append(otherwise).append("\n");
    }
    return sb.toString();
}
}
