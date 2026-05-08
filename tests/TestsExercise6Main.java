package tests;

import decisionTree.*;
import java.util.*;

public class TestsExercise6Main {
    public static void main(String[] args) {
        testPlainTextVisitor();
        testGraphvizVisitor();
    }

    private static void testPlainTextVisitor() {
        System.out.println("--- PLAIN TEXT VISITOR TEST ---");
        DecisionTree<String> tree = createSimpleTree();
        
        PlainTextVisitor<String> visitor = new PlainTextVisitor<>(tree);
        Node<String> root = tree.getNodes().get(tree.getRootName());
        root.accept(visitor);
        
        String result = visitor.getResult();
        System.out.println("Generated Text:");
        System.out.println(result);
        
        System.out.println("Contains 'Node: root'? " + result.contains("Node: root"));
        System.out.println("Contains 'Result -> ResultA'? " + result.contains("Result -> ResultA"));
        System.out.println();
    }

    private static void testGraphvizVisitor() {
        System.out.println("--- GRAPHVIZ VISITOR TEST ---");
        DecisionTree<String> tree = createSimpleTree();
        
        GraphvizVisitor<String> visitor = new GraphvizVisitor<>(tree);
        Node<String> root = tree.getNodes().get(tree.getRootName());
        root.accept(visitor);
        
        String result = visitor.getDotCode();
        System.out.println("Generated DOT Code:");
        System.out.println(result);
        
        System.out.println("Starts with 'digraph G {'? " + result.startsWith("digraph G {"));
        System.out.println("Contains 'shape=ellipse'? " + result.contains("shape=ellipse"));
        System.out.println();
    }

    private static DecisionTree<String> createSimpleTree() {
        DecisionTree<String> tree = new DecisionTree<>();
        tree.node("root")
            .withCondition("NodeA", s -> s.equals("A"))
            .otherwise("ResultB");
        
        tree.node("NodeA").otherwise("ResultA");
        return tree;
    }
}
