package treeLearner;
import datasets.*;
import java.util.*;

public class WeatherFeaturizer implements Featurizer<Weather> {
    @Override
    public List<String> featureNames() {
        return List.of("Condition", "Temperature");
    }

    @Override
    public Comparable<?> featureValue(Weather element, String featureName) {
        return switch (featureName) {
            case "Condition" -> element.getCondition();
            case "Temperature" -> element.getTemperature();
            default -> throw new IllegalArgumentException();
        };
    }
}