package decisionTree;

import java.util.*;

import datasets.Featurizer;
import datasets.LabelProvider;
import datasets.LabeledDataset;
import strategies.FeatureStrategy;
import strategies.RandomStrategy;

/**
 * GreedyTreeLearner.java
 * 
 * Clase encargada de generar un árbol de decisión a partir
 * de un dataset etiquetado, o los datos necesarios para generar dicho dataset.
 * Utiliza un algoritmo recursivo (greedy) que divide los datos según las
 * características disponibles hasta alcanzar nodos puros o agotar las opciones
 * de división.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 * @param <T> Tipo de objeto que procesará el árbol.
 * @param <L> Tipo de etiqueta que el árbol aprenderá a predecir.
 */
public class GreedyTreeLearner<T, L> {
    private FeatureStrategy<T, L> strategy = new RandomStrategy<>(); // Estrategia por defecto

    /**
     * Establece la estrategia de seleccion de caracteristicas.
     * @param strategy La estrategia a usar.
     * @return El propio learner para encadenar llamadas.
     */
    public GreedyTreeLearner<T, L> setStrategy(FeatureStrategy<T, L> strategy) {
        if (strategy != null) {
            this.strategy = strategy;
        }
        return this;
    }

    /**
     * Inicia el proceso de construcción a partir de un LabeledDataset. Obtenemos
     * toda la informacion que necesita la funcion learnRecursive
     * 
     * @param dataset El conjunto de datos de entrenamiento.
     * @return Un DecisionTree configurado.
     */
    public DecisionTree<T> learn(LabeledDataset<T, L> dataset) {
        if (dataset == null) {
            throw new IllegalArgumentException("Training dataset cannot be null.");
        }
        if (dataset.getElements().isEmpty()) {
            throw new IllegalArgumentException("Training dataset cannot be empty.");
        }
        DecisionTree<T> tree = new DecisionTree<>();
        List<String> features = new ArrayList<>(dataset.getFeaturizer().featureNames());
        learnRecursive(tree, "root", dataset, features);
        return tree;
    }

    /**
     * Versión alternativa que construye el arbol a traves de una coleccion de
     * objetos, transformandolos en un dataset etiquetado, y llamando a la funcion
     * learn
     * 
     * @param objects       Colección de objetos para crear el arbol
     * @param featurizer    El extractor de características.
     * @param labelProvider El proveedor de etiquetas.
     * @return Un DecisionTree configurado.
     */
    public DecisionTree<T> learn(Collection<T> objects, Featurizer<T> featurizer, LabelProvider<T, L> labelProvider) {
        if (objects == null || featurizer == null || labelProvider == null) {
            throw new IllegalArgumentException("Parameters for learning cannot be null.");
        }
        LabeledDataset<T, L> ds = new LabeledDataset<>(featurizer, labelProvider);
        ds.addAll(objects);
        return learn(ds);
    }

