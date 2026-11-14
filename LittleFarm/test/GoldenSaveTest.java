import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Golden-output test for SaveGame (assignment 1.4 b).
 * It calls the game's own classes directly (no window) and prints what they do.
 * Run it before a refactoring and after it: the two outputs must match line for line.
 */
public class GoldenSaveTest {

    private static final List<String> VALID = Arrays.asList(
            "little-farm save 1",
            "day 12",
            "weather RAINY",
            "gold 380",
            "earned 900",
            "can 12",
            "seeds Turnip 2",
            "seeds Potato 0",
            "seeds Strawberry 5",
            "basket Turnip 3",
            "basket Strawberry 1",
            "villager 2 5",
            "request Turnip 3 360 4",
            "soil 5 6 Turnip 1 wet",
            "soil 6 6 Potato 3 dry",
            "soil 7 6 - 0 dry",
            "soil 8 6 Strawberry 2 wet");

    public static void main(String[] args) throws IOException {
        Path dir = Files.createTempDirectory("golden-save");
        try {
            System.out.println("=== 1. load a valid save, then save it again");
            roundTrip(dir);
            System.out.println();
            System.out.println("=== 2. damaged saves (the game must refuse politely, never crash)");
            damagedCases(dir);
            System.out.println();
            System.out.println("=== 3. describe() and exists()");
            describeCases(dir);
        } finally {
            for (Path file : Files.newDirectoryStream(dir)) Files.delete(file);
            Files.delete(dir);
        }
    }

    private static void roundTrip(Path dir) throws IOException {
        Path file = dir.resolve("valid.txt");
        Files.write(file, VALID);
        SaveGame save = new SaveGame(file.toString());
        Farm farm = new Farm();
        Farmer farmer = new Farmer(2, 5, 300);
        GameClock clock = new GameClock();
        save.loadInto(farm, farmer, clock);
        printState(farm, farmer, clock);

        Path out = dir.resolve("written.txt");
        new SaveGame(out.toString()).save(farm, farmer, clock);
        System.out.println("--- file written by save():");
        for (String line : Files.readAllLines(out)) System.out.println(line);
    }

    private static void printState(Farm farm, Farmer farmer, GameClock clock) {
        System.out.println("day=" + clock.getDay() + " weather=" + clock.getWeather()
                + " gold=" + farmer.getGold() + " earned=" + farmer.getTotalEarned()
                + " can=" + farmer.getCan().getWater());
        for (String name : CropFactory.NAMES) System.out.println("seeds " + name + "=" + farmer.getSeedBag(name).getSeeds());
        System.out.println("basket=" + farmer.getBasket());
        Villager v = farm.getVillager();
        System.out.println("villager friendship=" + v.getFriendship() + " done=" + v.getRequestsDone());
        Request r = v.getRequest();
        System.out.println(r == null ? "request=none"
                : "request=" + r.getAmount() + " " + r.getCropName() + " " + r.getReward() + "G " + r.getDaysLeft() + "d");
        for (int row = 0; row < Farm.ROWS; row++) {
            for (int col = 0; col < Farm.COLS; col++) {
                Tile t = farm.tileAt(col, row);
                if (t.getType() != TileType.SOIL) continue;
                System.out.println("soil " + col + "," + row + " crop="
                        + (t.hasCrop() ? t.getCrop().getName() + "/" + t.getCrop().getDaysGrown() : "none")
                        + " watered=" + t.isWatered());
            }
        }
    }

    private static void damagedCases(Path dir) throws IOException {
        tryLoad(dir, "empty file", new ArrayList<String>());
        tryLoad(dir, "wrong header", replace(VALID, 0, "hello"));
        tryLoad(dir, "unknown entry", replace(VALID, 3, "silver 10"));
        tryLoad(dir, "gold is a word", replace(VALID, 3, "gold abc"));
        tryLoad(dir, "gold is negative", replace(VALID, 3, "gold -5"));
        tryLoad(dir, "gold is missing its number", replace(VALID, 3, "gold"));
        tryLoad(dir, "day is zero", replace(VALID, 1, "day 0"));
        tryLoad(dir, "unknown weather", replace(VALID, 2, "weather STORMY"));
        tryLoad(dir, "can is a word", replace(VALID, 5, "can lots"));
        tryLoad(dir, "unknown seed bag", replace(VALID, 6, "seeds Cabbage 2"));
        tryLoad(dir, "seeds missing amount", replace(VALID, 6, "seeds Turnip"));
        tryLoad(dir, "villager friendship too high", replace(VALID, 11, "villager 9 5"));
        tryLoad(dir, "request for unknown crop", replace(VALID, 12, "request Cabbage 3 360 4"));
        tryLoad(dir, "request amount zero", replace(VALID, 12, "request Turnip 0 360 4"));
        tryLoad(dir, "request too short", replace(VALID, 12, "request Turnip 3"));
        tryLoad(dir, "soil too short", replace(VALID, 13, "soil 5 6"));
        tryLoad(dir, "soil off the map", replace(VALID, 13, "soil 99 99 - 0 dry"));
        tryLoad(dir, "soil unknown crop", replace(VALID, 13, "soil 5 6 Cabbage 1 wet"));
        tryLoad(dir, "soil days not a number", replace(VALID, 13, "soil 5 6 Turnip x wet"));
        tryLoad(dir, "blank line is fine", replace(VALID, 4, ""));
        tryLoad(dir, "damage on the last line", replace(VALID, 16, "soil 8 6 Strawberry two wet"));
    }

    private static void describeCases(Path dir) throws IOException {
        Path file = dir.resolve("describe.txt");
        SaveGame save = new SaveGame(file.toString());
        System.out.println("no file: exists=" + save.exists() + " describe=" + save.describe());
        Files.write(file, VALID);
        System.out.println("valid:   exists=" + save.exists() + " describe=" + save.describe());
        Files.write(file, Arrays.asList("little-farm save 1", "day 3"));
        System.out.println("partial: describe=" + save.describe());
    }

    private static void tryLoad(Path dir, String label, List<String> lines) throws IOException {
        Path file = dir.resolve("case.txt");
        Files.write(file, lines);
        String result;
        try {
            new SaveGame(file.toString()).loadInto(new Farm(), new Farmer(2, 5, 300), new GameClock());
            result = "loaded OK";
        } catch (Exception e) {
            result = e.getClass().getSimpleName() + ": " + e.getMessage();
        }
        System.out.println(label + " -> " + result);
    }

    private static List<String> replace(List<String> lines, int index, String newLine) {
        List<String> copy = new ArrayList<>(lines);
        copy.set(index, newLine);
        return copy;
    }
}
