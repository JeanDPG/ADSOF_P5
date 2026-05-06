package datasets;

import java.util.List;

/**
 * Featurizer.java
 * 
 * Interfaz que define los métodos necesarios para extraer características
 * de un objeto de tipo T, para convertirlo en datos para el dataset. Esta interfaz
 * permite que el dataset pueda adaptarse a cualquier tipo de objeto, y actua como traductor 
 * entre objeto y dataset
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> El tipo de objeto a procesar
 */
public interface Featurizer<T> {

    /**
     * Lista los nombres de las columnas que el featurizer sabe extraer.
     * @return Lista con los nombres de las características.
     */
    List<String> featureNames();

    /**
     * Devuelve el valor de una columna concreta cuyo nombre nos pasan como parametro
     *  para un objeto T.
     * @param element El objeto del cual extraer la característica.
     * @param featureName El nombre de la característica deseada.
     * @return El valor de la característica como un Comparable.
     */
    Comparable<?> featureValue(T element, String featureName);
}
