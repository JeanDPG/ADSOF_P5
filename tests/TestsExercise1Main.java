package tests;
import datasets.*;
import tests.resources.exercise1.Person;
import tests.resources.exercise1.PersonFeaturizer;

import java.util.*;

public class TestsExercise1Main {
    public static void main(String[] args) {
        testBasicFunctionality();
        testDuplicateRemoval();
        testEdgeCases();
        testErrorHandling();
    }

    private static void testBasicFunctionality() {
        System.out.println("=== TEST 1: BASIC FUNCTIONALITY ===");
        Person p1 = new Person("Jaime", 20, 70.5, 180, true);
        Person p2 = new Person("Jean", 22, 65.0, 175, true);
        
        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.add(p1);
        ds.add(p2);

        System.out.println("Dataset content: " + ds);
        
        Feature<Integer> ages = ds.feature("age");
        System.out.println("Ages: " + ages);
        System.out.println("Min age: " + ages.min());
        System.out.println("Max age: " + ages.max());
        System.out.println("Age distribution: " + ages.distribution());
        System.out.println();
    }

    private static void testDuplicateRemoval() {
        System.out.println("=== TEST 2: DUPLICATE REMOVAL ===");
        Person p1 = new Person("Original", 30, 80.0, 190, true);
        Person p2 = new Person("Duplicate Values", 30, 80.0, 190, true); 
        Person p3 = new Person("Different", 25, 60.0, 160, false);

        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.addAll(new Person[]{p1, p2, p3});

        System.out.println("Before removal (3 elements): " + ds.getElements().size());
        ds.removeDuplicates();
        System.out.println("After removal (Should be 2): " + ds.getElements().size());
        System.out.println("Final Dataset: " + ds);
        System.out.println();
    }

    private static void testEdgeCases() {
        System.out.println("=== TEST 3: EDGE CASES ===");
        Dataset<Person> emptyDs = new Dataset<>(new PersonFeaturizer());
        
        System.out.println("Empty Dataset: " + emptyDs);
        
        try {
            Feature<Integer> emptyAges = emptyDs.feature("age");
            System.out.println("Empty feature min: " + emptyAges.min());
        } catch (NoSuchElementException e) {
            System.out.println("Caught expected error: Cannot get min of empty feature.");
        }
        
        Feature<Double> weights = new Feature<>();
        weights.add(70.0);
        weights.add(70.0);
        weights.add(85.5);
        System.out.println("Distribution of [70, 70, 85.5]: " + weights.distribution());
        System.out.println();
    }

    private static void testErrorHandling() {
        System.out.println("=== TEST 4: ERROR HANDLING ===");
        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.add(new Person("Test", 20, 70, 170, true));

        System.out.println("Testing Type Token version: " + ds.feature("age", Integer.class));

        try {
            System.out.println("Requesting unknown feature...");
            ds.feature("non_existent_column");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        try {
            System.out.println("Requesting age as Double (Wrong cast)...");
            Feature<Double> wrongType = ds.feature("age");
            Double val = wrongType.get(0); 
        } catch (ClassCastException e) {
            System.out.println("Caught expected error: Cannot cast Integer to Double.");
        }
        System.out.println();
    }
}