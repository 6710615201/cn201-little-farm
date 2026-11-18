import java.util.Random;

// Drought mode produces mostly sunny days and no storms.
public class DroughtWeatherStrategy implements WeatherStrategy {

    // Forecast weather with a 90% sunny and 10% rainy chance.
    @Override
    public Weather forecast(Random rng, Season season) {
        if (rng.nextInt(100) < 10) {
            return Weather.RAINY;
        }

        return Weather.SUNNY;
    }
}