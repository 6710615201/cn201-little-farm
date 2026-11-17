
import java.awt.Color;
import java.awt.Graphics2D;

// The game's title screen.
public class TitleScreen implements Screen {
    private final GamePanel game;
    private final Farm backdrop = new Farm();   // Farm backdrop for the title menu
    private final Menu menu = new Menu();

    // Build the title menu.
    public TitleScreen(GamePanel game) {
        this.game = game;

        // Check for an existing save.
        String saved = game.getSaveGame().describe();

        if (saved != null) {
            // Continue loads the existing farm.
            menu.add("Continue  (" + saved + ")", () -> FarmScreen.load(game));

            // Ask for confirmation before starting over.
            menu.add("New farm", () -> game.setScreen(new ConfirmScreen(
                    game,
                    this,
                    "Start a new farm?",
                    "Your saved farm will be erased when you next sleep.",
                    "Start over",
                    "Keep my farm",
                    true,
                    // Open the weather mode screen after confirmation.
                    () -> game.setScreen(new WeatherModeScreen(game, this))
            )));
        } else {
            // Open the weather mode screen when there is no existing save.
            menu.add("New farm",
                    () -> game.setScreen(new WeatherModeScreen(game, this)));
        }

        // Add the other title options.
        menu.add("How to play", () -> game.setScreen(new HelpScreen(game, this)));
        menu.add("Quit", game::quit);
    }

    // Pass key presses to the menu.
    @Override
    public void keyPressed(int code) {
        menu.keyPressed(code);
    }

    // Draw the title screen.
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

        // Draw the title menu.
        menu.draw(g, 270, Color.WHITE);

        Ui.centered(g, "UP/DOWN choose    ENTER select",
                13, 520, new Color(190, 190, 190));
    }
}
