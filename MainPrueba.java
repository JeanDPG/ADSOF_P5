import datasets.*;
import decisionTree.*;
import tests.resources.exercise1.*;
import java.util.*;

/**
 * MainPrueba.java
 * 
 * Este es un main de prueba completo que muestra cómo usar todas las piezas del proyecto:
 * 1. Crear un Dataset de personas.
 * 2. Entrenar un árbol automáticamente con GreedyTreeLearner.
 * 3. Realizar predicciones.
 * 4. Visualizar el árbol con PlainTextVisitor.
 */
public class MainPrueba {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DEL PROYECTO DECISION TREE ===");

        // Preparar los datos (Personas)
        // El Featurizer extrae: age, weight, gender
        Dataset<Person> ds = new Dataset<>(new PersonFeaturizer());
        ds.add(new Person("Ana", 25, 60, 165, false));
        ds.add(new Person("Juan", 30, 80, 180, true));
        ds.add(new Person("Luis", 15, 50, 160, true));
        ds.add(new Person("Marta", 12, 40, 150, false));

        // Definir qué se quiere predecir (LabelProvider)
        // Predecir si es "Adulto" o "Menor"
        LabelProvider<Person, String> lp = p -> p.getAge() >= 18 ? "ADULTO" : "MENOR";

        // Entrenar el árbol automáticamente
        GreedyTreeLearner<Person, String> learner = new GreedyTreeLearner<>();
        DecisionTree<Person> tree = learner.learn(ds.getElements(), ds.getFeaturizer(), lp);

        // Mostrar el árbol usando el PlainTextVisitor
        System.out.println("\nEstructura del árbol generado:");
        PlainTextVisitor<Person> visitor = new PlainTextVisitor<>(tree);
        // Empezar la visita desde la raíz
        Node<Person> root = tree.getNodes().get(tree.getRootName());
        if (root != null) {
            root.accept(visitor);
            System.out.println(visitor.getResult());
        }

        // Probar predicciones con datos nuevos
        System.out.println("Predicciones:");
        Person p1 = new Person("Nuevo1", 40, 85, 185, true);
        Person p2 = new Person("Nuevo2", 5, 20, 110, false);
        
        System.out.println("  - " + p1.name() + " (40 años) -> Predicción: " + tree.predict(p1));
        System.out.println("  - " + p2.name() + " (5 años) -> Predicción: " + tree.predict(p2));

        System.out.println("\n=== FIN DE LA PRUEBA ===");
    }
}
