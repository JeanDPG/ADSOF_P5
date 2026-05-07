package tests;

import datasets.*;
import tests.resources.exercise1.*;
import decisionTree.*;
import java.util.*;

/**
 * TestsExercise2Main.java
 * 
 * Clase de prueba para el apartado 2
 * Se centra en construir el arbol manualmente, probar las predicciones,
 * tanto para elementos individuales como apra grupos, probar el correcto manejo
 * de situaciones de stop, y de manejo de errores.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 */
public class TestsExercise2Main {
    public static void main(String[] args) {
        testManualTreeBuilding();
        testSinglePredictions();
        testGroupPredictions();
        testStopConditions();
        testErrorHandling();
    }

    private static void testManualTreeBuilding() {
        System.out.println("--- MANUAL TREE BUILDING  ---");
        System.out.println("Scenario A: Multiple branches on one node");
        DecisionTree<Person> ageTree = new DecisionTree<>();
        ageTree.node("root")
                .withCondition("infant", p -> p.getAge() < 3)
                .withCondition("child", p -> p.getAge() < 12)
                .withCondition("teenager", p -> p.getAge() < 18)
                .otherwise("adult");
        System.out.println(ageTree);

        System.out.println("Scenario B: nested decisions");
        DecisionTree<Person> nestedTree = new DecisionTree<>();
        nestedTree.node("root")
                .withCondition("check_weight", p -> p.getAge() >= 18)
                .otherwise("child_node");

        nestedTree.node("check_weight")
                .withCondition("heavy", p -> p.weight() > 90)
                .otherwise("normal_weight");

        nestedTree.node("heavy")
                .withCondition("very_tall", p -> p.height() > 190)
                .otherwise("standard_tall");
        System.out.println(nestedTree);

        System.out.println("Scenario C: Only otherwise");
        DecisionTree<Person> simpleTree = new DecisionTree<>();
        simpleTree.node("entry").otherwise("result_label");
        System.out.println(simpleTree);

        System.out.println("Scenario D: Shared target nodes");
        DecisionTree<Person> sharedTree = new DecisionTree<>();
        sharedTree.node("root")
                .withCondition("common_result", p -> p.isMale())
                .withCondition("common_result", p -> p.getAge() > 50)
                .otherwise("other");
        sharedTree.node("common_result").otherwise("SUCCESS");
        System.out.println(sharedTree);

        System.out.println();
    }

    private static void testSinglePredictions() {
        DecisionTree<Person> deepTree = new DecisionTree<>();

        deepTree.node("root")
                .withCondition("adult", p -> p.getAge() >= 18)
                .otherwise("minor");

        deepTree.node("minor")
                .withCondition("toddler", p -> p.getAge() < 5)
                .otherwise("student");

        deepTree.node("adult")
                .withCondition("male_branch", p -> p.isMale())
                .otherwise("female_branch");

        deepTree.node("male_branch")
                .withCondition("athlete_man", p -> p.height() > 185)
                .otherwise("standard_man");

        deepTree.node("female_branch")
                .withCondition("tall_woman", p -> p.height() > 170)
                .otherwise("short_woman");

        Person jaime = new Person("Jaime", 20, 70, 180, true);
        Person Sara = new Person("Sara", 22, 65, 175, false);
        Person baby = new Person("Baby", 2, 12, 85, true);
        Person student = new Person("Teen", 15, 55, 165, false);
        Person giant = new Person("Giant", 25, 100, 210, true);

        System.out.println("Path Test results:");
        System.out.println("  - " + jaime.name() + " (20, M, 180cm): " + deepTree.predict(jaime));
        System.out.println("  - " + Sara.name() + " (22, F, 175cm): " + deepTree.predict(Sara));
        System.out.println("  - " + baby.name() + " (2, M, 85cm): " + deepTree.predict(baby));
        System.out.println("  - " + student.name() + " (15, F, 165cm): " + deepTree.predict(student));
        System.out.println("  - " + giant.name() + " (25, M, 210cm): " + deepTree.predict(giant));

        System.out.println("\nTesting dead end node logic:");
        deepTree.node("athlete_man");
        System.out.println("  - Giant in athlete_man node: " + deepTree.predict(giant));

        System.out.println();
    }

