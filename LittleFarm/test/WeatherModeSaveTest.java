import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Checks the "weatherMode" line of the save file: a known mode is loaded, an unknown word is
 * refused with the line number (it must not silently turn into "normal"), and an old save with
 * no such line still loads.
 * Prints PASS or FAIL for each case and exits with 1 if anything failed.
 */
public class WeatherModeSaveTest {

    // The "weatherMode" line is line 5.
    private static final List<String> VALID = Arrays.asList(
            "little-farm save 1",      // line 1
            "day 12",                  // line 2
            "season Rainy",            // line 3
            "weather RAINY",           // line 4
            "weatherMode normal",      // line 5
            "stormlosses 4",           // line 6
            "gold 380",                // line 7
            "earned 900",              // line 8
            "can 12",                  // line 9
            "villager 2 5",            // line 10
            "soil 5 6 Turnip 1 wet");  // line 11

    private static int failures = 0;

    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("weather-mode-save");
        try {
            check("normal is loaded", "normal".equals(modeAfterLoading(dir, VALID)));
            check("drought is loaded", "drought".equals(modeAfterLoading(dir, replace(VALID, 4, "weatherMode drought"))));
            check("monsoon is loaded", "monsoon".equals(modeAfterLoading(dir, replace(VALID, 4, "weatherMode monsoon"))));
            check("capital letters are accepted (Drought)",
                    "drought".equals(modeAfterLoading(dir, replace(VALID, 4, "weatherMode Drought"))));

            expectLine(dir, "unknown mode xyz", replace(VALID, 4, "weatherMode xyz"), 5);
            expectLine(dir, "mode is a number", replace(VALID, 4, "weatherMode 123"), 5);
            expectLine(dir, "mode has no value", replace(VALID, 4, "weatherMode"), 5);

            List<String> old = new ArrayList<String>(VALID);
            old.remove(4);
            check("old save without weatherMode loads as normal", "normal".equals(modeAfterLoading(dir, old)));

            // save, then load back
            Path file = dir.resolve("roundtrip.txt");
            GameClock clock = new GameClock();
            clock.setDay(12);
            clock.setWeatherMode("monsoon");
            new SaveGame(file.toString()).save(new Farm(), new Farmer(2, 5, 300), clock);
            check("save writes 'weatherMode monsoon'", Files.readAllLines(file).contains("weatherMode monsoon"));
            GameClock back = new GameClock();
            new SaveGame(file.toString()).loadInto(new Farm(), new Farmer(2, 5, 300), back);
            check("monsoon survives save and load", "monsoon".equals(back.getWeatherMode()));
        } finally {
            for (Path p : Files.newDirectoryStream(dir)) Files.delete(p);
            Files.delete(dir);
        }
        System.out.println(failures == 0 ? "ALL PASSED" : failures + " FAILED");
        if (failures > 0) System.exit(1);
    }

    private static String modeAfterLoading(Path dir, List<String> lines) throws IOException {
        GameClock clock = new GameClock();
        try {
            load(dir, lines, clock);
        } catch (SaveFormatException e) {
            return "refused: " + e.getMessage();
        }
        return clock.getWeatherMode();
    }

    private static void expectLine(Path dir, String label, List<String> lines, int line) throws IOException {
        try {
            load(dir, lines, new GameClock());
            check(label + " (nothing was thrown)", false);
        } catch (SaveFormatException e) {
            check(label + " -> " + e.getMessage(), e.getLineNumber() == line);
        }
    }

    private static void load(Path dir, List<String> lines, GameClock clock)
            throws IOException, SaveFormatException {
        Path file = dir.resolve("case.txt");
        Files.write(file, lines);
        new SaveGame(file.toString()).loadInto(new Farm(), new Farmer(2, 5, 300), clock);
    }

    private static void check(String label, boolean ok) {
        System.out.println((ok ? "PASS " : "FAIL ") + label);
        if (!ok) failures++;
    }

    private static List<String> replace(List<String> lines, int index, String newLine) {
        List<String> copy = new ArrayList<String>(lines);
        copy.set(index, newLine);
        return copy;
    }
}