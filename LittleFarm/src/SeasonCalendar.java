// แบ่งฤดูทุก 10 วัน: ร้อน 1–10 ฝน 11–20 หนาว 21–30; วันจบเกมยังใช้ฤดูหนาว
public class SeasonCalendar {
    private final Season hotSeason = new HotSeason();
    private final Season rainySeason = new RainySeason();
    private final Season coolSeason = new CoolSeason();

    public Season seasonOf(int day) {
        if (day < 1) {
            throw new IllegalArgumentException("day must be at least 1");
        }
        if (day <= 10) {
            return hotSeason;
        }
        if (day <= 20) {
            return rainySeason;
        }
        return coolSeason;
    }

    public int dayInSeason(int day) {
        if (day < 1) {
            throw new IllegalArgumentException("day must be at least 1");
        }
        if (day > 30) {
            return 10;
        }
        return ((day - 1) % 10) + 1;
    }
}