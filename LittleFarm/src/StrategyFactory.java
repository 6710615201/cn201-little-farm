
import java.util.Locale;

// Factory สำหรับสร้าง WeatherStrategy ตามโหมดที่ผู้เล่นเลือก
public class StrategyFactory {

    // ป้องกันการสร้าง Object ของ Factory
    private StrategyFactory() {
    }

    // แปลงชื่อโหมดให้เป็นชื่อมาตรฐาน และตรวจสอบโหมดที่รองรับ
    public static String normalizeName(String name) {

        // หากไม่มีชื่อโหมด ให้ใช้ Normal เป็นค่าเริ่มต้น
        if (name == null || name.trim().isEmpty()) {
            return "normal";
        }

        // ตัดช่องว่างและแปลงเป็นตัวพิมพ์เล็ก
        String selected = name.trim().toLowerCase(Locale.ROOT);

        // ตรวจสอบว่าเป็นโหมดที่รองรับหรือไม่
        switch (selected) {
            case "normal":
            case "drought":
            case "monsoon":
                return selected;

            default:
                // แจ้งเตือนและกลับไปใช้ Normal หากชื่อโหมดไม่ถูกต้อง
                System.err.println(
                    "Unknown weather strategy '" + name + "'. Using normal."
                );
                return "normal";
        }
    }

    // สร้าง WeatherStrategy ตามชื่อโหมด
    public static WeatherStrategy create(String name) {

        // ใช้ชื่อโหมดมาตรฐานก่อนสร้าง Strategy
        String selected = normalizeName(name);

        switch (selected) {
            case "drought":
                return new DroughtWeatherStrategy();

            case "monsoon":
                return new MonsoonWeatherStrategy();

            case "normal":
            default:
                return new NormalWeatherStrategy();
        }
    }
}
