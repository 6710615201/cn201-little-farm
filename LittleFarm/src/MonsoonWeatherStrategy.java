import java.util.Random;

// Monsoon mode produces frequent rain and storms.
public class MonsoonWeatherStrategy implements WeatherStrategy {

    // Forecast 30% storms, 60% rain, and 10% sunny weather.
    @Override
    public Weather forecast(Random rng, Season season) {
        int roll = rng.nextInt(100);

        // The first 30 values represent storms.
        if (roll < 30) {
            return Weather.STORM;
        }

        // The next 60 values represent rainy weather.
        if (roll < 90) {
            return Weather.RAINY;
        }

        // The remaining 10 values represent sunny weather.
        return Weather.SUNNY;
    }
}
