import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Saves the farm as plain text you can open and read, e.g.
 *   day 4
 *   season Rainy               (must match the season of the day above)
 *   weather RAINY
 *   stormlosses 3              (crops lost to storms since the game began)
 *   gold 380
 *   seeds Turnip 2
 *   request Turnip 3 360 4     (crop amount reward daysLeft)
 *   soil 5 6 Turnip 1 wet      (col row crop daysGrown wet/dry; "-" for an empty plot)
 */
public class SaveGame {
    private static final String HEADER = "little-farm save 1";
    private final Path file;

    public SaveGame(String fileName) {
        this.file = Paths.get(fileName);
    }

    public boolean exists() {
        return Files.exists(file);
    }

    /** A one-line description for the title menu, e.g. "Day 5, 780G", or null if there is no save. */
    public String describe() {
        if (!exists()) return null;
        String day = "?", gold = "?";
        try {
            for (String line : Files.readAllLines(file)) {
                if (line.startsWith("day ")) day = line.substring(4);
                else if (line.startsWith("gold ")) gold = line.substring(5);
            }
        } catch (IOException e) {
            return "unreadable save";
        }
        return "Day " + day + ", " + gold + "G";
    }

    public void delete() throws IOException {
        Files.deleteIfExists(file);
    }

    public void save(Farm farm, Farmer farmer, GameClock clock) throws IOException {
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(file))) {
            out.println(HEADER);
            out.println("day " + clock.getDay());
            out.println("season " + clock.getSeason().getName());
            out.println("weather " + clock.getWeather().name());
            out.println("weatherMode " + clock.getWeatherMode());
            out.println("stormlosses " + farm.getCropsLostToStorms());
            out.println("gold " + farmer.getGold());
            out.println("earned " + farmer.getTotalEarned());
            out.println("can " + farmer.getCan().getWater());
            for (String name : CropFactory.NAMES) out.println("seeds " + name + " " + farmer.getSeedBag(name).getSeeds());
            for (Map.Entry<String, Integer> entry : farmer.getBasket().asMap().entrySet()) {
                out.println("basket " + entry.getKey() + " " + entry.getValue());
            }
            Villager villager = farm.getVillager();
            out.println("villager " + villager.getFriendship() + " " + villager.getRequestsDone());
            Request request = villager.getRequest();
            if (request != null) {
                out.println("request " + request.getCropName() + " " + request.getAmount() + " "
                        + request.getReward() + " " + request.getDaysLeft());
            }
            for (int r = 0; r < Farm.ROWS; r++) {
                for (int c = 0; c < Farm.COLS; c++) {
                    Tile tile = farm.tileAt(c, r);
                    if (tile.getType() != TileType.SOIL) continue;
                    Crop crop = tile.getCrop();
                    out.println("soil " + c + " " + r + " " + (crop == null ? "- 0" : crop.getName() + " " + crop.getDaysGrown())
                            + (tile.isWatered() ? " wet" : " dry"));
                }
            }
        }
    }

    /** Fills a brand-new farm, farmer and clock from the file. */
    public void loadInto(Farm farm, Farmer farmer, GameClock clock) throws IOException, SaveFormatException {
        List<String> lines = Files.readAllLines(file);
        if (lines.isEmpty()) throw new SaveFormatException(1, "the file is empty");
        if (!lines.get(0).equals(HEADER)) throw new SaveFormatException(1, "not a Little Farm save file");

        for (int i = 1; i < lines.size(); i++) {
            try {
                loadLine(lines.get(i).trim().split(" "), i + 1, farm, farmer, clock);
            } catch (SaveFormatException e) {
                throw e;   // already says which line
            } catch (UnknownCropException e) {
                throw new SaveFormatException(i + 1, e.getMessage(), e);
            } catch (IllegalArgumentException | IndexOutOfBoundsException | FarmException e) {
                // NumberFormatException is an IllegalArgumentException, so a bad number lands here too
                throw new SaveFormatException(i + 1, lines.get(i), e);
            }
        }
    }

    /** Sends one line of the file to the method that knows how to read that kind of entry. */
    private void loadLine(String[] parts, int lineNumber, Farm farm, Farmer farmer, GameClock clock)
            throws FarmException {
        switch (parts[0]) {
            case "day":     loadDay(parts, clock); break;
            case "season":  loadSeason(parts, lineNumber, clock); break;
            case "weather": loadWeather(parts, clock); break;
            case "weatherMode": loadWeatherMode(parts, clock); break;
            case "stormlosses": loadStormLosses(parts, farm); break;
            case "gold":    loadGold(parts, farmer); break;
            case "earned":  loadEarned(parts, farmer); break;
            case "villager": loadVillager(parts, farm); break;
            case "request": loadRequest(parts, farm); break;
            case "can":     loadCan(parts, farmer); break;
            case "seeds":   loadSeeds(parts, farmer); break;
            case "basket":  loadBasket(parts, farmer); break;
            case "soil":    loadSoil(farm, parts); break;
            case "":        break;   // blank line
            default:        throw new SaveFormatException(lineNumber, "unknown entry '" + parts[0] + "'");
        }
    }

    private void loadDay(String[] parts, GameClock clock) {
        clock.setDay(Integer.parseInt(parts[1]));
    }

    /** The season is worked out from the day, so the file must agree with it (the "day" line comes first). */
    private void loadSeason(String[] parts, int lineNumber, GameClock clock) throws SaveFormatException {
        String actual = clock.getSeason().getName();
        if (!parts[1].equals(actual)) {
            throw new SaveFormatException(lineNumber, "season " + parts[1] + " does not match day "
                    + clock.getDay() + " (" + actual + ")");
        }
    }

    private void loadStormLosses(String[] parts, Farm farm) {
        farm.setCropsLostToStorms(Integer.parseInt(parts[1]));
    }

    private void loadWeather(String[] parts, GameClock clock) {
        clock.setWeather(Weather.valueOf(parts[1]));
    }

    // Restore the selected weather mode from the save file.
    private void loadWeatherMode(String[] parts, GameClock clock) {
        clock.setWeatherMode(parts[1]);
    }

    private void loadGold(String[] parts, Farmer farmer) {
        farmer.setGold(Integer.parseInt(parts[1]));
    }

    private void loadEarned(String[] parts, Farmer farmer) {
        farmer.setTotalEarned(Integer.parseInt(parts[1]));
    }

    private void loadVillager(String[] parts, Farm farm) {
        farm.getVillager().restore(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    private void loadRequest(String[] parts, Farm farm) throws UnknownCropException {
        requireKnownCrop(parts[1]);
        farm.getVillager().setRequest(new Request(parts[1], Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
    }

    private void loadCan(String[] parts, Farmer farmer) {
        farmer.getCan().setWater(Integer.parseInt(parts[1]));
    }

    private void loadSeeds(String[] parts, Farmer farmer) throws UnknownCropException {
        requireKnownCrop(parts[1]);
        farmer.getSeedBag(parts[1]).setSeeds(Integer.parseInt(parts[2]));
    }

    private void loadBasket(String[] parts, Farmer farmer) throws UnknownCropException {
        requireKnownCrop(parts[1]);
        int amount = Integer.parseInt(parts[2]);
        if (amount < 1) throw new IllegalArgumentException("basket amount must be at least 1, not " + amount);
        farmer.getBasket().add(parts[1], amount);
    }

    /** A crop name that no CropFactory entry knows would crash the game later, so refuse it here. */
    private void requireKnownCrop(String name) throws UnknownCropException {
        if (!Arrays.asList(CropFactory.NAMES).contains(name)) throw new UnknownCropException(name);
    }

    private void loadSoil(Farm farm, String[] parts) throws FarmException {
        Tile tile = farm.tileAt(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        if (tile == null) throw new FarmException("soil is off the map");
        if (tile.getType() != TileType.SOIL) tile.till();
        if (parts.length > 5 && parts[5].equals("wet")) tile.soak();
        if (parts[3].equals("-")) return;
        requireKnownCrop(parts[3]);
        Crop crop = CropFactory.create(parts[3]);
        crop.setDaysGrown(Integer.parseInt(parts[4]));
        tile.plant(crop);
    }
}
