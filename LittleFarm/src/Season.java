import java.awt.Color;

public abstract class Season {
    protected final int stormChance;
    protected final String icon;

    protected Season(int stormChance, String icon) {
        this.stormChance = stormChance;
        this.icon = icon;
    }

    public int getStormChance() {
        return stormChance;
    }

    public String getIcon() {
        return icon;
    }

    /** Stable name used by crop rules and messages. */
    public abstract String getName();

    public abstract Color getTint();
}