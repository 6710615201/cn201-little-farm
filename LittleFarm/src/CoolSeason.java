import java.awt.Color;

public class CoolSeason extends Season {
    public CoolSeason() {
        super(10, "\u2744");
    }

    @Override public String getName() { return "Cool"; }

    @Override
    public Color getTint() {
        return new Color(150, 210, 240, 45);
    }
}
