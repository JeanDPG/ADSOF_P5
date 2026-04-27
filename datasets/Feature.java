package datasets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Feature<S extends Comparable<? super S>> extends ArrayList<S> {

    // Busca el menor valor de la columna usando el orden natural del tipo.
    public S min() {
        return Collections.min(this);
    }

    // Busca el mayor valor de la columna usando el orden natural del tipo.
    public S max() {
        return Collections.max(this);
    }

    // Cuenta cuantas veces aparece cada valor dentro de la columna.
    public Map<S, Integer> distribution() {
        Map<S, Integer> frequencies = new LinkedHashMap<>();
        for (S value : this) {
            frequencies.merge(value, 1, Integer::sum);
        }
        return frequencies;
    }
}
