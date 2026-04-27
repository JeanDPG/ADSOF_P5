package datasets;

import java.util.List;

public interface Featurizer<T> {

    // Lista los nombres de las columnas que este featurizer sabe extraer.
    List<String> featureNames();

    // Devuelve el valor de una columna concreta para un objeto T.
    Comparable<?> featureValue(T element, String featureName);
}
