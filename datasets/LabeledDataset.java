package datasets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LabeledDataset.java
 * 
 * Clase que extiende de Dataset y que incluye un label provider para poder obtener las 
 * etiquetas asociadas a cada objeto, y así permitir a un arbol aprender. Incluye un metodo para obtener
 * todas las etiquetas unicas, para que el algoritmo sepa cuando un conjunto de datos no necesita 
 * mas divisiones .
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> El tipo de los objetos contenidos en el dataset.
 * @param <L> El tipo de la etiqueta asociada a cada objeto.
 */
public class LabeledDataset<T, L> extends Dataset<T> {
    /** El proveedor de etiquetas asociado */
    private final LabelProvider<T, L> labelProvider;

    /**
     * Constructor para un dataset etiquetado.
     * @param featurizer El extractor de características.
     * @param labelProvider El proveedor de etiquetas para los objetos.
     */
    public LabeledDataset(Featurizer<T> featurizer, LabelProvider<T, L> labelProvider) {
        super(featurizer);
        this.labelProvider = labelProvider;
    }

    /**
     * Obtiene la etiqueta específica para un elemento dado.
     * @param element El objeto a consultar.
     * @return La etiqueta asociada al objeto.
     */
    public L getLabel(T element) {
        return labelProvider.getLabel(element);
    }

    /**
     * Identifica todos los valores únicos de etiquetas presentes en el dataset actual.
     * Obtenemos a traves de la llamada al metodo getElements todos los elementos del dataset. 
     * Con streamss, recorremos todos los elementos, y para cada uno obtenemos usando una lambda, 
     * su label asociada. Finalmente con collect, las recogemos e intentamos meterlas en un set.
     * Esto hace que se eliminen las etiquetas duplicadas.
     * @return Un set con las etiquetas únicas.
     */
    public Set<L> getUniqueLabels() {
        return getElements().stream()
                .map(element -> getLabel(element))
                .collect(Collectors.toSet());
    }

    /**
     * Devuelve el proveedor de etiquetas asociado.
     * @return El objeto LabelProvider.
     */
    public LabelProvider<T, L> getLabelProvider() {
        return labelProvider;
    }
}
