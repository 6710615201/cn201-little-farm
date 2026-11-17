
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        // Read the selected strategy; use normal when no argument is provided.
        String strategyName = args.length > 0 ? args[0] : "normal";

        // Convert the command-line name into a weather strategy object.
        WeatherStrategy selectedStrategy = StrategyFactory.create(strategyName);

        // Build and start the Swing game on its event thread.
        SwingUtilities.invokeLater(() -> {
            // Pass the selected strategy into the game.
            GamePanel panel = new GamePanel(selectedStrategy);

            JFrame frame = new JFrame("Little Farm - CN201");
            frame.add(panel);
            frame.pack();
            frame.setResizable(true);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            panel.requestFocusInWindow();
            panel.start();
        });
    }
}
