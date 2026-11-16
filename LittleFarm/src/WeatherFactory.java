/** Creates the weather forecasting strategy (Factory pattern). */
public class WeatherFactory {

    // Create the default seasonal weather strategy.
    public static WeatherStrategy create() {
        return new SeasonalWeatherStrategy();
    }
}
