package themeable;

/**
 * Created by brett on 13/08/15.
 */
public class ThemeableParseExeception extends Exception {

    public ThemeableParseExeception(Exception e) {
        super(e);
    }

    public ThemeableParseExeception(String msg, Exception e) {
        super(msg, e);
    }
}
