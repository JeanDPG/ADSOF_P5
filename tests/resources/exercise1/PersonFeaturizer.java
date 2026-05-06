package tests.resources.exercise1;

import java.util.List;

import datasets.Featurizer;

/**
 * PersonFeaturizer.java
 * 
 * Implementación de Featurizer específica para objetos de tipo Person.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 */
public class PersonFeaturizer implements Featurizer<Person> {
    /** Lista de nombres de las columnas que el featurizer sabe extraer */
    private static final List<String> FEATURES = List.of("age", "weight", "gender");
    
    /**
     * Devuelve la lista de nombres de las columnas que el featurizer sabe extraer.
     * @return Lista con los nombres de las características.
     */
    @Override
    public List<String> featureNames() {
        return FEATURES;
    }

    /**
     * Traduce el nombre de una columna al valor real dentro de Person.
     * Si la featureName no es reconocida lanza una excepcion que hereda de 
     * RuntimeException, y que por tanto no es comprobada.
     * @param element El objeto del cual extraer la característica.
     * @param featureName El nombre de la característica deseada.
     * @return El valor de la característica como un Comparable.
     */
    @Override
    public Comparable<?> featureValue(Person element, String featureName) {
        return switch (featureName) {
            case "age" -> element.getAge();
            case "weight" -> element.weight();
            case "gender" -> element.gender();
            default -> throw new IllegalArgumentException("Unknown feature: " + featureName);
        };
    }
}