    private static void testGroupPredictions() {
        System.out.println("--- GROUP PREDICTIONS  ---");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("adult", p -> p.getAge() >= 18).otherwise("child");
        dt.node("adult").withCondition("male_adult", p -> p.isMale()).otherwise("female_adult"); 

        System.out.println("---  Diverse Varargs ---");
        Map<String, List<Person>> varargsRes = dt.predict(
                new Person("Man_1", 25, 75, 180, true),
                new Person("Man_2", 30, 80, 185, true),
                new Person("Woman_1", 22, 60, 165, false),
                new Person("Child_1", 5, 20, 100, true),
                new Person("Child_2", 8, 25, 115, false));
     
        varargsRes.forEach(
                (label, list) -> System.out.println("  Label '" + label + "': " + list.size() + " people -> " + list));

        System.out.println("\n--- Dataset Classification ---");
        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        for (int i = 0; i < 10; i++) {
            boolean isMale = (i % 2 == 0);
            int age = (i * 5); 
            ds.add(new Person("Person_" + i, age, 50, 160, isMale));
        }

        Map<String, List<Person>> datasetRes = dt.predict(ds);
        System.out.println("  Groups found in Dataset: " + datasetRes.keySet());
        datasetRes.forEach((label, list) -> System.out.println("  " + label + " has " + list.size() + " members."));

        System.out.println("\n--- Empty Inputs ---");
        Dataset<Person> emptyDs = new Dataset<>(new PersonFeaturizer());
        System.out.println("  Empty Dataset prediction size, should be 0: " + dt.predict(emptyDs).size()); 

        System.out.println("  Empty Varargs prediction size, should be 0: " + dt.predict().size()); 

        System.out.println("\n---  Consistency ---");
        Person testP = new Person("Test", 40, 80, 180, true);
        Map<String, List<Person>> res1 = dt.predict(new Person[] { testP });
        Map<String, List<Person>> res2 = dt.predict(new Person[] { testP });
        System.out.println("  Is classification consistent? " + res1.keySet().equals(res2.keySet()));
        System.out.println();
    }

    private static void testStopConditions() {
        System.out.println("---  STOP CONDITIONS & LEAVES ---");
        DecisionTree<Person> emptyTree = new DecisionTree<>();
        Person p = new Person("Test", 20, 70, 180, true);
        System.out.println("Empty tree prediction, should be null: " + emptyTree.predict(p));
        
        DecisionTree<Person> rootOnly = new DecisionTree<>();
        rootOnly.node("TheOnlyNode");
        System.out.println("Root-only tree (no conditions): " + rootOnly.predict(p)); 

        DecisionTree<Person> noPathTree = new DecisionTree<>();
        noPathTree.node("start")
                .withCondition("impossible", x -> x.getAge() > 1000);
        System.out.println("Implicit dead end, unmet condition, no otherwise, should be start: " + noPathTree.predict(p)); 

        DecisionTree<Person> leafTree = new DecisionTree<>();
        leafTree.node("root")
                .withCondition("FINAL_RESULT", x -> true);
        System.out.println("Jump to a String label (not a node), should be FINAL_RESULT: " + leafTree.predict(p)); 

        DecisionTree<Person> otherwiseLeaf = new DecisionTree<>();
        otherwiseLeaf.node("root")
                .withCondition("node_A", x -> false)
                .otherwise("LABEL_B");
        System.out.println("Jump via OTHERWISE to a String label, should be LABEL_B: " + otherwiseLeaf.predict(p)); 

        System.out.println("Predicting a NULL object:");
        DecisionTree<Person> nullSafeTree = new DecisionTree<>();
        nullSafeTree.node("root")
                .withCondition("should_not_reach", x -> x.getAge() > 0)
                .otherwise("null_fallback");
        System.out.println("   Result for null person, should be null_fallback: " + nullSafeTree.predict((Person) null));
        System.out.println();
    }

    private static void testErrorHandling() {
        System.out.println("---  ERROR HANDLING ---");
        DecisionTree<Person> dt = new DecisionTree<>();
        Node<Person> root = dt.node("root");

        System.out.println("--- DecisionTree Nodes ---");
        try {
            dt.node(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null node): " + e.getMessage());
        }
        try {
            dt.node("");
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (empty node): " + e.getMessage());
        }

        System.out.println("\n--- Node Conditions ---");
        try {
            root.withCondition(null, p -> true);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null target): " + e.getMessage());
        }
        try {
            root.withCondition("next", null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null predicate): " + e.getMessage());
        }

        System.out.println("\n---  Node Otherwise ---");
        try {
            root.otherwise("");
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (empty otherwise): " + e.getMessage());
        }

        System.out.println("\n--- Prediction Inputs ---");
        try {
            dt.predict((Dataset<Person>) null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null dataset): " + e.getMessage());
        }
        try {
            dt.predict((Person[]) null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null varargs): " + e.getMessage());
        }

        System.out.println("\n--- Path Predicates ---");
        try {
            dt.getPredicate(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null label): " + e.getMessage());
        }

        System.out.println("\n---  Condition Object ---");
        try {
            new Condition<Person>(null, p -> true);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null target in constructor): " + e.getMessage());
        }
        System.out.println();
    }
}