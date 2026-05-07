package tests;

import datasets.*;
import tests.resources.exercise1.Person;
import tests.resources.exercise1.PersonFeaturizer;

import java.util.*;

/**
 * TestsExercise2Main.java
 * 
 * Clase de prueba para el apartado 1.
 * Se centra en probar de forma general que se cumplen los requisitos, eliminar duplicados correctamente, 
 * y que se maneja correctamente en los casos limites o de error.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 */
public class TestsExercise1Main {
    public static void main(String[] args) {
        testBasicFunctionality();
        testDuplicateRemoval();
        testEdgeCases();
        testErrorHandling();
    }

    private static void testBasicFunctionality() {
        System.out.println("--- BASIC FUNTCIONALITY ---");
        Person p1 = new Person("Jaime", 20, 67.5, 180, true);
        Person p2 = new Person("Jean", 22, 65.0, 178, true);
        Person p3 = new Person("", 33, 64, 178, false);
        Person p4 = new Person("Marco", 34, 63, 178, false);
        Person p5 = new Person("Luis", 34, 63, 178, false);

        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.add(p1);
        ds.add(p2);
        ds.add(p3);
        ds.add(p4);
        ds.add(p5);

        System.out.println("Dataset content: " + ds);

        Feature<Integer> ages = ds.feature("age");
        System.out.println("Ages: " + ages);
        System.out.println("Min age: " + ages.min());
        System.out.println("Max age: " + ages.max());
        System.out.println("Age distribution: " + ages.distribution());
        Feature<Double> weights = ds.feature("weight", Double.class);
        System.out.println("Weights: " + weights);
        System.out.println("Min weight: " + weights.min());
        System.out.println("Max weight: " + weights.max());
        System.out.println("Weight distribution: " + weights.distribution());
        System.out.println();
    }

    private static void testDuplicateRemoval() {
        System.out.println("--- DUPLICATE REMOVAL ---");
        Person p1 = new Person("Original", 30, 80.0, 190, true);
        Person p2 = new Person("Duplicate Values", 30, 80.0, 190, true);
        Person p3 = new Person("Different", 25, 60.0, 160, false);

        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.addAll(new Person[] { p1, p2, p3 });

        System.out.println("Before removal (3 elements): " + ds.getElements().size());
        System.out.println("Original Dataset: " + ds);
        ds.removeDuplicates();
        System.out.println("After removal (Should be 2): " + ds.getElements().size());
        System.out.println("Final Dataset: " + ds);
        System.out.println();

        Person p4 = new Person("Original2", 30, 80.0, 190, true);
        Person p5 = new Person("Original3", 30, 80.1, 190, true);
        Person p6 = new Person("Original4", 25, 60.0, 160, false);

        Dataset<Person> ds2 = new Dataset<>(new PersonFeaturizer());
        ds2.addAll(List.of(p4, p5, p6));

        System.out.println("Before removal (3 elements): " + ds2.getElements().size());
        System.out.println("Original Dataset: " + ds2);
        ds2.removeDuplicates();
        System.out.println("After removal (Should be 3): " + ds2.getElements().size());
        System.out.println("Final Dataset: " + ds2);
        System.out.println();

        /*
         * Comprobamos si la diferencia en altura, que no es una caracteristica en
         * PersonFeaturizer, afecta al resultado
         */
        Person p7 = new Person("Original5", 30, 80.0, 190, true);
        Person p8 = new Person("Original6", 30, 80.0, 195, true);

        Dataset<Person> ds3 = new Dataset<>(new PersonFeaturizer());
        ds3.addAll(Arrays.asList(p7, p8));

        System.out.println("Before removal (2 elements): " + ds3.getElements().size());
        System.out.println("Original Dataset: " + ds3);
        ds3.removeDuplicates();
        System.out.println("After removal (Should be 1): " + ds3.getElements().size());
        System.out.println("Final Dataset: " + ds3);
        System.out.println();
    }

    private static void testEdgeCases() {
        System.out.println("--- EDGE CASES ---");
        Dataset<Person> emptyDs = new Dataset<>(new PersonFeaturizer());

        System.out.println("Empty Dataset: " + emptyDs);
        Feature<Integer> emptyAges = emptyDs.feature("age");
        try {

            System.out.println("Empty feature min: " + emptyAges.min());
        } catch (NoSuchElementException e) {
            System.out.println("Caught expected error: Cannot get min of empty feature.");
        }
        try {
            System.out.println("Empty feature max: " + emptyAges.max());
        } catch (NoSuchElementException e) {
            System.out.println("Caught expected error: Cannot get max of empty feature.");
        }
        Map<Integer, Integer> dist = emptyAges.distribution();
        System.out.println("Distribución vacía: " + dist);

        Feature<Double> weights = new Feature<>();
        weights.add(70.0);
        weights.add(70.0);
        weights.add(85.5);
        System.out.println("Distribution of [70, 70, 85.5]: " + weights.distribution());
        System.out.println();

        Dataset<Person> cloneDs = new Dataset<>(new PersonFeaturizer());
        Person p = new Person("Clon", 30, 70, 175, true);
        for (int i = 0; i < 5; i++)
            cloneDs.add(p);

        cloneDs.removeDuplicates();
        System.out.println("Size after 5 clones (should be 1): " + cloneDs.getElements().size());
        cloneDs.removeDuplicates();
        System.out.println("Size after repeating removal (should be 1): " + cloneDs.getElements().size());
        System.out.println();
        Dataset<Person> singleDs = new Dataset<>(new PersonFeaturizer());
        singleDs.add(new Person("Solo", 40, 80, 180, true));

        Feature<Integer> singleAge = singleDs.feature("age");
        System.out.println("Min/Max of single element (should be equal): "
                + singleAge.min() + " / " + singleAge.max());

        Feature<Integer> extremes = new Feature<>();
        extremes.add(Integer.MAX_VALUE);
        extremes.add(Integer.MIN_VALUE);
        System.out.println("Extremes -> Min: " + extremes.min() + " Max: " + extremes.max());

        System.out.println();
    }

    private static void testErrorHandling() {
        System.out.println("--- ERROR HANDELING ---");
        try {
        System.out.println("Testing null Featurizer...");
        new Dataset<Person>(null);
    } catch (IllegalArgumentException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }

    Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());

    ds.add(new Person("Test", 20, 70, 170, true)); 
    try {
        System.out.println("Testing add(null)...");
        ds.add(null);
    } catch (IllegalArgumentException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }

    try {
        System.out.println("Testing addAll(null collection)...");
        ds.addAll((Collection<Person>) null);
    } catch (IllegalArgumentException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }

    try {
        System.out.println("Testing feature(null name)...");
        ds.feature(null);
    } catch (IllegalArgumentException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }

    try {
        System.out.println("Testing feature(\"\") empty name...");
        ds.feature("");
    } catch (IllegalArgumentException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }

    try {
        System.out.println("Testing feature(\"age\", null class)...");
        ds.feature("age", null);
    } catch (IllegalArgumentException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }

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
            System.out.println(val);
        } catch (ClassCastException e) {
            System.out.println("Caught expected error: Cannot cast Integer to Double.");
        }
        System.out.println();
    }
}