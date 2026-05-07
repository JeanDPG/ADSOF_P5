package datasets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Feature.java
 * 
 * Esta clase representa una columna de datos. Extiende ArrayList para almacenar
 * los valores y permite obtener el minimo, el maximo y la distribucion de esos
 * valores.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <S> Tipo de los datos almacenados, debe ser Comparable.
 */
public class Feature<S extends Comparable<? super S>> extends ArrayList<S> {

    /**
     * Busca el menor valor de la columna, cosa que podemos porque S extiende a
     * Comparable.
     * 
     * @return El valor mínimo presente en la característica.
     */
    public S min() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException("Cannot compute min on an empty feature.");
        }
        return Collections.min(this);
    }

    /**
     * Busca el mayor valor de la columna usando el orden natural del tipo,
     * cosa que podemos porque S extiende a Comparable.
     * 
     * @return El valor máximo presente en la característica.
     */
    public S max() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException("Cannot compute max on an empty feature.");
        }
        return Collections.max(this);
    }

    /**
     * Cuenta cuántas veces aparece cada valor dentro de la columna. Usamos un mapa, ya que es perfecto
     * para almacenar eld ato y las veces de aparicion. Recorremos todos los elementos del Feature,
     * y usamos merge para ir sumando las apariciones de cada valor. Merge busca en el mapa el valor,
     * si no aparece, le asocia el valor 1, y si lo encuentra usamos una funcion lambda para sumar 1.
     * 
     * @return Un mapa con las frecuencias de cada valor.
     */
    public Map<S, Integer> distribution() {
        Map<S, Integer> frequencies = new LinkedHashMap<>();
        if (this.isEmpty())
            return frequencies;
        for (S value : this) {
            frequencies.merge(value, 1, (oldValue, newValue) -> oldValue + newValue);
        }
        return frequencies;
    }
}
