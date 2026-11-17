import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

// Lets the player choose a weather mode for a new farm.
public class WeatherModeScreen implements Screen {
    private final GamePanel game;
    private final Screen previous;
    private final Farm backdrop = new Farm();
    private final Menu menu = new Menu();

    // Remember the previous screen for Back.
    public WeatherModeScreen(GamePanel game, Screen previous) {
        this.game = game;
        this.previous = previous;

        // Add the three weather modes.
        menu.add("Normal - Original seasonal weather",
                () -> startGame("normal"));

        menu.add("Drought - Mostly sunny, no storms",
                () -> startGame("drought"));

        menu.add("Monsoon - Frequent rain and storms",
                () -> startGame("monsoon"));

        // Return to the title screen.
        menu.add("Back", () -> game.setScreen(previous));
    }

    // Start a new farm with the selected weather mode.
    private void startGame(String weatherMode) {
        game.setScreen(FarmScreen.newGame(game, weatherMode));
    }

    // Handle Escape and forward the other keys to the menu.
    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.setScreen(previous);
        } else {
            menu.keyPressed(code);
        }
    }

    // Match the title screen's appearance.
    @Override
    public void draw(Graphics2D g) {
        backdrop.draw(g);
        backdrop.getVillager().draw(g, game.getFrames());

        g.setColor(Ui.WOOD);
        g.fillRect(0, Ui.MAP_HEIGHT, Ui.WIDTH, Ui.HUD_HEIGHT);

        Ui.dim(g, 150);

        // Draw the heading and description.
        Ui.centered(g, "CHOOSE WEATHER MODE", 30, 130, Ui.HIGHLIGHT);
        Ui.centered(g, "Choose the weather for your new farm.",
                15, 180, Color.WHITE);

        // Draw the weather mode menu.
        menu.draw(g, 270, Color.WHITE);

        Ui.centered(g, "UP/DOWN choose    ENTER select    ESC back",
                13, 520, new Color(190, 190, 190));
    }
}
