import java.util.Random;

// Strategy interface for seasonal weather forecasting.
public interface WeatherStrategy {

    // Forecast weather using the random source and current season.
    Weather forecast(Random rng, Season season);
}