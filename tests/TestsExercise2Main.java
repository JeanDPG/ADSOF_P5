package tests;
import datasets.*;
import tests.resources.exercise1.*;
import decisionTree.*;
import java.util.*;

public class TestsExercise2Main {
    public static void main(String[] args) {
        testManualTreeBuilding();
        testSinglePredictions();
        testGroupPredictions();
        testStopConditions();
    }

    private static void testManualTreeBuilding() {
        System.out.println("=== TEST 1: MANUAL TREE BUILDING (FLUID API) ===");
        DecisionTree<Person> dt = new DecisionTree<>();
        
        // Construcción usando la API fluida
        dt.node("root")
          .withCondition("adult", p -> p.getAge() >= 18)
          .otherwise("child");
          
        dt.node("adult")
          .withCondition("male_adult", p -> p.isMale())
          .otherwise("female_adult");

        // Imprimimos el árbol (requiere los toString que añadimos antes)
        System.out.println(dt);
        System.out.println();
    }

    private static void testSinglePredictions() {
        System.out.println("=== TEST 2: SINGLE PREDICTIONS ===");
        DecisionTree<Person> dt = buildTestTree();
        
        Person p1 = new Person("Jaime", 20, 70, 180, true);   // Debería ser male_adult
        Person p2 = new Person("Child", 10, 30, 120, false); // Debería ser child
        Person p3 = new Person("Jean", 22, 65, 175, false);  // Debería ser female_adult

        System.out.println(p1.name() + " is classified as: " + dt.predict(p1));
        System.out.println(p2.name() + " is classified as: " + dt.predict(p2));
        System.out.println(p3.name() + " is classified as: " + dt.predict(p3));
        System.out.println();
    }

    private static void testGroupPredictions() {
        System.out.println("=== TEST 3: GROUP PREDICTIONS (DATASET & VARARGS) ===");
        DecisionTree<Person> dt = buildTestTree();
        
        // Caso A: Usando Varargs (T...)
        System.out.println("Testing Varargs Prediction:");
        Map<String, List<Person>> varargsRes = dt.predict(
            new Person("A", 25, 70, 180, true),
            new Person("B", 5, 20, 100, true),
            new Person("C", 30, 60, 160, false)
        );
        varargsRes.forEach((label, list) -> System.out.println("  " + label + ": " + list.size() + " people"));

        // Caso B: Usando un Dataset
        System.out.println("\nTesting Dataset Prediction:");
        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.add(new Person("D", 40, 90, 185, true));
        ds.add(new Person("E", 35, 75, 170, true));
        
        Map<String, List<Person>> datasetRes = dt.predict(ds);
        System.out.println("  Results: " + datasetRes);
        System.out.println();
    }

    private static void testStopConditions() {
        System.out.println("=== TEST 4: STOP CONDITIONS & LEAVES ===");
        DecisionTree<Person> dt = new DecisionTree<>();
        
        // Creamos un árbol que se detiene en un nodo que NO tiene ni condiciones ni otherwise
        dt.node("start").withCondition("dead_end", p -> p.getAge() > 50);
        dt.node("dead_end"); // Este nodo no tiene nada, es un callejón sin salida
        
        Person pOld = new Person("Old Man", 60, 70, 170, true);
        
        System.out.println("Prediction for Old Man (should stop at 'dead_end'): " + dt.predict(pOld));
        
        // Caso: Árbol vacío
        DecisionTree<Person> emptyTree = new DecisionTree<>();
        System.out.println("Prediction for empty tree: " + emptyTree.predict(pOld));
        System.out.println();
    }

    // Método auxiliar para no repetir código
    private static DecisionTree<Person> buildTestTree() {
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("adult", p -> p.getAge() >= 18).otherwise("child");
        dt.node("adult").withCondition("male_adult", p -> p.isMale()).otherwise("female_adult");
        return dt;
    }
}