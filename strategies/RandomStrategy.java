package strategies;

import datasets.LabeledDataset;
import java.util.List;
import java.util.Random;

public class RandomStrategy<T, L> implements FeatureStrategy<T, L> {
    @Override
    public String execute(LabeledDataset<T, L> data, List<String> availableFeatures) {
        if (availableFeatures == null || availableFeatures.isEmpty()) return null;
        Random rand = new Random();
        return availableFeatures.get(rand.nextInt(availableFeatures.size()));
    }
}