
import java.util.Locale;

// Creates a weather strategy for the selected mode.
public class StrategyFactory {

    // Prevent instantiation.
    private StrategyFactory() {
    }

    // Normalize the mode name and validate it.
    public static String normalizeName(String name) {

        // Use Normal if a mode is missing.
        if (name == null || name.trim().isEmpty()) {
            return "normal";
        }

        // Trim the name and make it lowercase.
        String selected = name.trim().toLowerCase(Locale.ROOT);

        // Accept the supported mode names.
        switch (selected) {
            case "normal":
            case "drought":
            case "monsoon":
                return selected;

            default:
                // Warn about an unknown mode and fall back to Normal.
                System.err.println(
                    "Unknown weather strategy '" + name + "'. Using normal."
                );
                return "normal";
        }
    }

    // Create a strategy for the given mode name.
    public static WeatherStrategy create(String name) {

        // Normalize the name before choosing a strategy.
        String selected = normalizeName(name);

        switch (selected) {
            case "drought":
                return new DroughtWeatherStrategy();

            case "monsoon":
                return new MonsoonWeatherStrategy();

            case "normal":
            default:
                return new NormalWeatherStrategy();
        }
    }
}
