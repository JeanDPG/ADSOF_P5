package datasets;

import java.util.List;

public class PersonFeaturizer implements Featurizer<Person> {

    private static final List<String> FEATURES = List.of("age", "weight", "gender");

    @Override
    // Define que columnas vamos a usar al convertir Person en dataset.
    public List<String> featureNames() {
        return FEATURES;
    }

    @Override
    // Traduce el nombre de una columna al valor real dentro de Person.
    public Comparable<?> featureValue(Person element, String featureName) {
        return switch (featureName) {
            case "age" -> element.age();
            case "weight" -> element.weight();
            case "gender" -> element.gender();
            default -> throw new IllegalArgumentException("Unknown feature: " + featureName);
        };
    }
}
