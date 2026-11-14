// จอบไม่ใช้ฤดู แต่รับพารามิเตอร์ให้ตรงกับ Tool; กฎการพรวนดินเดิมไม่เปลี่ยน
public class Hoe implements Tool {
    @Override public String getLabel() { return "Hoe"; }
    @Override public String getActionName() { return "Till"; }
    @Override public int getEnergyCost() { return 4; }

    @Override
    public void use(Tile tile, Season season) throws FarmException {
        tile.till();
    }
}
