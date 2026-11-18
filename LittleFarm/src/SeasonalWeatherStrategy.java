import java.util.Random;

// Strategy for forecasting weather based on seasonal storm chances.
public class SeasonalWeatherStrategy implements WeatherStrategy {
    // Forecast weather using the current season.
    @Override
    public Weather forecast(Random rng, Season season) {
        // Roll for a seasonal storm before using the sunny/rainy distribution.
        if (rng.nextInt(100) < season.getStormChance()) {
            return Weather.STORM;
        }
        // These odds apply only to the days that pass the storm check.
        return Weather.forecast(rng);
    }
}
