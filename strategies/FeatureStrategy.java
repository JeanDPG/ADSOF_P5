package strategies;

import datasets.LabeledDataset;
import java.util.List;

public interface FeatureStrategy<T, L> {
    String execute(LabeledDataset<T, L> data, List<String> availableFeatures);
}
