package tests;

import decisionTree.*;
import tests.resources.exercise1.*;
import java.util.function.Predicate;

public class TestsExercise3Main {
    public static void main(String[] args) {
        testSimplePredicate();
        testComplexPathPredicate();
        testOtherwiseNegation();
        testNonExistentLabel();
    }

    private static void testSimplePredicate() {
        System.out.println("=== TEST 1: SIMPLE PREDICATE ===");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("adult", p -> p.getAge() >= 18);

        Predicate<Person> isAdult = dt.getPredicate("adult");
        
        Person p1 = new Person("Adult", 20, 70, 180, true);
        Person p2 = new Person("Child", 10, 30, 120, true);

        System.out.println("Testing 'adult' predicate:");
        System.out.println("  Is 20yo an adult? " + isAdult.test(p1)); // true
        System.out.println("  Is 10yo an adult? " + isAdult.test(p2)); // false
        System.out.println();
    }

    private static void testComplexPathPredicate() {
        System.out.println("=== TEST 2: COMPLEX PATH (AND LOGIC) ===");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").withCondition("adult_node", p -> p.getAge() >= 18);
        dt.node("adult_node").withCondition("male_adult", p -> p.isMale());

        // Este predicado debe ser: (Edad >= 18 AND esVarón)
        Predicate<Person> isMaleAdult = dt.getPredicate("male_adult");

        Person p1 = new Person("Jaime", 20, 70, 180, true);  // Cumple ambos
        Person p2 = new Person("Jean", 22, 65, 175, false);  // Falla el segundo
        Person p3 = new Person("Boy", 10, 30, 120, true);    // Falla el primero

        System.out.println("Testing 'male_adult' (Age >= 18 AND Male):");
        System.out.println("  Jaime (20, M): " + isMaleAdult.test(p1)); // true
        System.out.println("  Jean (22, F): " + isMaleAdult.test(p2));  // false
        System.out.println("  Boy (10, M): " + isMaleAdult.test(p3));   // false
        System.out.println();
    }

    private static void testOtherwiseNegation() {
        System.out.println("=== TEST 3: OTHERWISE NEGATION LOGIC ===");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root")
          .withCondition("infant", p -> p.getAge() < 3)
          .withCondition("child", p -> p.getAge() < 12)
          .otherwise("senior_or_adult");

        // El predicado de 'senior_or_adult' debe ser: (NOT < 3 AND NOT < 12)
        Predicate<Person> isNotChild = dt.getPredicate("senior_or_adult");

        Person p1 = new Person("Baby", 1, 10, 60, true);
        Person p2 = new Person("Kid", 8, 25, 110, false);
        Person p3 = new Person("Adult", 30, 75, 175, true);

        System.out.println("Testing 'otherwise' (NOT < 3 AND NOT < 12):");
        System.out.println("  Baby (1yo): " + isNotChild.test(p1));  // false
        System.out.println("  Kid (8yo): " + isNotChild.test(p2));   // false
        System.out.println("  Adult (30yo): " + isNotChild.test(p3)); // true
        System.out.println();
    }

    private static void testNonExistentLabel() {
        System.out.println("=== TEST 4: NON-EXISTENT LABEL ===");
        DecisionTree<Person> dt = new DecisionTree<>();
        dt.node("root").otherwise("end");

        Predicate<Person> ghost = dt.getPredicate("non_existent");
        System.out.println("Predicate for non-existent label: " + ghost); // null
        System.out.println();
    }
}
