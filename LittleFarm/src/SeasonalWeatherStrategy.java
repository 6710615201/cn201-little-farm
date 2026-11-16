import java.util.Random;

// Strategy for forecasting weather based on seasonal storm chances.
public class SeasonalWeatherStrategy implements WeatherStrategy {
    // Forecast weather using the current season.
    @Override
    public Weather forecast(Random rng, Season season) {
        // Check the storm chance for the current season.
        if (rng.nextInt(100) < season.getStormChance()) {
            return Weather.STORM;
        }
        // Use the original sunny and rainy forecast if no storm occurs.
        return Weather.forecast(rng);
    }
}
