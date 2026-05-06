package datasets;

/**
 * LabelProvider.java
 * 
 * Interfaz funcional con un ùnico metodo que devuelve la etiqueta asociada a un 
 * elemento de tipo T. La etiqueta resultante tambien es genérica. Esta etiqueta 
 * es la que usaremos para que el arbol aprenda. 
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> El tipo de objeto a etiquetar.
 * @param <L> El tipo de la etiqueta resultante 
 */
@FunctionalInterface
public interface LabelProvider<T, L> {
    /**
     * Devuelve la etiqueta asociada a un elemento concreto.
     * @param element El objeto del cual queremos obtener la etiqueta.
     * @return La etiqueta correspondiente al objeto.
     */
    L getLabel(T element);
}
