package treeLearner;
public class ShouldIPlayTennisToday implements LabelProvider<Weather, Boolean> {
    @Override
    public Boolean getLabel(Weather element) {
        return (element.getCondition() != WeatherCondition.RAINY) && (element.getTemperature() != Temperature.COLD);
    }
}
