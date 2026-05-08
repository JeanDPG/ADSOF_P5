package tests;

import datasets.*;
import decisionTree.*;
import strategies.*;
import tests.resources.exercise1.*;
import java.util.*;

public class TestsExercise5Main {
    public static void main(String[] args) {
        testStrategiesIndividually();
        testLearnerWithStrategies();
    }

    private static void testStrategiesIndividually() {
        System.out.println("--- STRATEGIES INDIVIDUAL TEST ---");
        List<String> features = List.of("age", "weight", "gender");
        
        // No necesitamos datos reales para RandomStrategy
        RandomStrategy<Person, String> random = new RandomStrategy<>();
        String picked = random.execute(null, features);
        System.out.println("Random strategy picked: " + picked);
        System.out.println("Is valid feature? " + features.contains(picked));

        // Para MisclassificationStrategy necesitamos datos
        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        // Caso: Gender predice perfectamente, Age no (misma edad, distinto genero).
        ds.add(new Person("P1", 20, 70, 180, true));  // Age 20, M -> Label A
        ds.add(new Person("P2", 20, 70, 180, false)); // Age 20, F -> Label B
        
        LabeledDataset<Person, String> lds = new LabeledDataset<>(new PersonFeaturizer(), p -> p.isMale() ? "A" : "B");
        lds.addAll(ds.getElements());

        MisclassificationStrategy<Person, String> misclass = new MisclassificationStrategy<>();
        String best = misclass.execute(lds, features);
        System.out.println("Misclassification strategy picked: " + best);
        System.out.println("Is gender? " + "gender".equals(best));
        System.out.println();
    }

    private static void testLearnerWithStrategies() {
        System.out.println("--- LEARNER WITH STRATEGIES ---");
        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.add(new Person("P1", 20, 70, 180, true));
        ds.add(new Person("P2", 20, 70, 180, false));
        
        LabeledDataset<Person, String> lds = new LabeledDataset<>(new PersonFeaturizer(), p -> p.isMale() ? "MALE_LABEL" : "FEMALE_LABEL");
        lds.addAll(ds.getElements());

        System.out.println("Testing Learner with MisclassificationStrategy:");
        GreedyTreeLearner<Person, String> learner = new GreedyTreeLearner<Person, String>()
                .setStrategy(new MisclassificationStrategy<>());
        
        DecisionTree<Person> tree = learner.learn(lds);
        System.out.println("Tree root (should be gender-based):");
        System.out.println(tree);

        // Verificar que la prediccion funciona
        Person testP = new Person("Test", 20, 70, 180, true);
        System.out.println("Prediction for Male: " + tree.predict(testP));
        System.out.println();
    }
}
