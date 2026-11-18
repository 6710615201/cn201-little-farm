
import java.util.Random;

// Tracks game time from 06:00 to midnight.
public class GameClock {
    private static final int DAY_START = 6 * 60;
    private static final int DAY_END = 24 * 60;
    private static final int DUSK = 18 * 60;
    private static final int TICKS_PER_STEP = 120;
    private static final int MINUTES_PER_STEP = 10;

    private int day = 1;
    private final SeasonCalendar seasonCalendar = new SeasonCalendar();

    // Keep the selected mode and its forecasting strategy.
    private String weatherMode;
    private WeatherStrategy weatherStrategy;

    private int minutes = DAY_START;
    private int ticks = 0;
    private Weather weather = Weather.SUNNY;

    // Default to Normal weather.
    public GameClock() {
        this("normal");
    }

    // Initialize the clock with the selected weather mode.
    public GameClock(String weatherMode) {
        setWeatherMode(weatherMode);
    }

    // Update both the stored mode and its forecasting strategy.
    public void setWeatherMode(String weatherMode) {
        this.weatherMode = StrategyFactory.normalizeName(weatherMode);
        this.weatherStrategy = StrategyFactory.create(this.weatherMode);
    }

    // Return the mode name for saving.
    public String getWeatherMode() {
        return weatherMode;
    }

    public void tick() {
        if (++ticks >= TICKS_PER_STEP) {
            ticks = 0;
            minutes += MINUTES_PER_STEP;
        }
    }

    public boolean isMidnight() {
        return minutes >= DAY_END;
    }

    // Use the active strategy to forecast weather for the new day.
    public void nextDay(Random rng) {
        day++;
        minutes = DAY_START;
        ticks = 0;
        weather = weatherStrategy.forecast(rng, getSeason());
    }

    public int getDay() {
        return day;
    }

    public Season getSeason() {
        return seasonCalendar.seasonOf(day);
    }

    public int getDayInSeason() {
        return seasonCalendar.dayInSeason(day);
    }

    public int getMinutes() {
        return minutes;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void setDay(int day) {
        if (day < 1) {
            throw new IllegalArgumentException(
                "day must be at least 1, not " + day
            );
        }

        this.day = day;
    }

    // Calculate how dark the evening has become.
    public int darkness() {
        if (minutes < DUSK) {
            return 0;
        }

        return Math.min(160, (minutes - DUSK) * 160 / (4 * 60));
    }

    public String timeText() {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }
}
