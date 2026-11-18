import java.awt.Color;

public class HotSeason extends Season {
    public HotSeason() {
        super(0, "\u2600");
    }

    @Override public String getName() { return "Hot"; }

    @Override
    public Color getTint() {
        return new Color(255, 180, 70, 45);
    }
}