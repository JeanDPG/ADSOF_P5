package datasets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class Dataset<T> {

    private final Featurizer<T> featurizer;
    private final List<T> data = new ArrayList<>();

    // El dataset necesita un featurizer para saber que atributos puede leer de T.
    public Dataset(Featurizer<T> featurizer) {
        this.featurizer = featurizer;
    }

    // Inserta un solo objeto en el dataset.
    public void add(T element) {
        data.add(element);
    }

    // Inserta varios objetos de una vez.
    public void addAll(Collection<? extends T> elements) {
        data.addAll(elements);
    }

    // Elimina repetidos comparando los valores de las features de cada fila.
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

    // Devuelve una columna sin tipado explicito, util para mostrar el dataset.
    public List<Comparable<?>> feature(String field) {
        List<Comparable<?>> values = new ArrayList<>();
        for (T element : data) {
            values.add(featurizer.featureValue(element, field));
        }
        return values;
    }

    // Construye una columna tipada del dataset indicando el tipo esperado.
    public <S extends Comparable<? super S>> Feature<S> feature(String field, Class<S> type) {
        Feature<S> values = new Feature<>();
        for (T element : data) {
            values.add(type.cast(featurizer.featureValue(element, field)));
        }
        return values;
    }

    private List<Comparable<?>> rowValues(T element) {
        List<Comparable<?>> row = new ArrayList<>();
        for (String featureName : featurizer.featureNames()) {
            row.add(featurizer.featureValue(element, featureName));
        }
        return row;
    }

    @Override
    // Muestra el dataset por columnas, como en el ejemplo del enunciado.
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        for (String featureName : featurizer.featureNames()) {
            joiner.add(featureName + "=" + feature(featureName));
        }
        return joiner.toString();
    }
}
