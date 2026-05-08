package strategies;

import datasets.LabeledDataset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisclassificationStrategy<T, L> implements FeatureStrategy<T, L> {
    @Override
    public String execute(LabeledDataset<T, L> data, List<String> availableFeatures) {
        String bestFeature = null;
        int lowestScore = Integer.MAX_VALUE;

        for (String feature : availableFeatures) {
            int totalMismatches = 0;

            // Agrupar datos por los valores de la feature actual
            Map<Object, List<L>> groups = new HashMap<>();
            for (T item : data.getElements()) {
                Object val = data.getFeaturizer().featureValue(item, feature);
                L label = data.getLabel(item);
                
                if (!groups.containsKey(val)) {
                    groups.put(val, new ArrayList<>());
                }
                groups.get(val).add(label);
            }

            for (List<L> labels : groups.values()) {
                L majorityLabel = getMajorityLabel(labels);
                int mismatches = 0;
                for (L label : labels) {
                    if (!label.equals(majorityLabel)) {
                        mismatches++;
                    }
                }
                totalMismatches += mismatches;
            }

            if (totalMismatches < lowestScore) {
                lowestScore = totalMismatches;
                bestFeature = feature;
            }
        }
        return bestFeature;
    }

    private L getMajorityLabel(List<L> labels) {
        if (labels == null || labels.isEmpty()) return null;
        L bestLabel = null;
        int maxCount = -1;
        for (L label : labels) {
            int count = Collections.frequency(labels, label);
            if (count > maxCount) {
                maxCount = count;
                bestLabel = label;
            }
        }
        return bestLabel;
    }
}
