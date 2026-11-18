import java.util.Random;

// Test seasonal weather forecasting with predictable random values.
public class WeatherStrategyTest {

    // Supply fixed random values so each test is repeatable.
    private static class FixedRandom extends Random {
        private final int[] values;
        private int index = 0;

        FixedRandom(int... values) {
            this.values = values;
        }

        @Override
        public int nextInt(int bound) {
            // Return the next predetermined value.
            return values[index++];
        }
    }

    public static void main(String[] args) {
        // Create the strategy being tested.
        WeatherStrategy strategy = new SeasonalWeatherStrategy();

        // Hot season has 0% storm chance.
        check("Hot season", Weather.SUNNY,
                strategy.forecast(new FixedRandom(0, 0), new HotSeason()));

        // Rainy season has 30% storm chance.
        check("Rainy season storm", Weather.STORM,
                strategy.forecast(new FixedRandom(29), new RainySeason()));

        // Cool season has 10% storm chance.
        check("Cool season storm", Weather.STORM,
                strategy.forecast(new FixedRandom(9), new CoolSeason()));

        // A roll equal to the storm threshold must not produce a storm.
        check("Cool season no storm", Weather.SUNNY,
                strategy.forecast(new FixedRandom(10, 0), new CoolSeason()));

        System.out.println("All WeatherStrategy tests passed.");
    }

    // Compare the actual weather with the expected weather.
    private static void check(String label, Weather expected, Weather actual) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected "
                    + expected + " but got " + actual);
        }

        System.out.println("PASS: " + label);
    }
}
