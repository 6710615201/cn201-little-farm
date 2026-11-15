public class WrongSeasonException extends FarmException {
    private final String cropName;
    private final String seasonName;

    public WrongSeasonException(String cropName, String seasonName) {
        super(cropName + " can't grow in the " + seasonName + " season");
        this.cropName = cropName;
        this.seasonName = seasonName;
    }

    public WrongSeasonException(String cropName, Season season) {
        this(cropName, season.getName());
    }

    public String getCropName() {
        return cropName;
    }

    public String getSeasonName() {
        return seasonName;
    }
}