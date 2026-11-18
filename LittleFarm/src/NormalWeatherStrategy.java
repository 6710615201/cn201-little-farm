import java.util.Random;

// Normal mode preserves the existing seasonal weather behavior.
public class NormalWeatherStrategy implements WeatherStrategy {

    // Reuse the existing seasonal forecasting rules.
    private final WeatherStrategy seasonalStrategy =
            new SeasonalWeatherStrategy();

    // Forecast weather using the original seasonal rules.
    @Override
    public Weather forecast(Random rng, Season season) {
        return seasonalStrategy.forecast(rng, season);
    }
}
