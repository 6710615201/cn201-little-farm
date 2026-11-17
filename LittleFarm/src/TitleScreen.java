
import java.awt.Color;
import java.awt.Graphics2D;

// หน้าจอหลักของเกม
public class TitleScreen implements Screen {
    private final GamePanel game;
    private final Farm backdrop = new Farm();   // ฟาร์มพื้นหลังของหน้าเมนู
    private final Menu menu = new Menu();

    // สร้างตัวเลือกต่าง ๆ บนหน้า Title
    public TitleScreen(GamePanel game) {
        this.game = game;

        // ตรวจสอบว่ามี Save เดิมหรือไม่
        String saved = game.getSaveGame().describe();

        if (saved != null) {
            // Continue ยังคงโหลดฟาร์มเดิมตามปกติ
            menu.add("Continue  (" + saved + ")", () -> FarmScreen.load(game));

            // ถ้ามี Save ต้องยืนยันก่อนเริ่มฟาร์มใหม่
            menu.add("New farm", () -> game.setScreen(new ConfirmScreen(
                    game,
                    this,
                    "Start a new farm?",
                    "Your saved farm will be erased when you next sleep.",
                    "Start over",
                    "Keep my farm",
                    true,
                    // เมื่อยืนยันแล้ว ให้เปิดหน้าเลือกโหมดอากาศ
                    () -> game.setScreen(new WeatherModeScreen(game, this))
            )));
        } else {
            // ถ้าไม่มี Save สามารถเลือกโหมดอากาศได้ทันที
            menu.add("New farm",
                    () -> game.setScreen(new WeatherModeScreen(game, this)));
        }

        // ตัวเลือกเดิมของหน้า Title
        menu.add("How to play", () -> game.setScreen(new HelpScreen(game, this)));
        menu.add("Quit", game::quit);
    }

    // รับปุ่มกดเพื่อควบคุมเมนู
    @Override
    public void keyPressed(int code) {
        menu.keyPressed(code);
    }

    // วาดหน้าจอหลัก
    @Override
    public void draw(Graphics2D g) {
        backdrop.draw(g);
        backdrop.getVillager().draw(g, game.getFrames());

        g.setColor(Ui.WOOD);
        g.fillRect(0, Ui.MAP_HEIGHT, Ui.WIDTH, Ui.HUD_HEIGHT);
        Ui.dim(g, 150);

        Ui.centered(g, "LITTLE FARM", 48, 130, Ui.HIGHLIGHT);
        Ui.centered(g, "Grandpa left you his farm - and a " + FarmScreen.GOAL_GOLD + "G debt.",
                15, 180, Color.WHITE);
        Ui.centered(g, "Pay it off by the end of day " + FarmScreen.LAST_DAY + ".",
                15, 202, Color.WHITE);

        // วาดเมนูหลัก
        menu.draw(g, 270, Color.WHITE);

        Ui.centered(g, "UP/DOWN choose    ENTER select",
                13, 520, new Color(190, 190, 190));
    }
}
