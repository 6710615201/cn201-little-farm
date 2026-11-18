/** A save file that cannot be read. The message always names the line, e.g. "line 4 is damaged: gold abc". */
public class SaveFormatException extends FarmException {
    private final int lineNumber;

    public SaveFormatException(int lineNumber, String detail) {
        super("line " + lineNumber + " is damaged: " + detail);
        this.lineNumber = lineNumber;
    }

    public SaveFormatException(int lineNumber, String detail, Throwable cause) {
        this(lineNumber, detail);
        initCause(cause);
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
