// ข้อผิดพลาดการปลูกผิดฤดู สืบทอดจาก FarmException เพื่อให้เกมแสดงข้อความแทนการหยุดทำงาน
/** Expected planting failure, caught by the existing FarmException handler. */
public class WrongSeasonException extends FarmException {
    public WrongSeasonException(String cropName, Season season) {
        super(cropName + " can't grow in " + season.getName() + " season");
    }
}