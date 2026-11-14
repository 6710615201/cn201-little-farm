// ฤดูหนาว: โอกาสพายุ 10% พร้อมชื่อและสีสำหรับระบบส่วนอื่น
import java.awt.Color;

public class CoolSeason extends Season {
    public CoolSeason() {
        super(10, "❄");
    }

    @Override public String getName() { return "Cool"; }

    @Override
    public Color getTint() {
        return new Color(150, 210, 240, 45);
    }
}
