package tests;

import datasets.*;
import decisionTree.*;
import tests.resources.exercise4.Temperature;
import tests.resources.exercise4.Weather;
import tests.resources.exercise4.WeatherCondition;
import tests.resources.exercise4.WeatherFeaturizer;
import java.util.*;

/**
 * TestsExercise4Main.java
 * 
 * Test para el apartado 4. Se centra en probar la funcionalidad del algoritmo,
 * casos de paradad, y manejo de errores. Tambien prueba en profundidad el dataset 
 * etiquetado
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 */
public class TestsExercise4Main {
    public static void main(String[] args) {
        testLabeledDatasetIntegrity();
        testPureDatasetLearning();
        testFeatureExhaustionWithMajority();
        testDeepRecursiveLearning();
        testLearningCollectioneMethod();
        testLearningErrorHandling();
    }

    private static void testLabeledDatasetIntegrity() {
        System.out.println("--- LABELED DATASET INTEGRITY ---");
        LabeledDataset<Weather, String> ds = new LabeledDataset<>(
            new WeatherFeaturizer(), 
            w -> w.getCondition().toString()
        );
        
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.HOT));
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.COLD));
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.MILD));

        Set<String> unique = ds.getUniqueLabels();
        System.out.println("Unique labels found: " + unique);
        System.out.println("Unique size check (should be 2): " + unique.size());
        System.out.println();
    }

    private static void testPureDatasetLearning() {
        System.out.println("--- PURE DATASET LEARNING ---");
        LabeledDataset<Weather, Boolean> ds = new LabeledDataset<>(
            new WeatherFeaturizer(), 
            w -> true 
        );
        ds.addAll(new Weather[]{
            new Weather(WeatherCondition.RAINY, Temperature.COLD),
            new Weather(WeatherCondition.SUNNY, Temperature.HOT)
        });

        GreedyTreeLearner<Weather, Boolean> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(ds);

        System.out.println("Pure Tree (Expected 1 root node pointing to 'true'):");
        System.out.println(tree);
        System.out.println();
    }

    private static void testFeatureExhaustionWithMajority() {
        System.out.println("--- FEATURE EXHAUSTION & MAJORITY ---");
        
        Featurizer<Weather> blindFeaturizer = new Featurizer<>() {
            public List<String> featureNames() { return Collections.emptyList(); }
            public Comparable<?> featureValue(Weather e, String n) { return null; }
        };

        LabeledDataset<Weather, String> ds = new LabeledDataset<>(blindFeaturizer, w -> {
            if (w.getTemperature() == Temperature.HOT) return "YES";
            return "NO";
        });
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.HOT)); 
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.HOT)); 
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.COLD)); 

        GreedyTreeLearner<Weather, String> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(ds);

        System.out.println("Majority Decision (Expected 'YES'): " + tree.predict(new Weather(WeatherCondition.CLOUDY, Temperature.MILD)));
        System.out.println();
    }

    private static void testDeepRecursiveLearning() {
        System.out.println("--- DEEP RECURSIVE LEARNING ---");
        
        LabeledDataset<Weather, String> ds = new LabeledDataset<>(
            new WeatherFeaturizer(), 
            w -> w.getCondition() == WeatherCondition.SUNNY && w.getTemperature() == Temperature.HOT ? "BEACH" : "STAY_HOME"
        );
        
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.HOT)); 
        ds.add(new Weather(WeatherCondition.SUNNY, Temperature.COLD)); 
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.HOT));  
        ds.add(new Weather(WeatherCondition.RAINY, Temperature.COLD)); 

        GreedyTreeLearner<Weather, String> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(ds);

        System.out.println("Deep Tree Structure:");
        System.out.println(tree);
        System.out.println("Prediction Sunny/Hot: " + tree.predict(new Weather(WeatherCondition.SUNNY, Temperature.HOT)));
        System.out.println("Prediction Rainy/Cold: " + tree.predict(new Weather(WeatherCondition.RAINY, Temperature.COLD)));
        System.out.println();
    }

    private static void testLearningCollectioneMethod() {
        System.out.println("--- COLLECTION LEARNINGH METHOD ---");
        List<Weather> weatherList = List.of(
            new Weather(WeatherCondition.SUNNY, Temperature.HOT),
            new Weather(WeatherCondition.RAINY, Temperature.COLD)
        );

        GreedyTreeLearner<Weather, Boolean> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(weatherList, new WeatherFeaturizer(), w -> true);

        System.out.println("Tree built from list: " + (tree != null));
        System.out.println();
    }

    private static void testLearningErrorHandling() {
        System.out.println("--- LEARNING ERROR HANDLING ---");
        GreedyTreeLearner<Weather, Boolean> learner = new GreedyTreeLearner<>();

        try {
            System.out.println("Testing learn(null)...");
            learner.learn(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        try {
            System.out.println("Testing learn(empty dataset)...");
            LabeledDataset<Weather, Boolean> empty = new LabeledDataset<>(new WeatherFeaturizer(), w -> true);
            learner.learn(empty);
        } catch (IllegalArgumentException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        try {
            System.out.println("Testing LabeledDataset with null LabelProvider...");
            new LabeledDataset<Weather, Boolean>(new WeatherFeaturizer(), null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }
        System.out.println();
    }
}
