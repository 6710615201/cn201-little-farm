/** A crop name that the farm does not grow, e.g. "Cabbage" in a save file. */
public class UnknownCropException extends FarmException {
    private final String cropName;

    public UnknownCropException(String cropName) {
        super("unknown crop '" + cropName + "'");
        this.cropName = cropName;
    }

    public String getCropName() {
        return cropName;
    }
}
