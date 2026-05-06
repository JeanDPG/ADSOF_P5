package decisionTree;
import java.util.function.Predicate;

/**
 * Condition.java
 * 
 * Representa una regla dentro de un nodo del árbol, o dicho de otra forma es la flecha 
 * que une dos nodos. Contiene el nombre del nodo al que saltar si se cumple al condicion, 
 * y usamos Predicate para hacer que la logica sea lo mas flexible posible.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> El tipo de objeto que se va a evaluar.
 */
public class Condition<T> {
    
    /** Nombre del nodo al que ir si se cumple la condición */
    private final String target;
    /** Predicado lógico que se debe evaluar */
    private final Predicate<T> predicate;

    /**
     * Constructor de la condición.
     * @param target Nombre del nodo destino si se cumple la condición.
     * @param prediction El predicado lógico que se debe evaluar.
     */
    public Condition(String target, Predicate<T> prediction) {
        this.target = target;
        this.predicate = prediction;
    }

    /**
     * Evalúa si un elemento cumple la lógica de esta condición. usamos predicate.test
     * @param element El objeto a probar.
     * @return true si cumple el predicado, false en caso contrario.
     */
    public boolean test(T element) {
        return predicate.test(element);
    }

    /**
     * Obtiene el nombre del nodo destino al que ir si se cumple la condición.
     * @return El nombre del nodo siguiente.
     */
    public String getTarget(){
        return target;
    }

    /**
     * Devuelve el predicado lógico, que usaremos para construir predicados compuestos.
     * @return El predicado de la condición.
     */
    public Predicate<T> getPredicate() {
    return predicate;
    }
}
