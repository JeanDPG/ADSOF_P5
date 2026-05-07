package tests;

import decisionTree.*;
import tests.resources.exercise1.*;
import java.util.function.Predicate;

/**
 * TestsExercise3Main.java
 * 
 * Clase de prueba para el apartado 3 es decir genración de predicados.
 * Se centra en validar la extracción de lógica del árbol
 * mediante la conjunción de condiciones y la negación de ramas otherwise.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 */
public class TestsExercise3Main {
    public static void main(String[] args) {
        testSimplePredicate();
        testComplexPathPredicate();
        testOtherwiseNegation();
        testDeepNestedPredicate();
        testOtherwiseInNestedNode();
        testNonExistentLabel();
    }

    private static void testSimplePredicate() {
        System.out.println("--- SIMPLE PREDICATE -");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("adult", p -> p.getAge() >= 18);

        Predicate<Person> isAdult = dt.getPredicate("adult");
        
        Person p1 = new Person("Adult", 20, 70, 180, true);
        Person p2 = new Person("Child", 10, 30, 120, true);

        System.out.println("Testing 'adult' predicate:");
        System.out.println("  - Is 20yo an adult? " + isAdult.test(p1)); 
        System.out.println("  - Is 10yo an adult? " + isAdult.test(p2)); 
        System.out.println();
    }

    private static void testComplexPathPredicate() {
        System.out.println("--- TEST 2: COMPLEX PATH ---");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("adult_node", p -> p.getAge() >= 18);
        dt.node("adult_node").withCondition("male_adult", p -> p.isMale());

        Predicate<Person> isMaleAdult = dt.getPredicate("male_adult");

        Person p1 = new Person("Jaime", 20, 70, 180, true);
        Person p2 = new Person("Sara", 22, 65, 175, false);
        Person p3 = new Person("Boy", 10, 30, 120, true);

        System.out.println("Testing 'male_adult' (Age >= 18 AND Male):");
        System.out.println("  - Jaime (20, M): " + isMaleAdult.test(p1));
        System.out.println("  - Sara (22, F): " + isMaleAdult.test(p2)); 
        System.out.println("  - Boy (10, M): " + isMaleAdult.test(p3));  
        System.out.println();
    }

    private static void testOtherwiseNegation() {
        System.out.println("--- OTHERWISE NEGATION LOGIC ---");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root")
          .withCondition("infant", p -> p.getAge() < 3)
          .withCondition("child", p -> p.getAge() < 12)
          .otherwise("senior_or_adult");

        Predicate<Person> isNotChild = dt.getPredicate("senior_or_adult");

        System.out.println("Testing 'otherwise' (NOT < 3 AND NOT < 12):");
        System.out.println("  - Baby (1yo): " + isNotChild.test(new Person("A", 1, 10, 60, true)));  
        System.out.println("  - Kid (8yo): " + isNotChild.test(new Person("B", 8, 25, 110, true)));  
        System.out.println("  - Adult (30yo): " + isNotChild.test(new Person("C", 30, 75, 175, true))); 
        System.out.println();
    }

    private static void testDeepNestedPredicate() {
        System.out.println("---  DEEP PATH ---");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("level1", p -> p.getAge() > 10);
        dt.node("level1").withCondition("level2", p -> p.isMale());
        dt.node("level2").withCondition("level3", p -> p.weight() > 60);
        dt.node("level3").withCondition("target", p -> p.height() > 170);

        Predicate<Person> deepPred = dt.getPredicate("target");

        Person athlete = new Person("Athlete", 25, 80, 185, true);
        Person lightMan = new Person("Light", 25, 50, 185, true);

        System.out.println("Testing deep path union:");
        System.out.println("  - Athlete (Matches all): " + deepPred.test(athlete)); 
        System.out.println("  - Light Man (Fails weight): " + deepPred.test(lightMan)); 
        System.out.println();
    }

    private static void testOtherwiseInNestedNode() {
        System.out.println("---  OTHERWISE INSIDE COMPLEX PATH ---");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("adult_branch", p -> p.getAge() >= 18);
        
        dt.node("adult_branch")
          .withCondition("man", p -> p.isMale())
          .otherwise("woman_label");

        Predicate<Person> isWoman = dt.getPredicate("woman_label");

        System.out.println("Testing nested otherwise (Age >= 18 AND NOT Male):");
        System.out.println("  - sARA (22, F): " + isWoman.test(new Person("Sara", 22, 60, 170, false))); 
        System.out.println("  - Jaime (20, M): " + isWoman.test(new Person("Jaime", 20, 70, 180, true))); 
        System.out.println("  - Child (8, F): " + isWoman.test(new Person("Child", 8, 20, 120, false))); 
        System.out.println();
    }

    private static void testNonExistentLabel() {
        System.out.println("--- ERROR HANDLING---");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").otherwise("end");

        try {
            System.out.println("Searching for non-existent label...");
            Predicate<Person> ghost = dt.getPredicate("ghost");
            System.out.println("  Result: " + (ghost == null ? "null (Correct)" : "Not null (Fail)"));
        } catch (Exception e) {
            System.out.println("  Caught unexpected error: " + e.getMessage());
        }
        
        try {
            System.out.println("Searching for null label...");
            dt.getPredicate(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error (null label): " + e.getMessage());
        }
        System.out.println();
    }
}