    /**
     * Método recursivo del algoritmo de construcción usando un enfoque greedy.
     * 
     * @param tree              El árbol que se está construyendo.
     * @param nodeName          Nombre del nodo actual.
     * @param dataset           Subconjunto de datos que llega a este nodo.
     * @param availableFeatures Lista de características que aún no han sido usadas.
     */
    private void learnRecursive(DecisionTree<T> tree, String nodeName,
            LabeledDataset<T, L> dataset, List<String> availableFeatures) {

        /* Obtenemos las etiquetas unicas */
        Set<L> uniqueLabels = dataset.getUniqueLabels();

        /* Si la etiqueta es unica, creamos la hoja con esa etiqueta y terminamos */
        if (uniqueLabels.size() == 1) {
            tree.node(nodeName).otherwise(uniqueLabels.iterator().next().toString());
            return;
        }
        /*
         * Si no quedan caracteristicas, creamos la hoja con la etiqueta mayoritaria,
         * llamando al metodo getMajorityLabel. Esto nos da la respuesta mas probable,
         * enlos casos en los que no se han diferenciado como queremos los datos.
         */
        if (availableFeatures.isEmpty()) {
            L majority = getMajorityLabel(dataset);
            tree.node(nodeName).otherwise(majority.toString());
            return;
        }

        /*
         * Si quedan caracteristicas, obtenemos la primera, y la eleiminamos de la
         * lista,
         * para que en la recursion no se vuelva a usar
         */
        /*
         * Usamos la estrategia para obtener la mejor caracteristica.
         * Si la estrategia no devuelve nada, usamos la primera por defecto.
         */
        String selectedFeature = strategy.execute(dataset, availableFeatures);
        if (selectedFeature == null) {
            selectedFeature = availableFeatures.get(0);
        }
        
        final String featureName = selectedFeature;
        List<String> remainingFeatures = new ArrayList<>(availableFeatures);
        remainingFeatures.remove(featureName);

        /*
         * Llamamos a split para separar los datos respecto al valor de la
         * caracteristica
         */
        Map<Object, LabeledDataset<T, L>> splits = split(dataset, featureName);
        Node<T> node = tree.node(nodeName);

        /*
         * Para cada valor de la caracteristica llamamos a la funcion recursiva usando
         * como
         * nombre del hijo el calculado, usando el dataset correspondiente, y con la
         * lista actualizada
         * de reamining features
         */
        splits.forEach((val, subDataset) -> {
            /*
             * Hayamos el nombre del nodo hijo usando el nodo actual, la caracteristica y su
             * valor
             */
            String childName = nodeName + "_" + featureName + "_" + val;

            /* Creamos la condicion para que el nodo actual llegue al hijo */
            node.withCondition(childName, item -> {
                /*
                 * Obtenemos el valor de la caracteristica y devolvemos su comparacion con val
                 */
                Object itemVal = dataset.getFeaturizer().featureValue(item, featureName);
                return itemVal != null && itemVal.equals(val);
            });

            /* LLamamos recursivamente */
            learnRecursive(tree, childName, subDataset, remainingFeatures);
        });
    }

    /**
     * Divide un dataset en varios sub-datasets basados en los valores únicos
     * de una característica específica. Creamos un mapa con clave el valor de la
     * caracteristica, y valor un labeledDataset con los elementos que comparten
     * dicho valor. Recorremos todos los elementos del dataset, y para cada uno
     * obtenemos el valor de sucaracterística.
     * Finalmente lo añadimos al mapa en su lugar correspondientte. Para ello usamos
     * computeIfAbsent, que devuelve el sub_dataset correspondiente o lo crea si no
     * existe, añadiendo a este usando la funcion add de Dataset el elemento.
     * 
     * @param ds          Dataset a dividir.
     * @param featureName Nombre de la característica.
     * @return Un mapa que asocia cada valor con su subconjunto de datos
     *         correspondiente.
     */
    private Map<Object, LabeledDataset<T, L>> split(LabeledDataset<T, L> ds, String featureName) {

        if (featureName == null || featureName.isEmpty()) {
            throw new IllegalArgumentException("Feature name for splitting cannot be null or empty.");
        }
        Map<Object, LabeledDataset<T, L>> map = new HashMap<>();
        for (T item : ds.getElements()) {
            Object val = ds.getFeaturizer().featureValue(item, featureName);
            map.computeIfAbsent(val, k -> new LabeledDataset<>(ds.getFeaturizer(), ds.getLabelProvider()))
                    .add(item);
        }
        return map;
    }

    /**
     * Calcula la etiqueta mayoritaria en un conjunto de datos.
     * Se usa como desempate cuando no se pueden realizar más divisiones.
     * Para cada elemento del dataset, obtenemos su etiqueta, e introducimos en el
     * mapa counts, usando la clave la etiqueta y el valor el contador de
     * apariciones. Usamos geTOrDefault para que nos devuelva 0 si no encuentra la
     * clave. Los valores del mapa los recorremos usando stream,, obtenemos el entry
     * con mayor contador usando compareTo, y extraemos la
     * etiqueta correspondiente, devolviendola. Si no hay ninguna etiqueta,
     * devolvemos null.
     * 
     * @param ds Dataset a analizar.
     * @return La etiqueta que más veces aparece.
     */
    private L getMajorityLabel(LabeledDataset<T, L> ds) {
        if (ds.getElements().isEmpty())
            return null;
        Map<L, Long> counts = new HashMap<>();
        for (T item : ds.getElements()) {
            L label = ds.getLabel(item);
            counts.put(label, counts.getOrDefault(label, 0L) + 1);
        }
        
        L majority = null;
        long maxCount = -1;
        for (Map.Entry<L, Long> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                majority = entry.getKey();
            }
        }
        return majority;
    }
}