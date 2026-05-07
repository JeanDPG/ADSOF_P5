package datasets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Dataset.java
 * 
 * Esta clase es un contenedor de objetos de tipo T guardados en un array,
 * y que tambien contiene un Featurizer que es el que nos permite obtener las
 * columnas de
 * datos, es decir las Features. Tambien puede eliminar duplicados como se
 * explicara en dicho metodo. No existe ningun metodo para eliminar elementos aparte de este, 
 * ya que hemos considerado que el enunciado no lo pedía. 
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> Tipo de los objetos contenidos en el dataset.
 */
public class Dataset<T> {

    /* Featurizer que usara el dataset */
    private final Featurizer<T> featurizer;
    /* Array con los objetos que contiene el dataset */
    private final List<T> data = new ArrayList<>();

    /**
     * Constructor para la clase Dataset.
     * 
     * @param featurizer El featurizer asociado al dataset, que extraera las Features.
     */
    public Dataset(Featurizer<T> featurizer) {
        if (featurizer == null) {
            throw new IllegalArgumentException("Featurizer cannot be null.");
        }
        this.featurizer = featurizer;
    }

    /**
     * Inserta un solo objeto en el dataset.
     * 
     * @param element El objeto a añadir.
     */
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("You cannot add a null element to the dataset.");
        }
        data.add(element);
    }

    /**
     * Inserta una colección de objetos en el dataset. Usamos collection para
     * no limitar lo que nos pueda pasar el usuario a por ejemplo una lista.
     * Ademas usamos ? extends T, para permitir pasar objetos hijos de la clase T
     * 
     * @param elements Colección de objetos a añadir.
     */
    public void addAll(Collection<? extends T> elements) {
        if (elements == null) {
            throw new IllegalArgumentException("The element collection cannot be null.");
        }
        data.addAll(elements);
    }

    /**
     * Añade un array de elementos al dataset. Lo transformamos a una lista
     * 
     * @param elements Array de objetos a añadir.
     */
    public void addAll(T[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("The array of elements cannot be null.");
        }
        this.data.addAll(Arrays.asList(elements));
    }

    /**
     * Obtiene el featurizer asociado al dataset.
     * 
     * @return El objeto Featurizer.
     */
    public Featurizer<T> getFeaturizer() {
        return this.featurizer;
    }

    /**
     * Devuelve una vista inmutable de los elementos del dataset.
     * 
     * @return Lista no modificable de elementos.
     */
    public List<T> getElements() {
        return Collections.unmodifiableList(data);
    }

    /**
     * Elimina elementos repetidos basándose en los valores de las filas de un elemento
     * usando unicamente los valores que extrae el featurizer. Tenemos primero una lista
     * generica de objetos T, uniqueData, que está vacia al principio. También, un set de
     * filas. Para cada objeto T de el atributo data, obtenemos su fila, y la intentamos
     * añadir al set, que no permite duplicados. Asi, unicamente si la fila no estaba ya,
     * se introduce el objeto T element. Finalmente limpiamos data y añadimos los objetos
     * de uniqueData.
     */
    public void removeDuplicates() {
        List<T> uniqueData = new ArrayList<>();
        Set<List<Comparable<?>>> seenRows = new LinkedHashSet<>();

        for (T element : data) {
            List<Comparable<?>> row = rowValues(element);
            if (seenRows.add(row)) {
                uniqueData.add(element);
            }
        }

        data.clear();
        data.addAll(uniqueData);
    }

    /**
     * Extrae una columna de datos. Para cada objeto T de data, obtenemos su valor
     * de la caracteristica con el nombre name, y lo anadimos a la Feature. El
     * casteo, que genera un warning y nos obliga a usar el @SuppressWarnings, es
     * para mantener la flexibilidad, y permitir que el dataset no necesite saber
     * de que tipos son los datos
     * 
     * @param <V>  Tipo de los datos de la columna.
     * @param name Nombre de la característica.
     * @return Objeto Feature con los valores de la columna.
     */
    @SuppressWarnings("unchecked")
    public <V extends Comparable<? super V>> Feature<V> feature(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The feature name cannot be null or empty.");
        }
        Feature<V> feature = new Feature<>();
        for (T element : data) {
            feature.add((V) featurizer.featureValue(element, name));
        }
        return feature;
    }

    /**
     * Este metodo, como el anterior, extrae una columna de datos. En este caso si
     * recibimos como argumento el tipo de dato que queremos que tenga la Feature.
     * 
     * @param <S>  Tipo de los datos de la columna.
     * @param name Nombre de la característica.
     * @param type Clase del tipo de dato esperado.
     * @return Objeto Feature con los valores de la columna.
     */
    public <V extends Comparable<? super V>> Feature<V> feature(String name, Class<V> type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The feature name cannot be null or empty.");
        }
        if (type == null) {
            throw new IllegalArgumentException("The class type cannot be null.");
        }
        Feature<V> values = new Feature<>();
        for (T element : data) {
            values.add(type.cast(featurizer.featureValue(element, name)));
        }
        return values;
    }

    /**
     * Obtiene la lista de valores de características para un objeto concreto, es decir
     * la fila. Recorre todos los features names, para obtener su valor para el elemento.
     * la lista resultante tiene que ser comparable
     * 
     * @param element El objeto a procesar.
     * @return Lista de valores comparables de todas sus características.
     */
    private List<Comparable<?>> rowValues(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot extract features from a null element.");
        }
        List<Comparable<?>> row = new ArrayList<>();
        for (String featureName : featurizer.featureNames()) {
            row.add(featurizer.featureValue(element, featureName));
        }
        return row;
    }

    /**
     * Devuelve una representación en cadena de los datos del dataset.
     * Usamos un String Joiner, usando como separador una coma, y recorriendo
     * los nombres de las columnas y sus valores.
     * 
     * @return Representación en cadena de los datos del dataset.
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        for (String featureName : featurizer.featureNames()) {
            joiner.add(featureName + "=" + feature(featureName));
        }
        return joiner.toString();
    }
}
