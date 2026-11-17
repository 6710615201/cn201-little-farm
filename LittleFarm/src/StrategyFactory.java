import java.util.Locale;

// The only factory responsible for converting startup names into strategies.
public class StrategyFactory {

    // Prevent creating factory objects.
    private StrategyFactory() {
    }

    // Create a weather strategy from the startup argument.
    public static WeatherStrategy create(String name) {

        // Use normal when no strategy name is provided.
        if (name == null || name.trim().isEmpty()) {
            return new NormalWeatherStrategy();
        }

        // Ignore uppercase and surrounding spaces in the input.
        String selected = name.trim().toLowerCase(Locale.ROOT);

        // Map each supported name to its corresponding strategy.
        switch (selected) {
            case "normal":
                return new NormalWeatherStrategy();

            case "drought":
                return new DroughtWeatherStrategy();

            case "monsoon":
                return new MonsoonWeatherStrategy();

            default:
                // Warn the user and safely fall back to normal.
                System.err.println(
                    "Unknown weather strategy '" + name + "'. Using normal."
                );
                return new NormalWeatherStrategy();
        }
    }
}