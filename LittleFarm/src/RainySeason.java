// ฤดูฝน: โอกาสพายุ 30% พร้อมชื่อและสีสำหรับระบบส่วนอื่น
import java.awt.Color;

public class RainySeason extends Season {
    public RainySeason() {
        super(30, "☁");
    }

    @Override public String getName() { return "Rainy"; }

    @Override
    public Color getTint() {
        return new Color(70, 120, 210, 45);
    }
}