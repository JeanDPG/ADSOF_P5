package tests.resources.exercise4;
public class Weather {
    private final WeatherCondition condition;
    private final Temperature temperature;

    public Weather(WeatherCondition c, Temperature t) {
        this.condition = c;
        this.temperature = t;
    }

    public WeatherCondition getCondition() { return condition; }
    public Temperature getTemperature() { return temperature; }

    @Override
    public String toString() {
        return "Weather{cond=" + condition + ", temp=" + temperature + "}";
    }
}
