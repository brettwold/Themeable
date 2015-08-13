package themeable;

/**
 * Created by brett on 13/08/15.
 */
public class ThemeableParseException extends Exception {

    public ThemeableParseException(Exception e) {
        super(e);
    }

    public ThemeableParseException(String msg, Exception e) {
        super(msg, e);
    }
}
