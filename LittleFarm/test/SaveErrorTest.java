import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Checks the save-file exception family: every damaged save must give a SaveFormatException
 * (a FarmException) that names the right line, and a good save must still load.
 * Prints PASS or FAIL for each case and exits with 1 if anything failed.
 */
public class SaveErrorTest {

    private static final List<String> VALID = Arrays.asList(
            "little-farm save 1",      // line 1
            "day 12",                  // line 2
            "weather RAINY",           // line 3
            "gold 380",                // line 4
            "earned 900",              // line 5
            "can 12",                  // line 6
            "seeds Turnip 2",          // line 7
            "seeds Potato 0",          // line 8
            "seeds Strawberry 5",      // line 9
            "basket Turnip 3",         // line 10
            "basket Strawberry 1",     // line 11
            "villager 2 5",            // line 12
            "request Turnip 3 360 4",  // line 13
            "soil 5 6 Turnip 1 wet",   // line 14
            "soil 6 6 Potato 3 dry");  // line 15

    private static int failures = 0;

    public static void main(String[] args) throws IOException {
        Path dir = Files.createTempDirectory("save-errors");
        try {
            expectOk(dir, "valid save loads", VALID);
            expectLine(dir, "empty file", new ArrayList<String>(), 1);
            expectLine(dir, "wrong header", replace(VALID, 0, "hello"), 1);
            expectLine(dir, "unknown entry", replace(VALID, 3, "silver 10"), 4);
            expectLine(dir, "gold is a word", replace(VALID, 3, "gold abc"), 4);
            expectLine(dir, "day is zero", replace(VALID, 1, "day 0"), 2);
            expectLine(dir, "unknown weather", replace(VALID, 2, "weather STORMY"), 3);
            expectLine(dir, "soil too short", replace(VALID, 13, "soil 5 6"), 14);
            expectLine(dir, "soil off the map", replace(VALID, 13, "soil 99 99 - 0 dry"), 14);
            expectLine(dir, "damage on the last line", replace(VALID, 14, "soil 6 6 Potato x dry"), 15);
            expectUnknownCrop(dir, "seeds: unknown crop", replace(VALID, 6, "seeds Cabbage 2"), 7);
            expectUnknownCrop(dir, "basket: unknown crop", replace(VALID, 9, "basket Cabbage 3"), 10);
            expectUnknownCrop(dir, "request: unknown crop", replace(VALID, 12, "request Cabbage 3 360 4"), 13);
            expectUnknownCrop(dir, "soil: unknown crop", replace(VALID, 13, "soil 5 6 Cabbage 1 wet"), 14);
            expectLine(dir, "basket amount zero", replace(VALID, 9, "basket Turnip 0"), 10);
            check("WrongSeasonException is a FarmException",
                    new WrongSeasonException("Turnip", "Rainy") instanceof FarmException);
            check("WrongSeasonException message",
                    "Turnip can't grow in the Rainy season".equals(new WrongSeasonException("Turnip", "Rainy").getMessage()));
        } finally {
            for (Path file : Files.newDirectoryStream(dir)) Files.delete(file);
            Files.delete(dir);
        }
        System.out.println(failures == 0 ? "ALL PASSED" : failures + " FAILED");
        if (failures > 0) System.exit(1);
    }

    private static void expectOk(Path dir, String label, List<String> lines) throws IOException {
        try {
            load(dir, lines);
            check(label, true);
        } catch (Exception e) {
            check(label + " (got " + e + ")", false);
        }
    }

    private static void expectLine(Path dir, String label, List<String> lines, int line) throws IOException {
        try {
            load(dir, lines);
            check(label + " (nothing was thrown)", false);
        } catch (SaveFormatException e) {
            check(label + " -> " + e.getMessage(), e.getLineNumber() == line && e instanceof FarmException);
        } catch (Exception e) {
            check(label + " (wrong exception " + e + ")", false);
        }
    }

    private static void expectUnknownCrop(Path dir, String label, List<String> lines, int line) throws IOException {
        try {
            load(dir, lines);
            check(label + " (nothing was thrown)", false);
        } catch (SaveFormatException e) {
            check(label + " -> " + e.getMessage(),
                    e.getLineNumber() == line && e.getCause() instanceof UnknownCropException);
        } catch (Exception e) {
            check(label + " (wrong exception " + e + ")", false);
        }
    }

    private static void load(Path dir, List<String> lines) throws IOException, SaveFormatException {
        Path file = dir.resolve("case.txt");
        Files.write(file, lines);
        new SaveGame(file.toString()).loadInto(new Farm(), new Farmer(2, 5, 300), new GameClock());
    }

    private static void check(String label, boolean ok) {
        System.out.println((ok ? "PASS " : "FAIL ") + label);
        if (!ok) failures++;
    }

    private static List<String> replace(List<String> lines, int index, String newLine) {
        List<String> copy = new ArrayList<>(lines);
        copy.set(index, newLine);
        return copy;
    }
}
