import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

// หน้าจอสำหรับให้ผู้เล่นเลือกโหมดสภาพอากาศก่อนเริ่มฟาร์ม
public class WeatherModeScreen implements Screen {
    private final GamePanel game;
    private final Screen previous;
    private final Farm backdrop = new Farm();
    private final Menu menu = new Menu();

    // รับหน้าจอก่อนหน้าไว้สำหรับกลับไปเมื่อผู้เล่นกด Back
    public WeatherModeScreen(GamePanel game, Screen previous) {
        this.game = game;
        this.previous = previous;

        // เพิ่มตัวเลือกโหมดอากาศทั้งสามแบบ
        menu.add("Normal - Original seasonal weather",
                () -> startGame("normal"));

        menu.add("Drought - Mostly sunny, no storms",
                () -> startGame("drought"));

        menu.add("Monsoon - Frequent rain and storms",
                () -> startGame("monsoon"));

        // เพิ่มตัวเลือกกลับหน้า Title
        menu.add("Back", () -> game.setScreen(previous));
    }

    // เริ่มเกมใหม่ด้วยโหมดอากาศที่ผู้เล่นเลือก
    private void startGame(String weatherMode) {
        game.setScreen(FarmScreen.newGame(game, weatherMode));
    }

    // ส่งปุ่มกดให้ Menu และรองรับ ESC เพื่อกลับหน้าก่อนหน้า
    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.setScreen(previous);
        } else {
            menu.keyPressed(code);
        }
    }

    // วาดหน้าจอเลือกโหมดโดยใช้รูปแบบเดียวกับ TitleScreen
    @Override
    public void draw(Graphics2D g) {
        backdrop.draw(g);
        backdrop.getVillager().draw(g, game.getFrames());

        g.setColor(Ui.WOOD);
        g.fillRect(0, Ui.MAP_HEIGHT, Ui.WIDTH, Ui.HUD_HEIGHT);

        Ui.dim(g, 150);

        // แสดงหัวข้อและคำอธิบาย
        Ui.centered(g, "CHOOSE WEATHER MODE", 30, 130, Ui.HIGHLIGHT);
        Ui.centered(g, "Choose the weather for your new farm.",
                15, 180, Color.WHITE);

        // วาดตัวเลือกโดยใช้ Menu ที่มีอยู่แล้ว
        menu.draw(g, 270, Color.WHITE);

        Ui.centered(g, "UP/DOWN choose    ENTER select    ESC back",
                13, 520, new Color(190, 190, 190));
    }
}
