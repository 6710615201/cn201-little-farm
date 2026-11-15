import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Checks the season data in the save file: the "season" and "stormlosses" lines are saved, loaded
 * back, and a wrong value gives a SaveFormatException naming the right line.
 * Prints PASS or FAIL for each case and exits with 1 if anything failed.
 */
public class SeasonSaveTest {

    // Day 12 is in the Rainy season. The "season" line is line 3, "stormlosses" is line 5.
    private static final List<String> VALID = Arrays.asList(
            "little-farm save 1",      // line 1
            "day 12",                  // line 2
            "season Rainy",            // line 3
            "weather RAINY",           // line 4
            "stormlosses 4",           // line 5
            "gold 380",                // line 6
            "earned 900",              // line 7
            "can 12",                  // line 8
            "villager 2 5",            // line 9
            "soil 5 6 Turnip 1 wet");  // line 10

    private static int failures = 0;

    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("season-save");
        try {
            // loading
            Farm farm = new Farm();
            load(dir, VALID, farm, new GameClock());
            check("valid season save loads", true);
            check("stormlosses is loaded (4)", farm.getCropsLostToStorms() == 4);

            expectLine(dir, "season does not match the day", replace(VALID, 2, "season Hot"), 3);
            expectLine(dir, "unknown season name", replace(VALID, 2, "season Winter"), 3);
            expectLine(dir, "season has no value", replace(VALID, 2, "season"), 3);
            expectLine(dir, "stormlosses is negative", replace(VALID, 4, "stormlosses -1"), 5);
            expectLine(dir, "stormlosses is a word", replace(VALID, 4, "stormlosses abc"), 5);
            expectLine(dir, "stormlosses has no value", replace(VALID, 4, "stormlosses"), 5);

            List<String> hot = new ArrayList<String>(VALID);
            hot.set(1, "day 5");
            hot.set(2, "season Hot");
            check("day 5 with Hot loads", loads(dir, hot));
            List<String> cool = new ArrayList<String>(VALID);
            cool.set(1, "day 25");
            cool.set(2, "season Cool");
            check("day 25 with Cool loads", loads(dir, cool));
            cool.set(2, "season Rainy");
            expectLine(dir, "day 25 with Rainy is refused", cool, 3);

            // an old save with neither line still loads, and the counter stays 0
            List<String> old = new ArrayList<String>(VALID);
            old.remove(4);
            old.remove(2);
            Farm oldFarm = new Farm();
            load(dir, old, oldFarm, new GameClock());
            check("old save without the new lines loads (counter 0)", oldFarm.getCropsLostToStorms() == 0);

            // save, then load back
            Path file = dir.resolve("roundtrip.txt");
            Farm saved = new Farm();
            saved.setCropsLostToStorms(7);
            GameClock clock = new GameClock();
            clock.setDay(12);
            new SaveGame(file.toString()).save(saved, new Farmer(2, 5, 300), clock);
            List<String> written = Files.readAllLines(file);
            check("save writes 'season Rainy' for day 12", written.contains("season Rainy"));
            check("save writes 'stormlosses 7'", written.contains("stormlosses 7"));
            Farm back = new Farm();
            GameClock backClock = new GameClock();
            new SaveGame(file.toString()).loadInto(back, new Farmer(2, 5, 300), backClock);
            check("stormlosses survives save and load (7)", back.getCropsLostToStorms() == 7);
            check("season survives save and load (Rainy)", "Rainy".equals(backClock.getSeason().getName()));
        } finally {
            for (Path p : Files.newDirectoryStream(dir)) Files.delete(p);
            Files.delete(dir);
        }
        System.out.println(failures == 0 ? "ALL PASSED" : failures + " FAILED");
        if (failures > 0) System.exit(1);
    }

    private static boolean loads(Path dir, List<String> lines) throws IOException {
        try {
            load(dir, lines, new Farm(), new GameClock());
            return true;
        } catch (SaveFormatException e) {
            return false;
        }
    }

    private static void expectLine(Path dir, String label, List<String> lines, int line) throws IOException {
        try {
            load(dir, lines, new Farm(), new GameClock());
            check(label + " (nothing was thrown)", false);
        } catch (SaveFormatException e) {
            check(label + " -> " + e.getMessage(), e.getLineNumber() == line);
        }
    }

    private static void load(Path dir, List<String> lines, Farm farm, GameClock clock)
            throws IOException, SaveFormatException {
        Path file = dir.resolve("case.txt");
        Files.write(file, lines);
        new SaveGame(file.toString()).loadInto(farm, new Farmer(2, 5, 300), clock);
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
