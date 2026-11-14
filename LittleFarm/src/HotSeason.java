// ฤดูร้อน: ไม่มีพายุ (0%) พร้อมชื่อและสีสำหรับระบบส่วนอื่น
import java.awt.Color;

public class HotSeason extends Season {
    public HotSeason() {
        super(0, "☀");
    }

    @Override public String getName() { return "Hot"; }

    @Override
    public Color getTint() {
        return new Color(255, 180, 70, 45);
    }
}