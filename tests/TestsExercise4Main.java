package tests;

import datasets.*;
import decisionTree.*;
import treeLearner.*;
import java.util.*;

/**
 * TestsExercise4Main.java
 * 
 * Clase de prueba para la Sección 4. Verifica el aprendizaje automático 
 * de árboles de decisión a partir de datasets etiquetados, cubriendo 
 * casos estándar, conjuntos de datos puros y límites de características.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 */
public class TestsExercise4Main {
    public static void main(String[] args) {
        testStandardLearning();
        testLearningFromCollection();
        testPureDataset();
        testMajorityLabelFallback();
    }

    /**
     * Test 1: Aprendizaje estándar con el ejemplo del Clima/Tenis.
     * Verifica que el árbol aprende a predecir basándose en el tiempo.
     */
    private static void testStandardLearning() {
        System.out.println("=== TEST 1: STANDARD LEARNING (WEATHER/TENNIS) ===");
        
        LabeledDataset<Weather, Boolean> ds = new LabeledDataset<>(
            new WeatherFeaturizer(), 
            new ShouldIPlayTennisToday()
        );
        
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.HOT));
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.COLD));
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.HOT));
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.COLD));

        GreedyTreeLearner<Weather, Boolean> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(ds);

        System.out.println("Learned Tree Structure:");
        System.out.println(tree);

        Weather testRainy = new Weather(WeatherCondition.RAINY, Temperature.MILD);
        Weather testSunny = new Weather(WeatherCondition.SUNNY, Temperature.MILD);

        System.out.println("Prediction for Rainy day (Expected false): " + tree.predict(testRainy));
        System.out.println("Prediction for Sunny day (Expected true): " + tree.predict(testSunny));
        System.out.println();
    }

    /**
     * Test 2: Prueba la sobrecarga del método learn que recibe una colección directa.
     */
    private static void testLearningFromCollection() {
        System.out.println("=== TEST 2: LEARNING FROM COLLECTION (CONVENIENCE METHOD) ===");
        
        List<Weather> list = List.of(
            new Weather(WeatherCondition.RAINY, Temperature.HOT),
            new Weather(WeatherCondition.SUNNY, Temperature.COLD)
        );

        GreedyTreeLearner<Weather, Boolean> learner = new GreedyTreeLearner<>();
        // Usamos la versión que no requiere crear el LabeledDataset manualmente
        DecisionTree<Weather> tree = learner.learn(list, new WeatherFeaturizer(), new ShouldIPlayTennisToday());

        System.out.println("Tree learned from collection: " + (tree != null ? "SUCCESS" : "FAIL"));
        System.out.println("Prediction (Rainy): " + tree.predict(list.get(0)));
        System.out.println();
    }

    /**
     * Test 3: Verifica el caso de un dataset "puro" (todas las etiquetas son iguales).
     * El árbol debería tener un solo nodo final (hoja).
     */
    private static void testPureDataset() {
        System.out.println("=== TEST 3: PURE DATASET (ALL SAME LABELS) ===");
        
        LabeledDataset<Weather, String> ds = new LabeledDataset<>(
            new WeatherFeaturizer(), 
            w -> "ALWAYS_YES" // LabelProvider que siempre devuelve lo mismo
        );
        
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.COLD));
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.HOT));

        GreedyTreeLearner<Weather, String> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(ds);

        System.out.println("Pure Tree Structure (Should be just a leaf):");
        System.out.println(tree);
        System.out.println();
    }

    /**
     * Test 4: Verifica el comportamiento cuando se agotan las características
     * pero los datos siguen mezclados (debe elegir la etiqueta mayoritaria).
     */
    private static void testMajorityLabelFallback() {
        System.out.println("=== TEST 4: MAJORITY LABEL FALLBACK ===");
        
        // Creamos un featurizer que no devuelve NADA (lista vacía de features)
        Featurizer<Weather> emptyFeaturizer = new Featurizer<>() {
            public List<String> featureNames() { return Collections.emptyList(); }
            public Comparable<?> featureValue(Weather e, String n) { return null; }
        };

        LabeledDataset<Weather, String> ds = new LabeledDataset<>(emptyFeaturizer, w -> {
            // Etiquetamos manualmente para forzar una mayoría
            if (w.getCondition() == WeatherCondition.RAINY) return "RAIN_LABEL";
            return "SUN_LABEL";
        });

        // 2 días de lluvia vs 1 de sol -> Ganará RAIN_LABEL por mayoría
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.COLD));
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.HOT));
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.HOT));

        GreedyTreeLearner<Weather, String> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(ds);

        System.out.println("Majority Tree Structure:");
        System.out.println(tree);
        System.out.println("Predicted majority: " + tree.predict(new Weather(WeatherCondition.CLOUDY, Temperature.MILD)));
        System.out.println();
    }
}
