package tests.resources.exercise4;

import datasets.LabelProvider;

public class ShouldIPlayTennisToday implements LabelProvider<Weather, Boolean> {
    @Override
    public Boolean getLabel(Weather element) {
        return (element.getCondition() != WeatherCondition.RAINY) && (element.getTemperature() != Temperature.COLD);
    }
}
